package restaurant.model;

import restaurant.exception.OutOfStockException;

/** Abstract base class for everything on the menu. */
public abstract class MenuItem {
    private final int id;
    private final String name;
    private final Category category;
    private double price;
    private int stock;
    private boolean available = true;

    protected MenuItem(int id, String name, Category category, double price, int stock) {
        if (price < 0 || stock < 0) {
            throw new IllegalArgumentException("Price and stock cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    /** Which kitchen station prepares this item. */
    public abstract StationType getStationType();

    /** Short label shown in the menu listing, e.g. FOOD or DRINK. */
    public abstract String getKind();

    public boolean isAvailable() {
        return available && stock > 0;
    }

    public void reduceStock(int qty) throws OutOfStockException {
        if (!available) {
            throw new OutOfStockException(name + " is currently unavailable.");
        }
        if (stock < qty) {
            throw new OutOfStockException(
                    "Only " + stock + " of " + name + " left (requested " + qty + ").");
        }
        stock -= qty;
    }

    public void restoreStock(int qty) {
        stock += qty;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public double getPrice() { return price; }
    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }
    public int getStock() { return stock; }
    public void setStock(int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");
        this.stock = stock;
    }
    public void setAvailable(boolean available) { this.available = available; }
    public boolean getAvailableFlag() { return available; }
}
