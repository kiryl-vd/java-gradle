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

    @Test
    void freeShippingForALargeTotal() {
        assertEquals(0, service.shippingCents(12000));
    }

    @Test
    void freeShippingAtTheThreshold() {
        assertEquals(0, service.shippingCents(5000));
    }

    @Test
    void flatShippingForASmallTotal() {
        assertEquals(499, service.shippingCents(1000));
    }

    @Test
    void flatShippingJustBelowTheThreshold() {
        assertEquals(499, service.shippingCents(4999));
    }

    @Test
    void rejectsANegativeTotal() {
        final IllegalArgumentException error =
                assertThrows(IllegalArgumentException.class, () -> service.shippingCents(-1));
        assertEquals("The total must not be negative", error.getMessage());
    }
}
