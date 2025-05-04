package com.dbank.tradestore.repository.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dbank.tradestore.model.Trade;

@Repository
public interface TradeSqlRepository extends JpaRepository<Trade, Long> {
    Trade findByTradeId(String tradeId);
}
