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

import java.util.HashMap;

public class UDF {

    @JsonProperty("name")
    String name;

    @JsonProperty("code")
    String code;

    @JsonProperty("language")
    String language;

    @JsonProperty("type")
    Class type;

    @JsonProperty("params")
    HashMap<String, Class> params;

    @JsonProperty("returnType")
    Class returnType;

    public UDF() {
    }

    public UDF(String name, String code, String language, Class type, HashMap<String, Class> params, Class returnType) {
        this.name = name;
        this.code = code;
        this.language = language;
        this.type = type;
        this.params = params;
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public UDF setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public UDF setCode(String code) {
        this.code = code;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public UDF setLanguage(String language) {
        this.language = language;
        return this;
    }

    public Class getType() {
        return type;
    }

    public UDF setType(Class type) {
        this.type = type;
        return this;
    }

    public HashMap<String, Class> getParams() {
        return params;
    }

    public UDF setParams(HashMap<String, Class> params) {
        this.params = params;
        return this;
    }

    public Class getReturnType() {
        return returnType;
    }

    public UDF setReturnType(Class returnType) {
        this.returnType = returnType;
        return this;
    }

    public Object getInstance() {
        try{
            return this.type.getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
