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

import de.tuberlin.dima.hackit.core.tuple.HackitTuple;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.RDD;

public class HackitRDD<K, T> {
  private JavaRDD<HackitTuple<K, T>> rdd;

  private HackitRDD(JavaRDD<HackitTuple<K, T>> rdd) {
    this.rdd = rdd;
  }

  /**
   * Wrap the JavaRDD inside DebugRDD
   * */
  public static <K, T> HackitRDD<K, T> fromJavaRDD(JavaRDD<T> rdd){
    HackitRDD<K, T> ktHackItRDD = new HackitRDD<K, T>(
        rdd.map((Function<T, HackitTuple<K, T>>) HackitTuple::new)
    );
    return ktHackItRDD;
  }

  /**
   * unwrap the RDD and coming back to the normal execution
   * */
  public RDD<T> toRDD(){
    return this.toJavaRDD().rdd();
  }

  /**
   * unwrap the RDD and coming back to the normal execution
   * */
  public JavaRDD<T> toJavaRDD(){
    return this.rdd.map(hackit -> hackit.getValue());
  }

  public static <K, T> HackitRDD<K, T> wrapDebugRDD(JavaRDD<T> rdd){
    return HackitRDD.fromJavaRDD(rdd);
  }
  // Common RDD functions
//
//  public HackitRDD<K, T> filter(Function<T, Boolean> f) {
//    HackitRDD<K, T> ktHackItRDD = new HackitRDD<>(
//        this.rdd.filter(
//            (Function<HackitTuple<K, T>, Boolean>) new PredicateHackItTagger<K, T>(f).addPostTag(new LogTag())
//        )
//    );
//    return ktHackItRDD;
//  }
//
//  public <O> HackitRDD<K, O> map(Function<T, O> f){
//    HackitRDD<K, O> ktHackItRDD = new HackitRDD<>(
//        this.rdd.map(
//            new FunctionHackItTagger<>(f)
//        )
//    );
//    return ktHackItRDD;
//  }
//
//  public <KO, O> HackitRDD<K, KO, O> mapToPair(
//      PairFunction<T, KO, O> pairFunction){
//    HackitRDD<K, KO, O> ktDebugRDD = new HackitRDD<>(
//        this.rdd.mapToPair(
//            new PairFunctionHackItTagger<>(pairFunction)
//        )
//    );
//    return ktDebugRDD;
//  }
//
//  public <KO, O> HackitRDD<K, O> flatMap(FlatMapFunction<T, O> flatMapFunction){
//    HackitRDD<K, O> koHackItRDD = new HackitRDD<>(
//        this.rdd.flatMap(
//            new FlatMapFunctionHackItTagger<K, T, O>(
//                flatMapFunction
//            )
//        )
//    );
//    return koHackItRDD;
//  }
//
//  public HackitRDD<K, T> sniffer(){
//    HackItSnifferSpark<Long, String, byte[], SenderMultiChannelRabbitMQ<byte[]>, ReceiverMultiChannelRabbitMQ<Long, String>> lala = new HackItSnifferSpark<Long, String, byte[], SenderMultiChannelRabbitMQ<byte[]>, ReceiverMultiChannelRabbitMQ<Long, String>>(
//        new EmptyHackItInjector<>(),
//        ele -> true,
//        new HachitShipperDirectRabbitMQ(),
//        ele -> true,
//        new DefaultCloner<HackitTuple<Long, String>>()
//    );
//    return this.sniffer(lala);
//  }
//
//  public HackitRDD<K, T> sniffer(HackItSnifferSpark hackItSniffer){
//    return new HackitRDD<>(
//        this.rdd.flatMap(
//            hackItSniffer
//        )
//    );
//  }

    /*@Override
    public <R> DebugRDD<R> map(Function<T, R> f) {
        return null;
    }
    @Override
    public <K2, V2> DebugRDD<K2, V2> mapToPair(PairFunction<T, K2, V2> f) {
        return null;
    }
    @Override
    public <U> DebugRDD<U> flatMap(FlatMapFunction<T, U> f) {
        return null;
    }
    @Override
    public <K2, V2> DebugRDD<K2, V2> flatMapToPair(PairFlatMapFunction<T, K2, V2> f) {
        return null;
    }
    */

  public void saveAsTextFile(String path) {
    this.toJavaRDD().saveAsTextFile(path);
  }
}
