package com.dbank.tradestore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.dbank.tradestore.dto.TradeMessage;

@Service
public class ProducerService {

    private final KafkaTemplate<String, TradeMessage> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;

    public ProducerService(KafkaTemplate<String, TradeMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(TradeMessage message) {
        kafkaTemplate.send(topic, message);
    }
}