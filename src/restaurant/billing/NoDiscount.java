package restaurant.billing;

public class NoDiscount implements Discount {
    @Override
    public String getName() { return "No discount"; }

    @Override
    public double calculate(double subtotal) { return 0; }
}
