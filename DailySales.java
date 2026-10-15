package restaurant.service;

import restaurant.billing.Bill;
import restaurant.model.OrderItem;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/** Keeps running totals for the day and writes them to a text file. */
public class DailySales {
    private int billsPaid = 0;
    private double revenue = 0;
    private double taxCollected = 0;
    private double discountsGiven = 0;
    private final Map<String, Integer> unitsSold = new TreeMap<>();

    public void record(Bill bill) {
        billsPaid++;
        revenue += bill.getTotal();
        taxCollected += bill.getTax();
        discountsGiven += bill.getDiscountAmount();
        for (OrderItem line : bill.getOrder().getItems()) {
            unitsSold.merge(line.getMenuItem().getName(), line.getQuantity(), Integer::sum);
        }
    }

    public void printSummary() {
        System.out.println("\nBills paid: " + billsPaid);
        System.out.printf("Revenue: KES %.2f%n", revenue);
    }

    /** Writes the summary file; try-with-resources closes the file automatically. */
    public String writeToFile() throws IOException {
        String fileName = "daily_sales_" + LocalDate.now() + ".txt";
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println("DAILY SALES SUMMARY - " + LocalDate.now());
            out.println("====================================");
            out.println("Bills paid:       " + billsPaid);
            out.printf("Total revenue:    KES %.2f%n", revenue);
            out.printf("Tax collected:    KES %.2f%n", taxCollected);
            out.printf("Discounts given:  KES %.2f%n", discountsGiven);
            out.println("\nUnits sold:");
            for (Map.Entry<String, Integer> e : unitsSold.entrySet()) {
                out.println("  " + e.getKey() + ": " + e.getValue());
            }
        }
        return fileName;
    }
}
