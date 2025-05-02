package com.dbank.tradestore.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.dbank.tradestore.dto.TradeMessage;

@Component
public class TradeListener {

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "trade-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(TradeMessage message) {
        System.out.println("Received: " + message);
    }
}