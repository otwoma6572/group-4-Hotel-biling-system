package restaurant.billing;

import restaurant.model.OrderItem;
import restaurant.model.TableOrder;

/** Computes and prints the bill for a table order. */
public class Bill {
    public static final double DEFAULT_TAX_RATE = 0.16; // Kenya VAT

    private final TableOrder order;
    private final double taxRate;
    private final Discount discount;
    private final double tipRate;

    private final double subtotal;
    private final double discountAmount;
    private final double tax;
    private final double tip;
    private final double total;

    public Bill(TableOrder order, double taxRate, Discount discount, double tipRate) {
        this.order = order;
        this.taxRate = taxRate;
        this.discount = discount == null ? new NoDiscount() : discount;
        this.tipRate = tipRate;

        this.subtotal = round(order.getSubtotal());
        this.discountAmount = round(Math.min(this.discount.calculate(subtotal), subtotal));
        double taxable = subtotal - discountAmount;
        this.tax = round(taxable * taxRate);
        this.tip = round(taxable * tipRate);
        this.total = round(taxable + tax + tip);
    }

    public Bill(TableOrder order, Discount discount) {
        this(order, DEFAULT_TAX_RATE, discount, 0);
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    public double splitShare(int people) {
        if (people < 1) throw new IllegalArgumentException("People must be at least 1");
        return round(total / people);
    }

    public void print() {
        System.out.println("\n=============== BILL ===============");
        System.out.println("Table: " + order.getTableNumber());
        System.out.println("------------------------------------");
        for (OrderItem i : order.getItems()) {
            System.out.printf("%-16s %2d x %8.2f = %9.2f%n",
                    i.getMenuItem().getName(), i.getQuantity(), i.getMenuItem().getPrice(), i.getLineTotal());
        }
        System.out.println("------------------------------------");
        System.out.printf("%-26s KES %9.2f%n", "Subtotal", subtotal);
        System.out.printf("%-26s KES %9.2f%n", "Discount: " + discount.getName(), -discountAmount);
        System.out.printf("%-26s KES %9.2f%n", String.format("Tax (%.0f%%)", taxRate * 100), tax);
        if (tipRate > 0) {
            System.out.printf("%-26s KES %9.2f%n", String.format("Tip (%.0f%%)", tipRate * 100), tip);
        }
        System.out.println("------------------------------------");
        System.out.printf("%-26s KES %9.2f%n", "TOTAL", total);
        System.out.println("====================================");
    }

    public TableOrder getOrder() { return order; }
    public double getSubtotal() { return subtotal; }
    public double getDiscountAmount() { return discountAmount; }
    public double getTax() { return tax; }
    public double getTip() { return tip; }
    public double getTotal() { return total; }
}
