package restaurant.billing;

/** 20% off the whole bill during happy hour. */
public class HappyHourDiscount implements Discount {
    private static final double RATE = 0.20;

    @Override
    public String getName() { return "Happy Hour (20%)"; }

    @Override
    public double calculate(double subtotal) { return subtotal * RATE; }
}
