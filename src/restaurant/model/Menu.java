package restaurant.model;

import restaurant.exception.ItemNotFoundException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Holds all menu items and supports management operations. */
public class Menu {
    private final Map<Integer, MenuItem> items = new LinkedHashMap<>();
    private int nextId = 1;

    public int nextId() { return nextId++; }

    public void add(MenuItem item) {
        items.put(item.getId(), item);
        if (item.getId() >= nextId) nextId = item.getId() + 1;
    }

    public MenuItem get(int id) throws ItemNotFoundException {
        MenuItem item = items.get(id);
        if (item == null) throw new ItemNotFoundException("No menu item with id " + id);
        return item;
    }

    public void remove(int id) throws ItemNotFoundException {
        get(id);
        items.remove(id);
    }

    public List<MenuItem> all() { return new ArrayList<>(items.values()); }

    public List<MenuItem> byCategory(Category category) {
        List<MenuItem> out = new ArrayList<>();
        for (MenuItem i : items.values()) {
            if (i.getCategory() == category) out.add(i);
        }
        return out;
    }

    public void print() {
        System.out.println("\n================ MENU ================");
        for (Category c : Category.values()) {
            List<MenuItem> list = byCategory(c);
            if (list.isEmpty()) continue;
            System.out.println("-- " + c + " --");
            for (MenuItem i : list) {
                System.out.printf("  [%2d] %-22s KES %8.2f  %s%n", i.getId(), i.getName(), i.getPrice(),
                        i.isAvailable() ? "(stock " + i.getStock() + ")" : "** OUT OF STOCK **");
            }
        }
        System.out.println("======================================");
    }
}
