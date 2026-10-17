gitpackage restaurant.model;

import restaurant.exception.InvalidOrderException;
import restaurant.exception.ItemNotFoundException;
import restaurant.exception.OutOfStockException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** An open order for a single table. */
public class TableOrder {
    private final int tableNumber;
    private final List<OrderItem> items = new ArrayList<>();
    private boolean closed = false;

    public TableOrder(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public OrderItem addItem(MenuItem menuItem, int quantity)
            throws OutOfStockException, InvalidOrderException {
        if (closed) throw new InvalidOrderException("Table " + tableNumber + " order is already closed.");
        if (quantity <= 0) throw new InvalidOrderException("Quantity must be at least 1.");
        menuItem.reduceStock(quantity);
        OrderItem line = new OrderItem(tableNumber, menuItem, quantity);
        items.add(line);
        return line;
    }

    /** Removes a line and returns its stock. Only allowed while the line is still NEW. */
    public void removeItem(int orderItemId) throws ItemNotFoundException, InvalidOrderException {
        if (closed) throw new InvalidOrderException("Table " + tableNumber + " order is already closed.");
        OrderItem line = findItem(orderItemId);
        if (line.getStatus() != OrderStatus.NEW) {
            throw new InvalidOrderException(
                    "Cannot remove '" + line.getMenuItem().getName() + "': already " + line.getStatus() + ".");
        }
        line.getMenuItem().restoreStock(line.getQuantity());
        items.remove(line);
    }

    public OrderItem findItem(int orderItemId) throws ItemNotFoundException {
        for (OrderItem i : items) {
            if (i.getId() == orderItemId) return i;
        }
        throw new ItemNotFoundException("No order line #" + orderItemId + " on table " + tableNumber);
    }

    public double getSubtotal() {
        double sum = 0;
        for (OrderItem i : items) sum += i.getLineTotal();
        return sum;
    }

    public boolean allServed() {
        for (OrderItem i : items) {
            if (i.getStatus() != OrderStatus.SERVED) return false;
        }
        return true;
    }

    public int getTableNumber() { return tableNumber; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public boolean isClosed() { return closed; }
    public void close() { this.closed = true; }
}
