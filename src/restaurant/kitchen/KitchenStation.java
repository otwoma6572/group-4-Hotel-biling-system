package restaurant.kitchen;

import restaurant.model.OrderItem;
import restaurant.model.StationType;

/** A cooking/preparation station in the kitchen. */
public interface KitchenStation {
    String getName();

    StationType getType();

    /** True if this station handles the given order line. */
    default boolean canPrepare(OrderItem item) {
        return item.getMenuItem().getStationType() == getType();
    }

    /** Begin preparing the item. */
    void prepare(OrderItem item);
}
