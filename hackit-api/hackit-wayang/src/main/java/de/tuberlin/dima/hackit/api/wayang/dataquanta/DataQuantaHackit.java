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

package de.tuberlin.dima.hackit.api.wayang.dataquanta;

import de.tuberlin.dima.hackit.api.wayang.function.WayangSniffer;
import de.tuberlin.dima.hackit.core.tagger.wrapper.FlatmapWrapperHackit;
import de.tuberlin.dima.hackit.core.tagger.wrapper.FunctionWrapperHackit;
import de.tuberlin.dima.hackit.core.tagger.wrapper.PredicateWrapperHackit;
import de.tuberlin.dima.hackit.core.tags.LogTag;
import de.tuberlin.dima.hackit.core.tuple.HackitTuple;
import org.apache.wayang.api.DataQuanta;
import org.apache.wayang.basic.data.Tuple2;
import org.apache.wayang.core.function.FunctionDescriptor;
import scala.reflect.ClassTag;

import java.util.Iterator;
import java.util.Properties;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This class is a wrapper for the {@link DataQuanta} class of Wayang.
 * It provides a set of methods to manipulate the {@link DataQuanta} in a way that is
 * compatible with the HackIt framework.
 *
 * @param <K> the key type of the {@link DataQuanta}
 * @param <T> the value type of the {@link DataQuanta}
 */
public class DataQuantaHackit<K, T> {
    /**
     * The {@link DataQuanta} that is wrapped by this class.
     */
    private DataQuanta<HackitTuple<K, T>> quanta;

    Class<T> clazzT;

    private DataQuantaHackit(DataQuanta<HackitTuple<K, T>> quanta, Class<T> clazzT) {
        this.quanta = quanta;
        this.clazzT = clazzT;
    }

    /**
     * Casts the given {@link Class} to a more specific one.
     *
     * @param baseClass that should be casted
     * @param <T>       the specific type parameter for the {@code baseClass}
     * @return the {@code baseClass}, casted
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> specify(Class<? super T> baseClass) {
        return (Class<T>) baseClass;
    }

    /**
     * Casts the given {@link Class} to a more general one.
     *
     * @param baseClass that should be casted
     * @param <T>       the specific type parameter for the {@code baseClass}
     * @return the {@code baseClass}, casted
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> generalize(Class<? extends T> baseClass) {
        return (Class<T>) baseClass;
    }

    /**
     * classTag is used to wrap the class inside ClassTag
     * @param clazz the class to be wrapped
     * @return the wrapped class
     * @param <K> the key type of the tuple
     * @param <T> the value type of the tuple
     */
    protected static <K, T> ClassTag<HackitTuple<K, T>> getClassTagHackitTuple(Class<HackitTuple> clazz) {
        return scala.reflect.ClassTag$.MODULE$.apply(clazz);
    }

    protected static <A, B> ClassTag<Tuple2<A, B>> getClassTagTuple2(Class<Tuple2> clazz) {
        return scala.reflect.ClassTag$.MODULE$.apply(clazz);
    }

    /**
     * classTag is used to wrap the class inside ClassTag
     * @param clazz the class to be wrapped
     * @return the wrapped class
     * @param <T> the value type of the tuple
     */
    protected static <T> ClassTag<T> getClassTag(Class<T> clazz) {
        return scala.reflect.ClassTag$.MODULE$.apply(clazz);
    }

    /**
     * Wrap the {@link DataQuanta} inside {@link DataQuantaHackit}
     * */
    public static <K, T> DataQuantaHackit<K, T> fromDataQuanta(DataQuanta<T> quanta, Class<T> tClass){
        FunctionDescriptor.SerializableFunction<T, HackitTuple<K, T>> func = new FunctionDescriptor.SerializableFunction<T, HackitTuple<K, T>>() {
            @Override
            public HackitTuple<K, T> apply(T t) {
                return new HackitTuple<>(t);
            }
        };
        ClassTag<HackitTuple<K, T>> tag = getClassTagHackitTuple(HackitTuple.class);

        DataQuanta<HackitTuple<K, T>> data = quanta.mapJava(func, null, tag);
        return new DataQuantaHackit<K, T>(data, tClass);
    }

    /**
     * unwrap the DataQuanta and coming back to the normal execution
     * */
    public DataQuanta<T> toDataquante(){
        return this.quanta.mapJava(HackitTuple::getValue, null, getClassTag(this.clazzT));
    }

    // Common RDD functions

    /**
     * This function is used to filter the {@link DataQuanta}
     * */
    public DataQuantaHackit<K, T> filter(FunctionDescriptor.SerializablePredicate<T> f) {


        PredicateWrapperHackit<K, T> func = (PredicateWrapperHackit<K, T>) new PredicateWrapperHackit<K, T>(f::test).addPostTag(new LogTag());


        DataQuanta<HackitTuple<K, T>> data = quanta.filterJava(
                e -> {
                    try {
                        return func.execute(e);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return false;
                    }
                },
                null,
                null,
                null
        );
        return new DataQuantaHackit<K, T>(data, this.clazzT);
    }

    /**
     * This function is used to map the function to the {@link DataQuanta}
     * */
    public <O> DataQuantaHackit<K, O> map(FunctionDescriptor.SerializableFunction<T, O> f, Class<O> clazzO){

        FunctionWrapperHackit<K, T, O> func = new FunctionWrapperHackit<K, T, O>(f::apply);
        ClassTag<HackitTuple<K, O>> tag = getClassTagHackitTuple(HackitTuple.class);

        DataQuanta<HackitTuple<K, O>> data = quanta.mapJava(
                e -> {
                    try {
                        return func.execute(e);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return null;
                    }
                },
                null,
                tag
        );

        return new DataQuantaHackit<>(data, clazzO);
    }

    /**
     * this function transform the iterator into an iterable
     * @param iterator
     * @return
     * @param <T>
     */
    public static <T> Iterable<T> getIterableFromIterator(Iterator<T> iterator) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }

    /**
     * apply the flatmap function to the {@link DataQuanta}
     * */
    public <O> DataQuantaHackit<K, O> flatMap(FunctionDescriptor.SerializableFunction<T, java.lang.Iterable<O>> flatMapFunction, Class<O> clazzO){

        FlatmapWrapperHackit<K, T, O> flatmapWrapperHackit = new FlatmapWrapperHackit<K, T, O>(
            t -> {
                return flatMapFunction.apply(t).iterator();
            }
        );

        ClassTag<HackitTuple<K, O>> tag = getClassTagHackitTuple(HackitTuple.class);

        DataQuanta<HackitTuple<K, O>> data = quanta.flatMapJava(
                e -> {
                    try {
                        return getIterableFromIterator(flatmapWrapperHackit.execute(e));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        return null;
                    }
                },
                null,
                null,
                tag
        );

        return new DataQuantaHackit<>(data, clazzO);
    }

    /**
     * create the sniffer inside the pipeline
     * */
    public DataQuantaHackit<K, T> sniffer(WayangSniffer<K, T> hackItSniffer){

        ClassTag<HackitTuple<K, T>> tag = getClassTagHackitTuple(HackitTuple.class);

        return new DataQuantaHackit<>(
            this.quanta.flatMapJava(
                e -> {
                    return getIterableFromIterator(hackItSniffer.apply(e));
                },
                null,
                null,
                tag
            ),
            this.clazzT
        );
    }

    /**
     * creates automatically the sniffer inside the pipeline
     * */
    public DataQuantaHackit<K, T> addSniffer(){
        Properties conf = new Properties();
        //TODO add properties
        WayangSniffer<K, T> sniffer = new WayangSniffer<K, T>(conf);
        return this.sniffer(sniffer);
    }


    /**
     * this function store the results into the text file
     * */
    public void saveAsTextFile(String path) {
        this.quanta.writeTextFile(path, HackitTuple::toString, null);
    }

    /**
     * This function is used to map the function to the {@link DataQuanta}
     * */
    public <KT, T2> DataQuantaHackit<K, Tuple2<T, T2>> join(
            FunctionDescriptor.SerializableFunction<T, KT> keyExtractor,
            DataQuantaHackit<K, T2> other,
            FunctionDescriptor.SerializableFunction<T2, KT> keyExtractorOther,
            Class<KT> clazzKT,
            Class<T2> clazzT2
    ){
        FunctionWrapperHackit<K, T, KT> extractor1 = new FunctionWrapperHackit<K, T, KT>(keyExtractor::apply);
        FunctionWrapperHackit<K, T2, KT> extractor2 = new FunctionWrapperHackit<K, T2, KT>(keyExtractorOther::apply);

        ClassTag<Tuple2<KT, HackitTuple<K, T2>>> t2tag = getClassTagTuple2(Tuple2.class);
        ClassTag<KT> keyTag = getClassTag(clazzKT);

        DataQuanta<Tuple2<KT, HackitTuple<K, T>>> this_key = this.quanta
                .map(
                        t -> {
                            try {
                                return new Tuple2<KT, HackitTuple<K, T>>(keyExtractor.apply(t.getValue()), t);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        },
                        null,
                        getClassTagTuple2(Tuple2.class)
                );

        DataQuanta<Tuple2<KT, HackitTuple<K, T2>>> other_key = other.quanta
                .map(
                        t -> {
                            try {
                                return new Tuple2<KT, HackitTuple<K, T2>>(keyExtractorOther.apply(t.getValue()), t);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        },
                        null,
                        getClassTagTuple2(Tuple2.class)
                );

        return new DataQuantaHackit<K, Tuple2<T, T2>>(
            this_key.join(
                t -> t.field0,
                other_key,
                t -> t.field0,
                t2tag,
                keyTag
            ).mapJava(
                    tuple -> {
                        HackitTuple<K, T> t1 = tuple.getField0().getField1();
                        HackitTuple<K, T2> t2 = tuple.getField1().getField1();
                        return new HackitTuple<K, Tuple2<T, T2>>(new Tuple2<T, T2>(t1.getValue(), t2.getValue()))
                                .addFather(t1.getKey())
                                .addFather(t2.getKey());
                    },
                    null,
                    getClassTagHackitTuple(HackitTuple.class)
            ),
            specify(Tuple2.class)
        ).addSniffer();
    }

    /**
     * This function is used to reduceByKey the function to the {@link DataQuanta}
     * */
    public <KT> DataQuantaHackit<K, T> reduceByKey(
            FunctionDescriptor.SerializableFunction<T, KT> keyExtractor,
            FunctionDescriptor.SerializableBinaryOperator<T> reduceFunction,
            Class<KT> clazzKT
    ){
        ClassTag<HackitTuple<K, T>> tag = getClassTagHackitTuple(HackitTuple.class);
        ClassTag<HackitTuple<K, Tuple2<KT, T>>> tagTuple = getClassTagHackitTuple(HackitTuple.class);

        Properties conf = new Properties();
        //TODO add properties
        WayangSniffer<K, T> sniffer = new WayangSniffer<K, T>(conf);
        WayangSniffer<K, Tuple2<KT, T>> sniffer2 = new WayangSniffer<K, Tuple2<KT, T>>(conf);

        DataQuanta<HackitTuple<K, Tuple2<KT, T>>> presniffer = this.quanta
            .map(
                    t -> {
                        try {
                            return new Tuple2<KT, HackitTuple<K, T>>(keyExtractor.apply(t.getValue()), t);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    },
                    null,
                    getClassTagTuple2(Tuple2.class)
            )
            //Sniffer
            .flatMapJava(
                e -> {
                    Stream<HackitTuple<K, T>> stream = StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(
                            sniffer.apply(e.getField1()),
                            Spliterator.ORDERED
                        ),
                        false
                    );

                    Iterable<HackitTuple<K, Tuple2<KT, T>>> tmp = getIterableFromIterator(
                        stream.map(
                            t -> {
                                return new HackitTuple<K, Tuple2<KT, T>>(
                                        t.getHeader(),
                                        new Tuple2<KT, T>(
                                            e.getField0(),
                                            t.getValue()
                                        )
                                );
                            }
                        )
                        .iterator()
                    );
                    return tmp;
                },
                null,
                null,
                tagTuple
            )
        ;

        DataQuanta<HackitTuple<K, T>> postReduce = presniffer
            .mapJava(
                tuple -> {
                    return tuple.getValue();
                },
                null,
                getClassTagTuple2(Tuple2.class)
            )
            .reduceByKey(
                t -> t.field0,
                (origin, other) -> {
                    return new Tuple2<KT, T>(
                        origin.field0,
                        reduceFunction.apply(
                            origin.field1,
                            other.field1
                        )
                    );
                },
                null,
                getClassTag(clazzKT)
            )
            //Sniffer
            .flatMapJava(
                e -> {
                    HackitTuple<K, Tuple2<KT, T>> tuple = new HackitTuple<>(e);
                    return getIterableFromIterator(sniffer2.apply(tuple));
                },
                null,
                null,
                getClassTagHackitTuple(HackitTuple.class)
            )
            .mapJava(
                tuple -> {
                    return new HackitTuple<K, T>(tuple.getHeader(), tuple.getValue().getField1());
                },
                null,
                getClassTagHackitTuple(HackitTuple.class)
            )
        ;

        return new DataQuantaHackit<K, T>(postReduce, this.clazzT).addSniffer();
    }
}
