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

package de.tuberlin.dima.hackit.shipper.kafka;

import de.tuberlin.dima.hackit.core.sniffer.shipper.Shipper;
import de.tuberlin.dima.hackit.core.tuple.HackitTuple;
import de.tuberlin.dima.hackit.shipper.kafka.receiver.ReceiverKafka;
import de.tuberlin.dima.hackit.shipper.kafka.sender.SenderKafka;
import de.tuberlin.dima.hackit.shipper.kafka.transformation.KeyDeserializer;
import de.tuberlin.dima.hackit.shipper.kafka.transformation.KeySerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Iterator;
import java.util.Properties;

public class HackitShipperKafka
<
    K,
    T_IN,
    T_OUT
>
extends
    Shipper<
        T_IN,
        T_OUT,
        SenderKafka<K, T_OUT>,
        ReceiverKafka<K, T_IN>
    >
{

    Properties conf;
    Iterator<T_IN> current;

    public HackitShipperKafka() {
        this(new Properties());
    }

    public HackitShipperKafka(Properties conf) {
        conf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KeySerializer.class);
        conf.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KeyDeserializer.class);
        conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KeySerializer.class);
        conf.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KeyDeserializer.class);
        conf.put(ConsumerConfig.GROUP_ID_CONFIG, "hackit-executor");
        this.conf = conf;
        this.subscribeAsProducer("hackit-default");
        this.subscribeAsConsumer("hackit-default");
    }

    @Override
    protected SenderKafka<K, T_OUT> createSenderInstance() {
        return new SenderKafka<>(this.conf);
    }

    @Override
    protected ReceiverKafka<K, T_IN> createReceiverInstance() {
        return new ReceiverKafka<>(this.conf, "hackit-default");
    }

}
