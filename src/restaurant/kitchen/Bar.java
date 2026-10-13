package restaurant.kitchen;

import restaurant.model.OrderItem;
import restaurant.model.OrderStatus;
import restaurant.model.StationType;

public class Bar implements KitchenStation {
    @Override
    public String getName() { return "Bar"; }

    @Override
    public StationType getType() { return StationType.BAR; }

    @Override
    public void prepare(OrderItem item) {
        item.setStatus(OrderStatus.PREPARING);
        System.out.println("  [Bar] preparing " + item.getQuantity() + " x " + item.getMenuItem().getName()
                + " for table " + item.getTableNumber());
    }
}
