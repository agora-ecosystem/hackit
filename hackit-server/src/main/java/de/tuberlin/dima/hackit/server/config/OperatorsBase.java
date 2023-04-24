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

package de.tuberlin.dima.hackit.server.config;

import org.apache.wayang.basic.operators.*;
import org.apache.wayang.core.plan.wayangplan.OperatorBase;

public enum OperatorsBase {

    TEXT_FILE_SOURCE(TextFileSource.class),
    REDUCE_BY(ReduceByOperator.class),
    REDUCE_OPERATOR(ReduceOperator.class),
    MAP_OPERATOR(MapOperator.class),
    FLAT_MAP_OPERATOR(FlatMapOperator.class),
    LOOP_OPERATOR(LoopOperator.class),
    JOIN_OPERATOR(JoinOperator.class),
    GROUP_BY_OPERATOR(GroupByOperator.class),
    TEXT_FILE_SINK(TextFileSink.class),
    LOCAL_CALL_BACK_SINK(LocalCallbackSink.class);

    private final Class<? extends OperatorBase> clazz;

    OperatorsBase(Class<? extends OperatorBase> clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return this.clazz.getSimpleName();
    }

    public Class<? extends OperatorBase> getClazz() {
        return this.clazz;
    }

}
