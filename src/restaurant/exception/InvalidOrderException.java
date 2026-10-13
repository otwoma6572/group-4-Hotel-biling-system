package restaurant.exception;

/** Thrown when an order operation is not allowed (e.g. removing an item already in the kitchen). */
public class InvalidOrderException extends Exception {
    public InvalidOrderException(String message) {
        super(message);
    }
}
