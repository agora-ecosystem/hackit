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

import de.tuberlin.dima.hackit.api.spark.function.PairFunctionWrapperHackit;
import de.tuberlin.dima.hackit.api.spark.function.SparkSniffer;
import de.tuberlin.dima.hackit.core.tagger.wrapper.FlatmapWrapperHackit;
import de.tuberlin.dima.hackit.core.tagger.wrapper.FunctionWrapperHackit;
import de.tuberlin.dima.hackit.core.tagger.wrapper.PredicateWrapperHackit;
import de.tuberlin.dima.hackit.core.tagger.wrapper.template.FlatMapTemplate;
import de.tuberlin.dima.hackit.core.tags.HackitTag;
import de.tuberlin.dima.hackit.core.tags.LogTag;
import de.tuberlin.dima.hackit.core.tuple.HackitTuple;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.rdd.RDD;

import java.util.Iterator;
import java.util.Properties;

/**
 * HackitRDD is the wrapper of the {@link JavaRDD} that will be used to
 * execute the {@link de.tuberlin.dima.hackit.core.tagger.TaggerFunction}
 * inside of the {@link HackitTag
 * ger}
 *
 * @param <K> Key type of the {@link HackitTuple}
 * @param <T> Value type of the {@link HackitTuple}
 */
public class HackitRDD<K, T> {
  private JavaRDD<HackitTuple<K, T>> rdd;

  private HackitRDD(JavaRDD<HackitTuple<K, T>> rdd) {
    this.rdd = rdd;
  }

  /**
   * Wrap the JavaRDD inside DebugRDD
   * */
  public static <K, T> HackitRDD<K, T> fromJavaRDD(JavaRDD<T> rdd){
    return new HackitRDD<K, T>(
        rdd.map((Function<T, HackitTuple<K, T>>) HackitTuple::new)
    );
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

  // Common RDD functions

  /**
   * This function is used to filter the RDD
   * */
  public HackitRDD<K, T> filter(FilterFunction<T> f) {

    PredicateWrapperHackit<K, T> func = (PredicateWrapperHackit<K, T>) new PredicateWrapperHackit<K, T>(f::call).addPostTag(new LogTag());

    return new HackitRDD<>(
        this.rdd.filter(
                func::execute
        )
    );
  }

  /**
   * This function is used to map the function to the RDD
   * */
  public <O> HackitRDD<K, O> map(Function<T, O> f){
    FunctionWrapperHackit<K, T, O> func = new FunctionWrapperHackit<K, T, O>(f::call);

    return new HackitRDD<>(
        this.rdd.map(
                func::execute
        )
    );
  }


  /**
   * This function is used to map the function to the RDD
   * */
  public <KO, O> HackitPairRDD<K, KO, O> mapToPair(PairFunction<T, KO, O> pairFunction){

    PairFunctionWrapperHackit<K, KO, T, O> pairFunctionWrapperHackit = new PairFunctionWrapperHackit<K, KO, T, O>(pairFunction::call);


    return new HackitPairRDD<>(
        this.rdd.mapToPair(
            pairFunctionWrapperHackit::execute
        )
    );
  }

  /**
   * apply the flatmap function to the RDD
   * */
  public <KO, O> HackitRDD<K, O> flatMap(FlatMapFunction<T, O> flatMapFunction){
    FlatMapTemplate<T, O> flatMapTemplate = new FlatMapTemplate<T, O>(){
      @Override
      public Iterator<O> execute(T input) throws Exception {
        return flatMapFunction.call(input);
      }
    };

    FlatmapWrapperHackit<K, T, O> flatmapWrapperHackit = new FlatmapWrapperHackit<K, T, O>(flatMapTemplate);

    return new HackitRDD<>(
        this.rdd.flatMap(
            flatmapWrapperHackit::execute
        )
    );
  }

  /**
   * create the sniffer inside the pipeline
   * */
  public HackitRDD<K, T> sniffer(SparkSniffer<K, T> hackItSniffer){
    return new HackitRDD<>(
        this.rdd.flatMap(
            hackItSniffer::apply
        )
    );
  }

  /**
   * creates automatically the sniffer inside the pipeline
   * */
  public HackitRDD<K, T> addSniffer(){
    Properties conf = new Properties();

    //TODO add properties
    SparkSniffer<K, T> sniffer = new SparkSniffer<K, T>(conf);
    return this.sniffer(sniffer);
  }


  /**
   * this function store the results into the text file
   * */
  public void saveAsTextFile(String path) {
    this.toJavaRDD().saveAsTextFile(path);
  }
}
