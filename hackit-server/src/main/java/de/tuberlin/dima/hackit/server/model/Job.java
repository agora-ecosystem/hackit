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

public class Job {

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;

    @JsonProperty("plan")
    Plan[] plan;

    @JsonProperty("params")
    String[] params;

    @JsonProperty("udfs")
    HashMap<String, UDF> udfs;

    @JsonProperty("jars")
    byte[] jars;

    @JsonProperty("isRunning")
    boolean isRunning;

    @JsonProperty("processID")
    int processID;


    public Job() {
    }

    public Job(String id, String name, Plan[] plan, String[] params, HashMap<String, UDF> udfs, byte[] jars, boolean isRunning, int processID) {
        this.id = id;
        this.name = name;
        this.plan = plan;
        this.params = params;
        this.udfs = udfs;
        this.jars = jars;
        this.isRunning = isRunning;
        this.processID = processID;
    }

    public String getId() {
        return id;
    }

    public Job setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Job setName(String name) {
        this.name = name;
        return this;
    }

    public Plan getPlan(String planId){
        if (plan != null) {
            for (Plan p : plan) {
                if (p.getId().equals(planId)) {
                    return p;
                }
            }
        }
        return null;
    }

    public Plan[] getPlans() {
        return plan;
    }

    public Job setPlan(Plan[] plan) {
        this.plan = plan;
        return this;
    }

    public String[] getParams() {
        return params;
    }

    public Job setParams(String[] params) {
        this.params = params;
        return this;
    }

    public HashMap<String, UDF> getUdfs() {
        return udfs;
    }

    public Job setUdfs(HashMap<String, UDF> udfs) {
        this.udfs = udfs;
        return this;
    }

    public UDF getUDF(String udfName){
        return udfs.get(udfName);
    }


    public byte[] getJars() {
        return jars;
    }

    public Job setJars(byte[] jars) {
        this.jars = jars;
        return this;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Job setRunning(boolean running) {
        isRunning = running;
        return this;
    }

    public int getProcessID() {
        return processID;
    }

    public Job setProcessID(int processID) {
        this.processID = processID;
        return this;
    }

    public Job addPlan(Plan plan) {
        if (this.plan == null) {
            this.plan = new Plan[1];
            this.plan[0] = plan;
        } else {
            Plan[] newPlan = new Plan[this.plan.length + 1];
            System.arraycopy(this.plan, 0, newPlan, 0, this.plan.length);
            newPlan[this.plan.length] = plan;
            this.plan = newPlan;
        }
        return this;
    }

    public Job removePlan(String id) {
        if (this.plan != null) {
            Plan[] newPlan = new Plan[this.plan.length - 1];
            int i = 0;
            for (Plan plan : this.plan) {
                if (!plan.getId().equals(id)) {
                    newPlan[i] = plan;
                    i++;
                }
            }
            this.plan = newPlan;
        }
        return this;
    }

    public Platform[] getPlatforms(){
        Set<Platform> platforms = new HashSet<>();
        for (Plan p : plan) {
            p.getOperators().map(Operator::getPlatform).forEach(platforms::add);
        }
        return platforms.toArray(new Platform[0]);
    }

    public Set<Operator> getOperators(){
        Set<Operator> operators = new HashSet<>();
        for (Plan p : plan) {
            p.getOperators().forEach(operators::add);
        }
        return operators;
    }
}
