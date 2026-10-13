package restaurant.billing;

/** Strategy interface for discount policies. */
public interface Discount {
    String getName();

    /** @return the discount AMOUNT (not the new total) for the given subtotal. */
    double calculate(double subtotal);
}
