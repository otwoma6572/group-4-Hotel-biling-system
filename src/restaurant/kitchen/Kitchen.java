package restaurant.kitchen;

import restaurant.exception.InvalidOrderException;
import restaurant.exception.ItemNotFoundException;
import restaurant.model.OrderItem;
import restaurant.model.OrderStatus;
import restaurant.model.TableOrder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Kitchen workflow: NEW -> QUEUED -> PREPARING -> READY -> SERVED.
 * Waiting items live in a FIFO Queue&lt;OrderItem&gt;.
 */
public class Kitchen {
    private final Queue<OrderItem> queue = new LinkedList<>();
    private final List<OrderItem> inProgress = new ArrayList<>();
    private final List<KitchenStation> stations = new ArrayList<>();

    public Kitchen() {
        stations.add(new Grill());
        stations.add(new Fryer());
        stations.add(new Bar());
    }

    /** Sends every NEW item of the order to the kitchen queue. Returns how many were sent. */
    public int sendOrder(TableOrder order) {
        int sent = 0;
        for (OrderItem item : order.getItems()) {
            if (item.getStatus() == OrderStatus.NEW) {
                item.setStatus(OrderStatus.QUEUED);
                queue.add(item);
                sent++;
            }
        }
        return sent;
    }

    /** Takes the next item from the queue and hands it to the right station. */
    public OrderItem startNext() throws InvalidOrderException {
        OrderItem item = queue.poll();
        if (item == null) throw new InvalidOrderException("The kitchen queue is empty.");
        for (KitchenStation station : stations) {
            if (station.canPrepare(item)) {
                station.prepare(item);
                inProgress.add(item);
                return item;
            }
        }
        throw new InvalidOrderException("No station can prepare " + item.getMenuItem().getName());
    }

    public void markReady(int orderItemId) throws ItemNotFoundException, InvalidOrderException {
        OrderItem item = findInProgress(orderItemId);
        if (item.getStatus() != OrderStatus.PREPARING) {
            throw new InvalidOrderException("Item #" + orderItemId + " is " + item.getStatus() + ", not PREPARING.");
        }
        item.setStatus(OrderStatus.READY);
    }

    public void markServed(int orderItemId) throws ItemNotFoundException, InvalidOrderException {
        OrderItem item = findInProgress(orderItemId);
        if (item.getStatus() != OrderStatus.READY) {
            throw new InvalidOrderException("Item #" + orderItemId + " is " + item.getStatus() + ", not READY.");
        }
        item.setStatus(OrderStatus.SERVED);
    }

    private OrderItem findInProgress(int id) throws ItemNotFoundException {
        for (OrderItem i : inProgress) {
            if (i.getId() == id) return i;
        }
        throw new ItemNotFoundException("Item #" + id + " has not been started in the kitchen.");
    }

    public int queueSize() { return queue.size(); }

    public void printStatus() {
        System.out.println("\n--- KITCHEN STATUS ---");
        System.out.println("Queued (" + queue.size() + "):");
        for (OrderItem i : queue) System.out.println("  " + i);
        System.out.println("In progress / ready / served:");
        for (OrderItem i : inProgress) System.out.println("  " + i);
        System.out.println("----------------------");
    }
}
