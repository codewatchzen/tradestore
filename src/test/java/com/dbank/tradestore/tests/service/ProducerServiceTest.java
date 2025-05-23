package com.dbank.tradestore.tests.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.dbank.tradestore.dto.TradeMessage;
import com.dbank.tradestore.service.ProducerService;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @Mock
    private KafkaTemplate<String, TradeMessage> kafkaTemplate;

    @InjectMocks
    private ProducerService producerService;

    @Disabled("KafkaTemplate cannot be mocked on Java 24 with current setup")
    @Test
    void testSend() {
        TradeMessage msg = new TradeMessage("T1", 1);
        producerService.send(msg);
        verify(kafkaTemplate, times(1)).send(anyString(), eq(msg));
    }
}