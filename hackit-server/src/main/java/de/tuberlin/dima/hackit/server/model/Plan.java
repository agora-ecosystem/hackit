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

package de.tuberlin.dima.hackit.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.stream.Stream;


public class Plan {

    @JsonProperty("id")
    String id;

    @JsonProperty("sources")
    Operator[] sources;

    @JsonProperty("sinks")
    Operator[] sinks;

    public Plan() {
    }

    public Plan(String id, Operator[] sources, Operator[] sinks) {
        this.id = id;
        this.sources = sources;
        this.sinks = sinks;
    }

    public String getId() {
        return id;
    }

    public Plan setId(String id) {
        this.id = id;
        return this;
    }

    public Operator[] getSources() {
        return sources;
    }

    public Plan setSources(Operator[] sources) {
        this.sources = sources;
        return this;
    }

    public Operator[] getSinks() {
        return sinks;
    }

    public Plan setSinks(Operator[] sinks) {
        this.sinks = sinks;
        return this;
    }

    public Stream<Operator> getOperators(){
        Set<Operator> operators = new HashSet<>();
        List<Operator> to_visit = new ArrayList<>();
        to_visit.addAll(Arrays.asList(this.sources));

        while(to_visit.size() > 0){
            Operator op = to_visit.remove(0);
            if(operators.contains(op))
                continue;
            operators.add(op);
            to_visit.addAll(Arrays.asList(op.getNext()));
        }

        return operators.stream();
    }


}
