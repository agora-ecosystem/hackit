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
package de.tuberlin.dima.hackit.core.sniffer.inject;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import org.apache.wayang.plugin.hackit.core.iterator.ElementPlusIterator;

/**
 * EmptyInjector is a dummy implementation of the {@link Injector} to show how it need to looks and Inyector implementation
 *
 * @param <T> type of the element processed.
 */
public class EmptyInjector<T> implements Injector<T>{

    @Override
    public Iterator<T> inject(T element, Iterator<T> iterator) {
        return
            StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                    new ElementPlusIterator<T>(
                        element,
                        iterator
                    ),
                    Spliterator.ORDERED
                ),
                false
            ).map(
                (T _element) -> {
                    if (this.is_halt_job(_element)) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return _element;
                }
            ).filter(
                (T _element) -> ! this.is_skip_element(_element)
            ).iterator();
    }

    @Override
    public boolean is_skip_element(T element) {
        return false;
    }

    @Override
    public boolean is_halt_job(T element) {
        return false;
    }
}