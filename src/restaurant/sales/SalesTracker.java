package restaurant.sales;

import restaurant.billing.Bill;
import restaurant.model.OrderItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Tracks paid bills for the day and writes an end-of-day summary file. */
public class SalesTracker {
    private final List<Bill> bills = new ArrayList<>();

    public void record(Bill bill) { bills.add(bill); }

    public int billCount() { return bills.size(); }

    public double totalSales() {
        double sum = 0;
        for (Bill b : bills) sum += b.getTotal();
        return Math.round(sum * 100.0) / 100.0;
    }

    public String buildSummary() {
        double subtotal = 0, discounts = 0, tax = 0, tips = 0;
        Map<String, Integer> itemCounts = new TreeMap<>();
        for (Bill b : bills) {
            subtotal += b.getSubtotal();
            discounts += b.getDiscountAmount();
            tax += b.getTax();
            tips += b.getTip();
            for (OrderItem i : b.getOrder().getItems()) {
                itemCounts.merge(i.getMenuItem().getName(), i.getQuantity(), Integer::sum);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("DAILY SALES SUMMARY - ").append(LocalDate.now()).append(System.lineSeparator());
        sb.append("====================================").append(System.lineSeparator());
        sb.append(String.format("Bills paid      : %d%n", bills.size()));
        sb.append(String.format("Gross subtotal  : KES %.2f%n", subtotal));
        sb.append(String.format("Discounts given : KES %.2f%n", discounts));
        sb.append(String.format("Tax collected   : KES %.2f%n", tax));
        sb.append(String.format("Tips            : KES %.2f%n", tips));
        sb.append(String.format("TOTAL SALES     : KES %.2f%n", totalSales()));
        sb.append("------------------------------------").append(System.lineSeparator());
        sb.append("Items sold:").append(System.lineSeparator());
        if (itemCounts.isEmpty()) sb.append("  (none)").append(System.lineSeparator());
        for (Map.Entry<String, Integer> e : itemCounts.entrySet()) {
            sb.append(String.format("  %-22s x %d%n", e.getKey(), e.getValue()));
        }
        return sb.toString();
    }

    /** Writes the summary to daily_sales_YYYY-MM-DD.txt and returns the path. */
    public Path writeSummary() throws IOException {
        Path file = Paths.get("daily_sales_" + LocalDate.now() + ".txt");
        Files.writeString(file, buildSummary());
        return file;
    }
}
