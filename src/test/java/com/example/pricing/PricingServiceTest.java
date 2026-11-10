package com.example.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PricingServiceTest {
    private final PricingService service = new PricingService();

    @Test
    void standardCustomerGetsNoDiscount() {
        assertEquals(0, service.tierDiscountPercent(CustomerTier.STANDARD));
        assertEquals(2000, service.totalCents(1000, 2, CustomerTier.STANDARD));
    }

    @Test
    void silverAndGoldCustomersGetADiscount() {
        assertEquals(5, service.tierDiscountPercent(CustomerTier.SILVER));
        assertEquals(10, service.tierDiscountPercent(CustomerTier.GOLD));
        assertEquals(1900, service.totalCents(1000, 2, CustomerTier.SILVER));
    }

    @Test
    void rejectsAQuantityBelowOne() {
        assertThrows(IllegalArgumentException.class, () -> service.totalCents(1000, 0, CustomerTier.GOLD));
    }
}
