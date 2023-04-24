/*
 * Copyright 2022 DIMA/TU-Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tuberlin.dima.hackit.server.controller;

import de.tuberlin.dima.hackit.server.model.*;
import de.tuberlin.dima.hackit.server.repository.JobRepository;
import org.apache.wayang.api.*;
import org.apache.wayang.basic.operators.*;
import org.apache.wayang.core.api.WayangContext;
import org.apache.wayang.core.function.FunctionDescriptor;
import org.apache.wayang.core.plugin.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping("/executor")
public class ExecutorController {

    ThreadPoolExecutor executor;
    JobRepository jobRepository;
    @Autowired
    public ExecutorController(){
        this.jobRepository = JobRepository.getInstance();
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    }

    @PostMapping(value = "/execute/{jobId}", produces = "application/json")
    public Job executeJob(@PathVariable("jobId") String jobId) {

        Job job = this.jobRepository.getJob(jobId);

        if(job == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "job not found"
            );
        }

        this.executor.execute(new WayangRunnable(job));

        return job;
    }

    class WayangRunnable implements Runnable {

        private Job job;

        public WayangRunnable(Job job){
            this.job = job;
        }

        @Override
        public void run() {
            Plugin[] plugins = this.getPlatforms(job.getPlatforms());

            WayangContext wayangContext = new WayangContext();
            for (Plugin plugin : plugins) {
                wayangContext.withPlugin(plugin);
            }

            JavaPlanBuilder builder = new JavaPlanBuilder(wayangContext);

            HashMap<Operator, DataQuantaBuilder> operator_instance = this.emptyMap(job.getOperators());
            HashMap<String, UDF> udfs = job.getUdfs();

            Set<Operator> visited = new HashSet<>();
            List<Operator> to_visit = new ArrayList<>();

            for(Plan plan: job.getPlans()){
                for(Operator source: plan.getSources()){
                    operator_instance.put(source, this.getInstance(source, builder, operator_instance, udfs));
                    to_visit.addAll(Arrays.asList(source.getNext()));
                    visited.add(source);
                }
            }

            while (to_visit.size() > 0){
                Operator operator = to_visit.get(0);
                to_visit.remove(0);
                if(visited.contains(operator)){
                    continue;
                }
                DataQuantaBuilder instance = this.getInstance(operator, builder, operator_instance, udfs);
                if(instance == null){
                    to_visit.add(operator);
                    break;
                }
                operator_instance.put(operator, instance);
                to_visit.addAll(Arrays.asList(operator.getNext()));
                visited.add(operator);
            }

        }

        private Plugin[] getPlatforms(Platform[] platforms) {
            return (Plugin[]) Arrays.stream(platforms)
                    .map(p -> {
                        return Platform.getInstance(p.getName()).getPlugin();
                    })
                    .filter(Objects::nonNull)
                    .toArray();
        }

        private HashMap<Operator, DataQuantaBuilder> emptyMap(Set<Operator> operatorSet) {
            HashMap<Operator, DataQuantaBuilder> operator_instance = new HashMap<>();
            for (Operator operator : operatorSet) {
                operator_instance.put(operator, null);
            }
            return operator_instance;
        }

        private DataQuantaBuilder getInstance(Operator operator, JavaPlanBuilder builder, HashMap<Operator, DataQuantaBuilder> operator_instance, HashMap<String, UDF> udfs) {
            switch (operator.getType()) {
                case SOURCE:
                    return this.convertSource(operator, builder);
                case UNARY:
                    return this.convertUnary(operator, operator_instance, udfs);
                case BINARY:
                    return this.convertBinary(operator, operator_instance, udfs);
                case LOOP:
                    throw new RuntimeException("Operator type not supported: " + operator.getType());
                case SINK:
                    return this.convertSink(operator, operator_instance, udfs);
                default:
                    throw new RuntimeException("Operator type not supported: " + operator.getType());
            }
        }

        private DataQuantaBuilder convertSource(Operator operator, JavaPlanBuilder builder) {
            if (operator.getaClass() == TextFileSource.class) {
                return builder.readTextFile((String) operator.getParams().get("path"));
            }
            if (operator.getaClass() == CollectionSource.class) {
                return builder.loadCollection((Collection) operator.getParams().get("collection"));
            }
            throw new RuntimeException("Operator type not supported: " + operator.getaClass());
        }

        private DataQuantaBuilder convertUnary(Operator operator, HashMap<Operator, DataQuantaBuilder> operator_instance, HashMap<String, UDF> udfs) {
            if (operator.getaClass() == MapOperator.class) {
                Operator prev = operator.getPrevious()[0];
                DataQuantaBuilder prev_instance = operator_instance.get(prev);
                if (prev_instance == null) {
                    return null;
                }

                MapDataQuantaBuilder tmp = prev_instance.map(
                        getFunction((String) operator.getParams().get("function"), udfs)
                );
                operator_instance.put(operator, tmp);
                return tmp;
            }

            if (operator.getaClass() == FilterOperator.class) {
                Operator prev = operator.getPrevious()[0];
                DataQuantaBuilder prev_instance = operator_instance.get(prev);
                if (prev_instance == null) {
                    return null;
                }

                FilterDataQuantaBuilder tmp = prev_instance.filter(
                        getPredicate((String) operator.getParams().get("predicate"), udfs)
                );
                operator_instance.put(operator, tmp);
                return tmp;
            }

            if (operator.getaClass() == FlatMapOperator.class) {
                Operator prev = operator.getPrevious()[0];
                DataQuantaBuilder prev_instance = operator_instance.get(prev);
                if (prev_instance == null) {
                    return null;
                }

                FlatMapDataQuantaBuilder tmp = prev_instance.flatMap(
                        getFunction((String) operator.getParams().get("function"), udfs)
                );
                operator_instance.put(operator, tmp);
                return tmp;
            }
            throw new RuntimeException("Operator type not supported: " + operator.getaClass());
        }

        private DataQuantaBuilder convertBinary(Operator operator, HashMap<Operator, DataQuantaBuilder> operator_instance, HashMap<String, UDF> udfs) {
            if (operator.getaClass() == JoinOperator.class) {
                Operator prev1 = operator.getPrevious()[0];
                Operator prev2 = operator.getPrevious()[1];
                DataQuantaBuilder prev1_instance = operator_instance.get(prev1);
                DataQuantaBuilder prev2_instance = operator_instance.get(prev2);
                if (prev1_instance == null || prev2_instance == null) {
                    return null;
                }

                JoinDataQuantaBuilder tmp = prev1_instance.join(
                        getFunction((String)operator.getParams().get("getKey1"), udfs),
                        prev2_instance,
                        getFunction((String) operator.getParams().get("getKey2"), udfs)
                );
                operator_instance.put(operator, tmp);
                return tmp;
            }
            throw new RuntimeException("Operator type not supported: " + operator.getaClass());
        }

        private DataQuantaBuilder convertSink(Operator operator, HashMap<Operator, DataQuantaBuilder> operator_instance, HashMap<String, UDF> udfs) {
            if (operator.getaClass() == TextFileSink.class) {
                Operator prev = operator.getPrevious()[0];
                DataQuantaBuilder prev_instance = operator_instance.get(prev);
                if (prev_instance == null) {
                    return null;
                }

                prev_instance.writeTextFile(
                        (String) operator.getParams().get("path"),
                        getFunction( (String) operator.getParams().get("formatFunction"), udfs ),
                        (String) operator.getParams().get("name job")
                );
                return prev_instance;
            }
            throw new RuntimeException("Operator type not supported: " + operator.getaClass());
        }

        private FunctionDescriptor.SerializableFunction<?, ?> getFunction(String name, HashMap<String, UDF> udfs) {
            if (udfs.containsKey(name)) {
                return (FunctionDescriptor.SerializableFunction<?, ?> ) udfs.get(name).getInstance();
            }
            throw new RuntimeException("UDF not found: " + name);
        }

        private FunctionDescriptor.SerializablePredicate<?> getPredicate(String name, HashMap<String, UDF> udfs){
            if (udfs.containsKey(name)) {
                return (FunctionDescriptor.SerializablePredicate<?>) udfs.get(name).getInstance();
            }
            throw new RuntimeException("UDF not found: " + name);
        }
    }

}
