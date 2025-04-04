package com.kafka.template.services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

@Service
public class KafkaHistoryService {

    public List<String> getAllMessagesFromTopic(String topic) {
        List<String> messages = new ArrayList<>();

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        // Utiliser un group.id unique pour forcer la lecture depuis le début
        props.put("group.id", "temp-group-" + UUID.randomUUID());
        props.put("enable.auto.commit", "false");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of(topic));

        // Attendre que l'affectation des partitions soit faite
        long assignmentStart = System.currentTimeMillis();
        while (consumer.assignment().isEmpty() && System.currentTimeMillis() - assignmentStart < 5000) {
            consumer.poll(Duration.ofMillis(100));
        }
        // Une fois affecté, forcer la lecture depuis le début
        consumer.seekToBeginning(consumer.assignment());

        // Poll pendant 5 secondes pour accumuler l'historique complet
        long endTime = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < endTime) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
            for (ConsumerRecord<String, String> record : records) {
                messages.add(record.value());
            }
        }
        consumer.close();
        return messages;
    }
}
