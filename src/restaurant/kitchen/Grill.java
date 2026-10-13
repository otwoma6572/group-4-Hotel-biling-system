package restaurant.kitchen;

import restaurant.model.OrderItem;
import restaurant.model.OrderStatus;
import restaurant.model.StationType;

public class Grill implements KitchenStation {
    @Override
    public String getName() { return "Grill"; }

    @Override
    public StationType getType() { return StationType.GRILL; }

    @Override
    public void prepare(OrderItem item) {
        item.setStatus(OrderStatus.PREPARING);
        System.out.println("  [Grill] preparing " + item.getQuantity() + " x " + item.getMenuItem().getName()
                + " for table " + item.getTableNumber());
    }
}
