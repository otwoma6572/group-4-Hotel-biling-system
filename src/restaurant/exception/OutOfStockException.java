package restaurant.exception;

/** Thrown when a menu item is unavailable or has insufficient stock. */
public class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}
