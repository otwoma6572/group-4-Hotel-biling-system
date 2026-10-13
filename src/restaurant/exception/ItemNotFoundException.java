package restaurant.exception;

/** Thrown when a menu item, table order or order line cannot be found. */
public class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
