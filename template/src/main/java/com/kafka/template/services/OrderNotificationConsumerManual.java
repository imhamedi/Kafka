package com.kafka.template.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderNotificationConsumerManual {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = { "order_notifications",
            "order_updates" }, groupId = "manual-commit-group", containerFactory = "manualAckKafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String topic = record.topic();
        String message = record.value();
        System.out.println("Message reçu sur le topic " + topic + " : " + message);
        // On peut formater le message pour indiquer le topic d'origine
        messagingTemplate.convertAndSend("/topic/consumedMessages", "Topic: " + topic + " -> " + message);
        acknowledgment.acknowledge();
    }
}
