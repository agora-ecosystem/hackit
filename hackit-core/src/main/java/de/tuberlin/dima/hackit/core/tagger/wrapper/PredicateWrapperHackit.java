/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tuberlin.dima.hackit.core.tagger.wrapper;

import de.tuberlin.dima.hackit.core.tagger.HackitTagger;
import de.tuberlin.dima.hackit.core.tagger.wrapper.template.FunctionTemplate;
import de.tuberlin.dima.hackit.core.tuple.HackitTuple;
import de.tuberlin.dima.hackit.core.tuple.header.Header;


/**
 * PredicateWrapperHackit is an implementation of {@link FunctionTemplate} where
 * Hackit manage the logic before and after of tagging process, also it perform
 * to unwrap of the tuple to be handles by the original function. The original
 * {@link FunctionTemplate} it a predicate function because return
 * a {@link Boolean}
 *
 * @param <IDType> Type of {@link Header} key of the {@link HackitTuple}
 * @param <I> Input Type of the original Tuple to be evaluated
 */
public class PredicateWrapperHackit<IDType, I>
        extends HackitTagger
        implements FunctionTemplate<HackitTuple<IDType, I>, Boolean> {

    /**
     * Original predicate that will evaluate the data to give a True or False value
     */
    private FunctionTemplate<I, Boolean> function;

    /**
     * Default Construct
     *
     * @param function is the predicate that will be Wrapped by the
     * {@link PredicateWrapperHackit}
     */
    public PredicateWrapperHackit(FunctionTemplate<I, Boolean> function) {
        this.function = function;
    }

    @Override
    public Boolean execute(HackitTuple<IDType, I> v1) {
        this.preTaggingTuple(v1);
        Boolean result = this.function.execute(v1.getValue());
        this.postTaggingTuple(v1);
        return result;
    }
}
