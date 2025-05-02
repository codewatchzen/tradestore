package com.dbank.tradestore.service;

import com.dbank.tradestore.repository.TradeMongoRepository;
import com.dbank.tradestore.repository.TradeSqlRepository;
import com.dbank.tradestore.exception.TradeException;
import com.dbank.tradestore.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void saveTrade(Trade trade) {

        validateTrade(trade);

        // Check if an existing trade with the same TradeId exists
    Trade existingTrade = tradeSqlRepository.findByTradeId(trade.getTradeId());
    if (existingTrade != null) {
        // Update the existing trade's fields
        existingTrade.setVersion(trade.getVersion());
        existingTrade.setCounterPartyId(trade.getCounterPartyId());
        existingTrade.setBookId(trade.getBookId());
        existingTrade.setMaturityDate(trade.getMaturityDate());
        existingTrade.setCreatedDate(LocalDate.now());
        existingTrade.setExpired("N"); // Reset expired status

        // Save the updated trade to SQL and MongoDB
        tradeSqlRepository.save(existingTrade);
        tradeMongoRepository.save(existingTrade);
    } else {
        // If no existing trade, save the new trade
        trade.setCreatedDate(LocalDate.now());
        trade.setExpired("N"); // Set expired to "N" by default
        tradeSqlRepository.save(trade);
        tradeMongoRepository.save(trade);
    }

        // trade.setCreatedDate(LocalDate.now());
        // trade.setExpired("N"); // Set expired to "N" by default

        // // Save to SQL database
        // tradeSqlRepository.save(trade);
        
        // // Save to MongoDB
        // tradeMongoRepository.save(trade);
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
