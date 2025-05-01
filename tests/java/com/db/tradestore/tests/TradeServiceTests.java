package com.db.tradestore.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Test Class for TradeService 
public class TradeServiceTests {
    
    /*
     * Test Cases:
     * 1. Reject trades with lower version and generate an exception
     * 2. Replace trades with same version with curent record
     * 3. Reject trades with past maturity dates(< current date)
     * 4. Automatically expire trades with maturity dates in the past (cron/scheduled job)
     * 
     */

    public TradeServiceTests() {
        // Initialize the TradeService and any required dependencies here

    }
    
     // Test case to reject trades with lower version and generate an exception
     @Test
    public void testRejectLowerVersionTrade() {
        // Create a trade with a specific version
        //Trade --> TradeId, Version, CounterPartyId, BookId, MaturityDate, CreatedDate
        Trade trade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now());
        // Add the trade to the TradeStore
        tradeStore.addTrade(trade);
        // Create a new trade with a lower version
        Trade lowerVersionTrade = new Trade("T1", "CP-1", "B2", 0, 2000, LocalDate.now().plusDays(10), 0);
        //  add the lower version trade to the TradeStore and expect an exception
        assertThrows(TradeVersionException.class, () -> {
            tradeStore.addTrade(lowerVersionTrade);
        }, "Expected TradeVersionException to be thrown for lower version trade");
    }

    // Test case to replace trades with same version with current record
    @Test
    public void testReplaceSameVersionTrade() {
        // Create a trade with a specific version
        //Trade --> TradeId, Version, CounterPartyId, BookId, MaturityDate, CreatedDate
        Trade trade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now());
        // Add the trade to the TradeStore
        tradeStore.addTrade(trade);
        // Create a new trade with the same version but different BookId
        Trade updatedTrade = new Trade("T1", 1, "CP-1", "B2",LocalDate.now().plusDays(10), LocalDate.now());
        // Attempt to add the updated trade to the TradeStore
        tradeStore.addTrade(updatedTrade);
        // Verify that the updated trade has replaced the original trade in the TradeStore
        assertEquals(tradeStore.getTrade("T1").getVersion(), 1, "Trade should be replaced with the updated version");
        assertEquals(tradeStore.getTrade("T1").getCounterParty(), "B2", "Counterparty should be updated in the TradeStore");
    }

    // Test case to reject trades with past maturity dates
    @Test
    public void testRejectPastMaturityDateTrade() {
        // Create a trade with a past maturity date
        Trade trade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().minusDays(10), LocalDate.now());
        // Attempt to add the trade to the TradeStore and expect an exception 
        assertThrows(TradeMaturityDateException.class, () -> {
            tradeStore.addTrade(trade);
        }, "Expected TradeMaturityDateException to be thrown for past maturity date trade");
    }

    // Test case to automatically expire trades with maturity dates in the past (cron/scheduled job)
    @Test
    public void testExpirePastMaturityDateTrade() {
        // Create a trade with a past maturity date
        Trade trade = new Trade("T1", 1, "CP-1", "B1", LocalDate.now().minusDays(10), LocalDate.now());
        // Add the trade to the TradeStore
        tradeStore.addTrade(trade);
        // Simulate the expiration process (run a scheduled job)
        tradeStore.expireTrades();
        // Verify that the trade has been expired and removed from the TradeStore
        assertNull(tradeStore.getTrade("T1"), "Trade should be expired and removed from the TradeStore");
    }


}
