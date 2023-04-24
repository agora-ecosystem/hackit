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

import org.apache.wayang.core.plugin.Plugin;
import org.apache.wayang.java.Java;
import org.apache.wayang.spark.Spark;

public enum Platform {

    SPARK("spark", Spark.basicPlugin()),
    JAVA("java", Java.basicPlugin());

    private final String name;
    private final Plugin plugin;

    Platform(String name, Plugin plugin) {
        this.name = name;
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public String getName() {
        return this.name;
    }


    public static Platform getInstance(String name){

        for(Platform platform : Platform.values()){
            if(platform.getName().equals(name.toLowerCase())){
                return platform;
            }
        }
        return null;
    }


}
