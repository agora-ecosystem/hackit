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
import org.apache.spark.api.java.JavaPairRDD;

public class HackitPairRDD<K, KT, V>{
  private JavaPairRDD<KT, HackitTuple<K, V>> rdd;

  public HackitPairRDD(JavaPairRDD<KT, HackitTuple<K, V>> rdd) {
    this.rdd = rdd;
  }

//  public HackitPairRDD<K, KT, V> reduceByKey(Function2<V, V, V> func) {
//    return new HackitPairRDD<>(
//        this.rdd
//            .mapValues(
//                HackitTuple::getValue
//            )
//            .reduceByKey(
//                func
//            )
//            .mapValues(
//                HackitTuple::new
//            )
//    );
//  }
//
//  public HackitPairRDD<K, KT, V> join(HackitPairRDD<K, KT, V> other){
//    JavaPairRDD<KT, Tuple2<HackitTuple<K, V>, HackitTuple<K, V>>> tmp = this.rdd.join(other.rdd);
//    return null;
//  }


/*
    /**
     * Wrap the RDD inside of DebugRDD
     * *
    public static <T> DebugPairRDD<T> fromRDD(RDD<T> rdd){
        return this(rdd, rdd.elementClassTag());
    }
    /**
     * Wrap the JavaRDD inside of DebugRDD
     * *
    public static <T> DebugPairRDD<T> fromJavaRDD(JavaRDD<T> rdd){
        return new DebugRDD<T>(rdd.rdd(), rdd.classTag());
    }
    /**
     * unwrap the RDD and coming back to the normal execution
     * *
    public RDD<Tuple2<K, V>> toRDD(){
        return this.toPairJavaRDD().rdd();
    }
    /**
     * unwrap the RDD and coming back to the normal execution
     * *
    public JavaPairRDD<K, V> toPairJavaRDD(){
        RDD<Tuple2<K, V>> tmp = this.rdd();
        if(this.classTag().getClass() == HackItTuple.class) {
            tmp = null;//((JavaRDD<HackItTuple<T>>)this.rdd).map(com.qcri.hackit -> com.qcri.hackit.getValue());
        }
        return super.fromRDD(tmp, this.kClassTag(), this.vClassTag());
    }
    public static <T> DebugRDD<T> wrapDebugRDD(JavaRDD<T> rdd){
        return DebugRDD.fromJavaRDD(rdd);
    }
    @Override
    public Map<K, V> reduceByKeyLocally(Function2<V, V, V> func) {
        return super.reduceByKeyLocally(func);
    }
    @Override
    public <W> JavaPairRDD<K, Tuple2<V, W>> join(JavaPairRDD<K, W> other) {
        return super.join(other);
    }
    @Override
    public void saveAsTextFile(String path) {
        super.saveAsTextFile(path);
    }*/
}
