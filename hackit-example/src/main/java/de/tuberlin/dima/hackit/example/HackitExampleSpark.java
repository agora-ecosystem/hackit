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

package de.tuberlin.dima.hackit.example;

import de.tuberlin.dima.hackit.api.spark.rdd.HackitRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class HackitExampleSpark {

    public static void main(String... args){
        SparkConf conf = new SparkConf()
                .setMaster("local[1]")
                .setAppName("hackit-example");

        JavaSparkContext sc = new JavaSparkContext(conf);
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        JavaRDD<Integer> parallelize = sc.parallelize(list);

        List<Integer> result = HackitRDD.fromJavaRDD(parallelize)
                .map(
                        (Integer i) -> {
                            System.out.println("i = " + i);
                            return i + 1;
                        }
                )
                .addSniffer()
                .toJavaRDD()
                .collect();
        sc.stop();
        result.stream().forEach(System.out::println);
    }
}
