package com.dbank.tradestore.service;

import java.time.LocalDate;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbank.tradestore.exception.TradeException;
import com.dbank.tradestore.model.Trade;
import com.dbank.tradestore.repository.nosql.TradeMongoRepository;
import com.dbank.tradestore.repository.sql.TradeSqlRepository;

@Service
public class TradeService {

    private final TradeSqlRepository tradeSqlRepository;
    private final TradeMongoRepository tradeMongoRepository;

    public TradeService(TradeSqlRepository tradeSqlRepository, TradeMongoRepository tradeMongoRepository) {
        this.tradeSqlRepository = tradeSqlRepository;
        this.tradeMongoRepository = tradeMongoRepository;
    }

    @Transactional
    public void saveTrade(Trade trade) {
        
        Trade existingTrade = tradeSqlRepository.findByTradeId(trade.getTradeId());

        validateTrade(trade, existingTrade);

        Trade tradeToSave = (existingTrade != null)
                ? updateExistingTrade(existingTrade, trade)
                : prepareNewTrade(trade);

        tradeSqlRepository.save(tradeToSave);
        tradeMongoRepository.save(tradeToSave);
    }

    private void validateTrade(Trade trade, Trade existingTrade) {
        if (trade.getMaturityDate().isBefore(LocalDate.now())) {
            throw new TradeException(trade.getTradeId(), "Maturity Date cannot be in the past");
        }
         
        if (trade.getVersion() < existingTrade.getVersion()) {
            throw new TradeException("Lower version trade cannot be accepted");
        }
    }

    private Trade updateExistingTrade(Trade existing, Trade incoming) {
        existing.setVersion(incoming.getVersion());
        existing.setCounterPartyId(incoming.getCounterPartyId());
        existing.setBookId(incoming.getBookId());
        existing.setMaturityDate(incoming.getMaturityDate());
        existing.setCreatedDate(LocalDate.now());
        existing.setExpired("N");
        return existing;
    }

    private Trade prepareNewTrade(Trade trade) {
        trade.setCreatedDate(LocalDate.now());
        trade.setExpired("N");
        return trade;
    }

    public void markExpiredTrades() {
        Consumer<Trade> markExpiredIfDue = trade -> {
            if (trade.getMaturityDate().isBefore(LocalDate.now())) {
                trade.setExpired("Y");
                // Save the trade in both repositories
                    tradeSqlRepository.save(trade);
                    tradeMongoRepository.save(trade);
            }
        };

        tradeSqlRepository.findAll().forEach(markExpiredIfDue);
        tradeMongoRepository.findAll().forEach(markExpiredIfDue);
    }
}
