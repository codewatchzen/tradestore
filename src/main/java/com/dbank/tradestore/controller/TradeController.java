package com.dbank.tradestore.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbank.tradestore.dto.TradeMessage;
import com.dbank.tradestore.model.Trade;
import com.dbank.tradestore.service.ProducerService;
import com.dbank.tradestore.service.TradeService;

@RestController
@RequestMapping("/trade")
public class TradeController {

    private final ProducerService producerService;
    private final TradeService tradeService; 


    public TradeController(ProducerService producerService, TradeService tradeService) {
         this.producerService = producerService;
         this.tradeService = tradeService;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody TradeMessage message) {
        producerService.send(message);
        return "Message sent";
    }

    // New method to save the trade entity in both SQL and MongoDB
    @PostMapping("/save")
    public String saveTrade(@RequestBody Trade trade) {
        try {
            // Call the tradeService to save the trade
            tradeService.saveTrade(trade);
            return "Trade saved successfully";
        } catch (Exception e) {
            // Log and handle error properly in production applications
            return "Error saving trade: " + e.getMessage();
        }
    }
}

