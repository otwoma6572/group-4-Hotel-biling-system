package restaurant.kitchen;

import restaurant.model.OrderItem;
import restaurant.model.OrderStatus;
import restaurant.model.StationType;

public class Fryer implements KitchenStation {
    @Override
    public String getName() { return "Fryer"; }

    @Override
    public StationType getType() { return StationType.FRYER; }

    @Override
    public void prepare(OrderItem item) {
        item.setStatus(OrderStatus.PREPARING);
        System.out.println("  [Fryer] preparing " + item.getQuantity() + " x " + item.getMenuItem().getName()
                + " for table " + item.getTableNumber());
    }
}
