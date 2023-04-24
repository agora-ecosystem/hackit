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

package de.tuberlin.dima.hackit.shipper.kafka.receiver;

import de.tuberlin.dima.hackit.core.sniffer.shipper.PSProtocol;
import de.tuberlin.dima.hackit.core.sniffer.shipper.receiver.Receiver;
import de.tuberlin.dima.hackit.core.tuple.HackitTuple;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ReceiverKafka<K, T>
        implements Receiver<T>, PSProtocol {

    //TODO Get from configuration
    static Map<String, String> KAFKA_MAPPING;
    static {
        KAFKA_MAPPING = new HashMap<>();
        KAFKA_MAPPING.put("127.0.0.1", "127.0.0.1");
    }
    static Integer numPartitions = 1;
    static Short replicationFactor = 1;

    transient Consumer<K, T> consumer;
    Properties config;
    List<String> topics;

    public ReceiverKafka(Properties config){
        this.config = config;
        this.topics = new ArrayList<>();
    }

    public ReceiverKafka(Properties config, String... topic){
        this.config = config;
        this.topics = new ArrayList<>(topic.length);
        for(int i = 0; i < topic.length; i++){
            this.topics.add(topic[i]);
        }
    }


    @Override
    public PSProtocol addTopic(String... topic) {
        for(int i = 0; i < topic.length; i++){
            this.topics.add(topic[i]);
        }
//        this.topics.addAll(Arrays.asList(topic));
        this.consumer.subscribe(this.topics);
        return this;
    }

    @Override
    public PSProtocol addExchange(String exchange) {
        return this;
    }

    @Override
    public void init() {
        this.consumer = new KafkaConsumer<>(config);
    }

    @Override
    public Iterator<T> getElements() {
        if(this.consumer.subscription().size() == 0){
            System.out.println(this.topics);
            this.consumer.subscribe(this.topics);
        }
        final int giveUp = 100;
        int noRecordsCount = 0;
        ConsumerRecords<K, T> consumerRecords;
        Duration timeout = Duration.ofMillis(10);
        while (true) {
            consumerRecords = consumer.poll(timeout);

            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            List<T> list = new ArrayList<>();
            consumerRecords.forEach(record ->{
//                HackitTuple<K, T> result = new HackitTuple<>(
//                        record.value()
//                );
                // System.out.println("received " + record.value());
                list.add(record.value());
            });

            consumer.commitAsync();

            return list.iterator();
        }
        return null;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
