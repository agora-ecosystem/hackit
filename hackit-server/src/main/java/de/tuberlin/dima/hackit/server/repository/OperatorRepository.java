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

package de.tuberlin.dima.hackit.server.repository;

import de.tuberlin.dima.hackit.server.model.Operator;
import de.tuberlin.dima.hackit.server.model.OperatorType;
import org.apache.wayang.core.plan.wayangplan.OperatorBase;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.HashMap;

public class OperatorRepository {

    private static OperatorRepository INSTANCE = null;

    private static String OPERATOR_PACKAGE = "org.apache.wayang.basic.operators";
    private final HashMap<String, Operator> operators = new HashMap<>();

    private OperatorRepository(){
        init();
    }

    public static OperatorRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OperatorRepository();
        }
        return INSTANCE;
    }


    private void init() {
        Reflections reflections = new Reflections(OPERATOR_PACKAGE);
        reflections.getSubTypesOf(OperatorBase.class)
                .stream()
                .distinct()
                .map(record -> {
                    return new Operator(
                            record.getName(),
                            record,
                            OperatorType.fromClass(record),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                })
                .forEach(this::addOperator);
    }

    public Operator addOperator(Operator operator) {
        if(operators.containsKey(operator.getName())){
            throw new IllegalArgumentException("Operator already exists with name \""+ operator.getName() + "\"");
        }
        operators.put(operator.getName(), operator);
        return operator;
    }

    public Operator getOperator(String name) {
        return operators.get(name);
    }

    public Collection<Operator> getOperators() {
        return operators.values();
    }

    public Operator deleteOperator(String name) {
        return operators.remove(name);
    }

    public Operator deleteOperator(Operator operator) {
        return operators.remove(operator.getName());
    }

    public Operator updateOperator(String name, Operator operator) {
        return operators.put(name, operator);
    }

}
