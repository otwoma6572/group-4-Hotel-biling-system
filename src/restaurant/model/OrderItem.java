package restaurant.model;

/** One line on a table order: a menu item, a quantity and a kitchen status. */
public class OrderItem {
    private static int counter = 1;

    private final int id;
    private final int tableNumber;
    private final MenuItem menuItem;
    private final int quantity;
    private OrderStatus status = OrderStatus.NEW;

    public OrderItem(int tableNumber, MenuItem menuItem, int quantity) {
        this.id = counter++;
        this.tableNumber = tableNumber;
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public double getLineTotal() { return menuItem.getPrice() * quantity; }

    public int getId() { return id; }
    public int getTableNumber() { return tableNumber; }
    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("#%d Table %d: %d x %s [%s]",
                id, tableNumber, quantity, menuItem.getName(), status);
    }
}
