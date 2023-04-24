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

public class Types {

    @JsonProperty("name")
    String name;

    @JsonProperty("code")
    String code;


    public Types() {
    }


    public Types(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Types setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Types setCode(String code) {
        this.code = code;
        return this;
    }
}
