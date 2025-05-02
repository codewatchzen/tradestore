package com.dbank.tradestore.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbank.tradestore.dto.TradeMessage;
import com.dbank.tradestore.service.ProducerService;

@RestController
@RequestMapping("/trade")
public class TradeController {

    private final ProducerService producerService;

    public TradeController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody TradeMessage message) {
        producerService.send(message);
        return "Message sent";
    }
}

