package com.dbank.tradestore.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dbank.tradestore.service.TradeService;

@Component
public class ExpiryJobScheduler {

    @Autowired
    private TradeService tradeService;

    @Scheduled(cron = "0 0 0 * * ?") // every day at midnight
    public void markExpired() {
        tradeService.markExpiredTrades();
    }
}