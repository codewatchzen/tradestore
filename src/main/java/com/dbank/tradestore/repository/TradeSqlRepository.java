package com.dbank.tradestore.repository;

import com.dbank.tradestore.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeSqlRepository extends JpaRepository<Trade, String> {
    Trade findByTradeId(String tradeId);
}
