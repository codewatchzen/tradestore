package com.dbank.tradestore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "trades")
public class Trade {

    private String tradeId;
    private int version;
    private String counterPartyId;
    private String bookId;
    private LocalDate maturityDate;
    private LocalDate createdDate;
    private String expired;

    //Getters and Setters
    /*
     * TradeId
     */
    public String getTradeId() {
        return tradeId;
    } 
    
    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    /*
     * Version
     */
    public int getVersion() {
        return version;
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
    
    /*
     * CounterPartyId
     */
    public String getCounterPartyId() {
        return counterPartyId;
    }
    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    /*
     * BookId
     */
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    /*
     * MaturityDate
     */
    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    /*
     * CreatedDate
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    /*
     * Expired (Y/N)
     */
    public String getExpired() {
        return expired;
    }
    public void setExpired(String expired) {
        this.expired = expired;
    }

    
}
