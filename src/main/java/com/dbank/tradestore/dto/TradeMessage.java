package com.dbank.tradestore.dto;

public class TradeMessage {
    private String tradeId;
    private int version;

    public TradeMessage() {}

    public TradeMessage(String tradeId, int version) {
        this.tradeId = tradeId;
        this.version = version;
    }

    public String getTradeId() { return tradeId; }
    public void setTradeId(String tradeId) { this.tradeId = tradeId; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    @Override
    public String toString() {
        return "TradeMessage{" + "tradeId='" + tradeId + '\'' + ", version=" + version + '}';
    }
}