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

package de.tuberlin.dima.hackit.api.spark.rdd;

import de.tuberlin.dima.hackit.api.spark.function.SparkSniffer;
import de.tuberlin.dima.hackit.core.tuple.HackitTuple;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.rdd.RDD;
import scala.Tuple2;

import java.util.Properties;

public class HackitPairRDD<K, KT, V>{
    private JavaPairRDD<KT, HackitTuple<K, V>> rdd;

    /**
     * Constructor
     **/
    public HackitPairRDD(JavaPairRDD<KT, HackitTuple<K, V>> rdd) {
        this.rdd = rdd;
    }

    /**
     * Wrap the RDD inside HackitRDD
     **/
    public static <HK, TK, T> HackitPairRDD<HK, TK, T> fromRDD(JavaPairRDD<TK, T> rdd){
        return new HackitPairRDD<>(
                rdd.mapValues(
                        (Function<T, HackitTuple<HK, T>>) HackitTuple::new
                )
        );
    }


    /**
     * unwrap the RDD and coming back to the normal execution
     **/
    public JavaPairRDD<KT, V> toPairJavaRDD(){
        return this.rdd.mapValues(HackitTuple::getValue);
    }


    public HackitPairRDD<K, KT, V> addSniffer(){
        Properties conf = new Properties();
        //TODO add properties
        SparkSniffer<K, V> sniffer = new SparkSniffer<K, V>(conf);
        return new HackitPairRDD<>(this.rdd
                .flatMapValues(
                        sniffer::apply
                )
        );
    }

    /**
     * Return a new RDD containing only the elements that satisfy a predicate
     * in the reduceByKey
     **/
    public HackitPairRDD<K, KT, V> reduceByKey(Function2<V, V, V> func) {
        HackitPairRDD<K, KT, V> sniffed = this.addSniffer();

        return
            new HackitPairRDD<K, KT, V>(
                sniffed
                    .rdd
                    .mapValues(HackitTuple::getValue)
                    .reduceByKey(func)
                    .mapValues(HackitTuple::new)
            )
            .addSniffer();
    }

    /**
     * Return a new RDD containing only the elements that satisfy a predicate
     * in the join
     **/
    public <V2> HackitPairRDD<K, KT, Tuple2<V, V2>> join(HackitPairRDD<K, KT, V2> other){

        JavaPairRDD<KT, HackitTuple<K, Tuple2<V, V2>>> result = this.rdd
            .join(other.rdd)
            .mapValues(
                tuple -> {
                    HackitTuple<K, V> t1 = tuple._1;
                    HackitTuple<K, V2> t2 = tuple._2;
                    return new HackitTuple<K, Tuple2<V, V2>>(new Tuple2<V, V2>(t1.getValue(), t2.getValue()))
                            .addFather(t1.getKey())
                            .addFather(t2.getKey());
                }
            );
        return new HackitPairRDD<K, KT, Tuple2<V, V2>>(result)
                .addSniffer();
    }

    /**
     * save the RDD as text file
     **/
    public void saveAsTextFile(String path) {
        this.toPairJavaRDD().saveAsTextFile(path);
    }
}
