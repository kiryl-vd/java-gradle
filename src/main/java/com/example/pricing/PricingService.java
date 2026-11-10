package com.example.pricing;

/** Computes prices in cents. The service keeps no state and calls no other system. */
public final class PricingService {
    private static final int PERCENT = 100;

    /**
     * Returns the discount in percent for a customer tier.
     *
     * @param tier the loyalty level of the customer
     * @return the discount, from 0 to 100
     */
    public int tierDiscountPercent(final CustomerTier tier) {
        return switch (tier) {
            case STANDARD -> 0;
            case SILVER -> 5;
            case GOLD -> 10;
        };
    }

    /**
     * Returns the price of an order after the discount of the tier.
     *
     * @param unitPriceCents the price of one item in cents, not negative
     * @param quantity the number of items, at least 1
     * @param tier the loyalty level of the customer
     * @return the total in cents, rounded down
     */
    public long totalCents(final long unitPriceCents, final int quantity, final CustomerTier tier) {
        if (unitPriceCents < 0) {
            throw new IllegalArgumentException("The price must not be negative");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("The quantity must be at least 1");
        }
        final long gross = unitPriceCents * quantity;
        return gross * (PERCENT - tierDiscountPercent(tier)) / PERCENT;
    }
}
