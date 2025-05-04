package com.dbank.tradestore.repository.nosql;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.dbank.tradestore.model.Trade;

@Repository
public interface TradeMongoRepository extends MongoRepository<Trade, String> {
}