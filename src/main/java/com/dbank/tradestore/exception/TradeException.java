package com.dbank.tradestore.exception;

public class TradeException extends RuntimeException {
    public TradeException(String message) {
        super(message);
    }

    public TradeException(String tradeId, String reason) {
        super("Invalid Trade Id: " + tradeId + " due to reason: " + reason);
    }
}