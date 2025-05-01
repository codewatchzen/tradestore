package com.dbank.tradestore.repository;

import com.dbank.tradestore.model.Trade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeMongoRepository extends MongoRepository<Trade, String> {
}