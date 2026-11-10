package com.example.pricing;

/** Computes prices in cents. The service keeps no state and calls no other system. */
public final class PricingService {
    private static final int PERCENT = 100;
    private static final long FREE_SHIPPING_THRESHOLD_CENTS = 5000;
    private static final long FLAT_SHIPPING_CENTS = 499;

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

    /**
     * Returns the shipping cost for an order.
     *
     * @param orderTotalCents the order total in cents after the tier discount, not negative
     * @return 0 when the total is 5000 cents or more, otherwise 499 cents
     */
    public long shippingCents(final long orderTotalCents) {
        if (orderTotalCents < 0) {
            throw new IllegalArgumentException("The total must not be negative");
        }
        if (orderTotalCents >= FREE_SHIPPING_THRESHOLD_CENTS) {
            return 0;
        }
        return FLAT_SHIPPING_CENTS;
    }
}
