package com.kafka.template.controller;

import com.kafka.template.services.OrderNotificationProducer;

import com.kafka.template.services.KafkaHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotificationController {

    @Autowired
    private OrderNotificationProducer producer;

    @Autowired
    private KafkaHistoryService kafkaHistoryService;

    // Au chargement, récupérer l’historique des messages
    @GetMapping("/notifications")
    public String getNotifications(Model model,
            @RequestParam(name = "topic", required = false, defaultValue = "order_notifications") String topic) {
        model.addAttribute("titre", "Liste des messages consommés");
        // Vous pouvez récupérer l'historique du topic sélectionné
        model.addAttribute("messages", kafkaHistoryService.getAllMessagesFromTopic(topic));
        model.addAttribute("selectedTopic", topic);
        return "notifications";
    }

    @PostMapping("/send")
    public String sendNotification(@RequestParam("topic") String topic, @RequestParam("message") String message) {
        producer.sendNotification(topic, message);
        return "redirect:/notifications?topic=" + topic;
    }
}
