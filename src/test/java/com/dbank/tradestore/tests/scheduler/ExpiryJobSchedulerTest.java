package com.dbank.tradestore.tests.scheduler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.dbank.tradestore.scheduler.ExpiryJobScheduler;
import com.dbank.tradestore.service.TradeService;


class ExpiryJobSchedulerTest {

    @Disabled("Unable to mock objects on Java 24 with current setup")   
    @Test
    void testMarkExpiredCallsService() {
        // Mock the dependency
        TradeService mockTradeService = Mockito.spy(TradeService.class);

        // Inject into class manually
        ExpiryJobScheduler scheduler = new ExpiryJobScheduler();
        // Manually set the mock (use reflection because it's @Autowired in the real class)
        setField(scheduler, "tradeService", mockTradeService);

        // Call method
        scheduler.markExpired();

        // Verify behavior
        Mockito.verify(mockTradeService, Mockito.times(1)).markExpiredTrades();
    }

    // Utility to inject private field (reflection-based)
    private static void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}