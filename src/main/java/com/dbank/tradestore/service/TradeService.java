package com.dbank.tradestore.service;

import com.dbank.tradestore.repository.TradeMongoRepository;
import com.dbank.tradestore.repository.TradeSqlRepository;
import com.dbank.tradestore.exception.TradeException;
import com.dbank.tradestore.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    @Autowired
    private TradeSqlRepository tradeSqlRepository;
    @Autowired
    private TradeMongoRepository tradeMongoRepository;

    public TradeService(TradeSqlRepository tradeSqlRepository, TradeMongoRepository tradeRepository) {
        this.tradeSqlRepository = tradeSqlRepository; 
        this.tradeMongoRepository = tradeRepository;
    }

    public void saveTrade(Trade trade) {

        validateTrade(trade);

        trade.setCreatedDate(LocalDate.now());
        trade.setExpired("N"); // Set expired to "N" by default

        // Save to SQL database
        tradeSqlRepository.save(trade);
        
        // Save to MongoDB
        tradeMongoRepository.save(trade);
    }

    private void  validateTrade(Trade trade) {

        //validate if maturity date is in the past
        if(trade.getMaturityDate().isBefore(LocalDate.now())) {
            throw new TradeException(trade.getTradeId(), "Maturity Date cannot be in the past");
        }

        // Check if version is lower than existing trade version
        Trade existing = tradeSqlRepository.findByTradeId(trade.getTradeId());
        if (existing != null) {
            if (trade.getVersion() < existing.getVersion()) {
                throw new TradeException("Lower version trade cannot be accepted");
            }
        }
    }



    public void markExpiredTrades() {
        tradeSqlRepository.findAll().forEach(trade -> {
            if (trade.getMaturityDate().isBefore(LocalDate.now())) {
                trade.setExpired("Y");
                tradeSqlRepository.save(trade);
            }
        });

        tradeMongoRepository.findAll().forEach(trade -> {
            if (trade.getMaturityDate().isBefore(LocalDate.now())) {
                trade.setExpired("Y");
                tradeMongoRepository.save(trade);
            }
        });
    }
    
}
