package restaurant.billing;

/** 5% off, plus a further 1% for every 100 loyalty points, capped at 15%. */
public class LoyaltyDiscount implements Discount {
    private final int points;

    public LoyaltyDiscount(int points) {
        this.points = Math.max(0, points);
    }

    private double rate() {
        return Math.min(0.15, 0.05 + (points / 100) * 0.01);
    }

    @Override
    public String getName() { return String.format("Loyalty (%.0f%%)", rate() * 100); }

    @Override
    public double calculate(double subtotal) { return subtotal * rate(); }
}
