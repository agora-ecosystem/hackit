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

package de.tuberlin.dima.hackit.api.wayang.function;

import de.tuberlin.dima.hackit.core.sniffer.HackitSniffer;
import de.tuberlin.dima.hackit.core.sniffer.actor.Actor;
import de.tuberlin.dima.hackit.core.sniffer.actor.EmptyActor;
import de.tuberlin.dima.hackit.core.sniffer.clone.BasicCloner;
import de.tuberlin.dima.hackit.core.sniffer.inject.EmptyInjector;
import de.tuberlin.dima.hackit.core.sniffer.inject.Injector;
import de.tuberlin.dima.hackit.core.sniffer.sniff.CollectionTagsToSniff;
import de.tuberlin.dima.hackit.core.tuple.HackitTuple;
import de.tuberlin.dima.hackit.shipper.kafka.HackitShipperKafka;
import de.tuberlin.dima.hackit.shipper.kafka.receiver.ReceiverKafka;
import de.tuberlin.dima.hackit.shipper.kafka.sender.SenderKafka;

import java.util.Properties;

public class WayangSniffer<K, T> extends HackitSniffer<K, T, byte[], SenderKafka<K, byte[]>, ReceiverKafka<K, HackitTuple<K, T>>> {

    public WayangSniffer(Properties conf) {
        super(
                (Injector<HackitTuple<K, T>>) new EmptyInjector<HackitTuple<K, T>>(),
                (Actor<HackitTuple<K, T>>) new EmptyActor<K, T>(),
                new HackitShipperKafka<K, HackitTuple<K,T>, byte[]>(conf),
                new CollectionTagsToSniff<>(),
                new BasicCloner<>()
        );
    }
}
