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

package de.tuberlin.dima.hackit.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OperatorType {
    SOURCE,
    SINK,
    UNARY,
    BINARY,
    LOOP;

    @JsonValue
    public String getValue() {
        return this.name().toLowerCase();
    }

    @JsonCreator
    public static OperatorType fromValue(String value) {
        return OperatorType.valueOf(value.toUpperCase());
    }


    public static OperatorType fromClass(Class<?> clazz) {
        String name = clazz.getName();
        String metaClass = clazz.getGenericSuperclass().getTypeName();

        if (
                metaClass.startsWith("org.apache.wayang.core.plan.wayangplan.UnarySource") ||
                     name.equals("org.apache.wayang.core.plan.wayangplan.UnarySource")
        ) {
            return OperatorType.SOURCE;
        } else if (
                metaClass.startsWith("org.apache.wayang.core.plan.wayangplan.UnarySink") ||
                     name.equals("org.apache.wayang.core.plan.wayangplan.UnarySink")
        ) {
            return OperatorType.SINK;
        } else if (
                metaClass.startsWith("org.apache.wayang.core.plan.wayangplan.UnaryToUnaryOperator") ||
                     name.equals("org.apache.wayang.core.plan.wayangplan.UnaryToUnaryOperator")
        ) {
            return OperatorType.UNARY;
        } else if (
                metaClass.startsWith("org.apache.wayang.core.plan.wayangplan.BinaryToUnaryOperator") ||
                     name.equals("org.apache.wayang.core.plan.wayangplan.BinaryToUnaryOperator")
        ) {
            return OperatorType.UNARY;
        } else if (
                metaClass.startsWith("org.apache.wayang.core.plan.wayangplan.BinaryToBinaryOperator") ||
                     name.equals("org.apache.wayang.core.plan.wayangplan.BinaryToBinaryOperator")
        ) {
            return OperatorType.BINARY;
        } else if (
                metaClass.equals("org.apache.wayang.core.plan.wayangplan.LoopHeadOperator") ||
                     name.equals("org.apache.wayang.core.plan.wayangplan.LoopHeadOperator") ||
                metaClass.equals("org.apache.wayang.basic.operators.RepeatOperator") ||
                     name.equals("org.apache.wayang.basic.operators.RepeatOperator") ||
                metaClass.equals("org.apache.wayang.basic.operators.LoopOperator") ||
                     name.equals("org.apache.wayang.basic.operators.LoopOperator") ||
                metaClass.equals("org.apache.wayang.basic.operators.DoWhileOperator") ||
                     name.equals("org.apache.wayang.basic.operators.DoWhileOperator")


        ) {
            return OperatorType.LOOP;
        } else {
            throw new RuntimeException("Unknown operator type: " + metaClass + " of " + clazz.getName());

        }
    }
}
