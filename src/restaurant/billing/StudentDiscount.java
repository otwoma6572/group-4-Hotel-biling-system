package restaurant.billing;

/** 10% off for students. */
public class StudentDiscount implements Discount {
    private static final double RATE = 0.10;

    @Override
    public String getName() { return "Student (10%)"; }

    @Override
    public double calculate(double subtotal) { return subtotal * RATE; }
}
