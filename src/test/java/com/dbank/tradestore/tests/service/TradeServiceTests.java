package com.dbank.tradestore.tests.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dbank.tradestore.exception.TradeException;
import com.dbank.tradestore.model.Trade;
import com.dbank.tradestore.repository.nosql.TradeMongoRepository;
import com.dbank.tradestore.repository.sql.TradeSqlRepository;
import com.dbank.tradestore.service.TradeService;

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

    private TradeService tradeService;
    private TradeSqlRepository tradeSqlRepository;
    private TradeMongoRepository tradeMongoRepository;

    @BeforeEach
    public void setUp() {
        tradeSqlRepository = mock(TradeSqlRepository.class);
        tradeMongoRepository = mock(TradeMongoRepository.class);
        tradeService = new TradeService(tradeSqlRepository, tradeMongoRepository);
    }

    // Helper method to create a Trade object
    private Trade createTrade(String tradeId, int version, String counterPartyId, 
                                        String bookId, LocalDate maturityDate, LocalDate createdDate) {
        Trade trade = new Trade();
        trade.setTradeId(tradeId);
        trade.setVersion(version);
        trade.setCounterPartyId(counterPartyId);
        trade.setBookId(bookId);
        trade.setMaturityDate(maturityDate);
        trade.setCreatedDate(createdDate);
        trade.setExpired("N");
        return trade;
}
    
     // Test case to reject trades with lower version and generate an exception
    @Test
    public void testRejectLowerVersionTrade() {
        // Mock the existing trade in the repository
        Trade existingTrade = createTrade("T1", 2, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now());
        when(tradeSqlRepository.findByTradeId("T1")).thenReturn(existingTrade);

        // Create a new trade with a lower version
        Trade lowerVersionTrade = createTrade("T1", 1, "CP-1", "B2", LocalDate.now().plusDays(10), LocalDate.now());

        // Expect a TradeException to be thrown
        TradeException exception = assertThrows(TradeException.class, () -> {
            tradeService.saveTrade(lowerVersionTrade);
        });

        assertEquals("Lower version trade cannot be accepted", exception.getMessage());
        verify(tradeSqlRepository, never()).save(lowerVersionTrade);
        
    }

    // Test case to replace trades with same version with current record
    @Test
    public void testReplaceSameVersionTrade() {
        // Mock the existing trade in the repository
        Trade existingTrade = createTrade("T1", 1, "CP-1", "B1", LocalDate.now().plusDays(10), LocalDate.now());
        when(tradeSqlRepository.findByTradeId("T1")).thenReturn(existingTrade);

        // Create a new trade with the same version but different BookId and maturity date
        Trade updatedTrade = createTrade("T1", 1, "CP-1", "B2", LocalDate.now().plusDays(20), LocalDate.now());

        // Save the updated trade
        tradeService.saveTrade(updatedTrade);

        // Verify the existing trade is updated and saved
        verify(tradeSqlRepository).save(existingTrade); // Ensure the existing trade is updated
        verify(tradeMongoRepository).save(existingTrade); // Ensure MongoDB is updated

        // Verify that the fields of the existing trade are updated
        assertEquals("B2", existingTrade.getBookId());
        assertEquals(LocalDate.now().plusDays(20), existingTrade.getMaturityDate());
        
        assertEquals("N", existingTrade.getExpired()); // Ensure expired status is reset
    }

    // Test case to reject trades with past maturity dates
    @Test
    public void testRejectPastMaturityDateTrade() {
        // Create a trade with a past maturity date
        Trade trade = createTrade("T1", 1, "CP-1", "B1", LocalDate.now().minusDays(10), LocalDate.now());
        // Attempt to add the trade to the TradeStore and expect an exception 
        TradeException exception = assertThrows(TradeException.class, () -> {
            tradeService.saveTrade(trade);
        });

        assertEquals("Invalid Trade Id: T1 due to reason: Maturity Date cannot be in the past", exception.getMessage());
        verify(tradeSqlRepository, never()).save(trade);
        verify(tradeMongoRepository, never()).save(trade);
    }

    // Test case to automatically expire trades with maturity dates in the past (cron/scheduled job)
    @Test
    public void testExpirePastMaturityDateTrade() {
        // Create a trade with a past maturity date
        Trade trade1 = createTrade("T1", 1, "CP-1", "B1", LocalDate.now().minusDays(10), LocalDate.now());
        Trade trade2 = createTrade("T2", 1, "CP-2", "B2", LocalDate.now().plusDays(10), LocalDate.now());

        List<Trade> trades = new ArrayList<>();
        trades.add(trade1);
        trades.add(trade2);

        when(tradeSqlRepository.findAll()).thenReturn(trades);

        tradeService.markExpiredTrades();

        assertEquals("Y", trade1.getExpired());
        assertEquals("N", trade2.getExpired());
        verify(tradeSqlRepository).save(trade1);
        verify(tradeSqlRepository, never()).save(trade2);
    }


}
