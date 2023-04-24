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

import de.tuberlin.dima.hackit.api.wayang.dataquanta.DataQuantaHackit;
import org.apache.wayang.api.JavaPlanBuilder;
import org.apache.wayang.api.LoadCollectionDataQuantaBuilder;
import org.apache.wayang.core.api.WayangContext;
import org.apache.wayang.java.Java;
import scala.collection.Iterable;

import java.util.Arrays;
import java.util.List;

public class HackitExampleWayang {
    public static void main(String[] args) throws Exception{

        WayangContext wayangContext = new WayangContext().with(Java.basicPlugin());

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);


        LoadCollectionDataQuantaBuilder<Integer> wayangCollection = new JavaPlanBuilder(wayangContext)
                .loadCollection(list);


        Iterable<Integer> result = DataQuantaHackit.fromDataQuanta(wayangCollection.dataQuanta(), Integer.class)
                .map(
                        (Integer i) -> {
                            System.out.println("i = " + i);
                            return i + 1;
                        },
                        Integer.class
                )
                .addSniffer()
                .toDataquante()
                .collect();

        result.forall((Integer i) -> {
            System.out.println("i2 = " + i);
            return true;
        });
    }
}
