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

package de.tuberlin.dima.hackit.api.spark.function;

import de.tuberlin.dima.hackit.core.tagger.HackitTagger;
import de.tuberlin.dima.hackit.core.tagger.wrapper.FunctionWrapperHackit;
import de.tuberlin.dima.hackit.core.tagger.wrapper.template.FunctionTemplate;
import de.tuberlin.dima.hackit.core.tuple.HackitTuple;
import scala.Tuple2;

public class PairFunctionWrapperHackit <IDType,K,  I, O>
        extends HackitTagger
        implements FunctionTemplate<HackitTuple<IDType, I>, Tuple2<K, HackitTuple<IDType, O>>> {

    /**
     * Original function that will transform the data
     */
    private FunctionTemplate<I, Tuple2<K, O>> function;

    /**
     * Default Construct
     *
     * @param function is the function that will be Wrapped by the {@link FunctionWrapperHackit}
     */
    public PairFunctionWrapperHackit(FunctionTemplate<I, Tuple2<K, O>> function) {
        this.function = function;
    }

    @Override
    public Tuple2<K, HackitTuple<IDType, O>> execute(HackitTuple<IDType, I> v1) throws Exception{
        this.preTaggingTuple(v1);
        Tuple2<K, O> result = this.function.execute(v1.getValue());
        return new Tuple2<>(result._1(), this.postTaggingTuple(v1, result._2()));
    }

}
