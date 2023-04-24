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
import org.apache.wayang.core.plan.wayangplan.OperatorBase;

import java.util.HashMap;

public class Operator {

    @JsonProperty("name")
    String name;
    @JsonProperty("aClass")
    Class<? extends OperatorBase> aClass;

    @JsonProperty("type")
    OperatorType type;

    @JsonProperty("udf")
    String[] udf;

    @JsonProperty("inputType")
    Class<?>[] inputType;

    @JsonProperty("outputType")
    Class<?>[] outputType;

    @JsonProperty("previous")
    Operator[] previous;

    @JsonProperty("next")
    Operator[] next;

    @JsonProperty("platform")
    Platform platform;

    @JsonProperty("params")
    HashMap<String, Object> params;

    public Operator() {
    }

    public Operator(
            String name,
            Class<? extends OperatorBase> aClass,
            OperatorType type,
            String[] udf,
            Class<?>[] inputType,
            Class<?>[] outputType,
            Operator[] previous,
            Operator[] next,
            Platform platform,
            HashMap<String, Object> params
    ) {
        this.name = name;
        this.aClass = aClass;
        this.type = type;
        this.udf = udf;
        this.inputType = inputType;
        this.outputType = outputType;
        this.previous = previous;
        this.next = next;
        this.platform = platform;
        this.params = params;
    }


    public String getName() {
        return name;
    }

    public Operator setName(String name) {
        this.name = name;
        return this;
    }

    public Class<? extends OperatorBase> getaClass() {
        return aClass;
    }

    public Operator setaClass(Class<? extends OperatorBase> aClass) {
        this.aClass = aClass;
        return this;
    }

    public OperatorType getType() {
        return type;
    }

    public Operator setType(OperatorType type) {
        this.type = type;
        return this;
    }

    public String[] getUdf() {
        return udf;
    }

    public Operator setUdf(String[] udf) {
        this.udf = udf;
        return this;
    }

    public Class<?>[] getInputType() {
        return inputType;
    }

    public Operator setInputType(Class<?>[] inputType) {
        this.inputType = inputType;
        return this;
    }

    public Class<?>[] getOutputType() {
        return outputType;
    }

    public Operator setOutputType(Class<?>[] outputType) {
        this.outputType = outputType;
        return this;
    }

    public Operator[] getPrevious() {
        return previous;
    }

    public Operator setPrevious(Operator[] previous) {
        this.previous = previous;
        return this;
    }

    public Operator[] getNext() {
        return next;
    }

    public Operator setNext(Operator[] next) {
        this.next = next;
        return this;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Operator setPlatform(Platform platform) {
        this.platform = platform;
        return this;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public Operator setParams(HashMap<String, Object> params) {
        this.params = params;
        return this;
    }


}


