package restaurant;

import restaurant.billing.*;
import restaurant.exception.*;
import restaurant.kitchen.Kitchen;
import restaurant.model.*;
import restaurant.sales.SalesTracker;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/** Console application: menu management, orders, kitchen, billing and daily sales. */
public class RestaurantSystem {
    private final Menu menu = new Menu();
    private final Kitchen kitchen = new Kitchen();
    private final SalesTracker sales = new SalesTracker();
    private final Map<Integer, TableOrder> openOrders = new HashMap<>();
    private final Scanner in;

    public RestaurantSystem(Scanner in) {
        this.in = in;
        seedMenu();
    }

    private void seedMenu() {
        menu.add(new FoodItem(menu.nextId(), "Samosas", Category.STARTER, 150, 20, true));
        menu.add(new FoodItem(menu.nextId(), "Chicken Wings", Category.STARTER, 450, 15, true));
        menu.add(new FoodItem(menu.nextId(), "Nyama Choma", Category.MAIN, 900, 10, false));
        menu.add(new FoodItem(menu.nextId(), "Grilled Tilapia", Category.MAIN, 800, 8, false));
        menu.add(new FoodItem(menu.nextId(), "Beef Burger", Category.MAIN, 650, 12, false));
        menu.add(new FoodItem(menu.nextId(), "Chips", Category.SIDE, 250, 3, true));
        menu.add(new FoodItem(menu.nextId(), "Ugali Sukuma", Category.SIDE, 200, 15, false));
        menu.add(new FoodItem(menu.nextId(), "Fruit Salad", Category.DESSERT, 300, 10, false));
        menu.add(new DrinkItem(menu.nextId(), "Soda", Category.SOFT_DRINK, 100, 40, false));
        menu.add(new DrinkItem(menu.nextId(), "Fresh Juice", Category.SOFT_DRINK, 200, 20, false));
        menu.add(new DrinkItem(menu.nextId(), "Kenyan Tea", Category.HOT_DRINK, 120, 30, false));
        menu.add(new DrinkItem(menu.nextId(), "Coffee", Category.HOT_DRINK, 180, 30, false));
        menu.add(new DrinkItem(menu.nextId(), "Tusker Lager", Category.ALCOHOL, 350, 24, true));
    }

    // ------------------------------------------------------------------ input helpers
    private String ask(String prompt) {
        System.out.print(prompt);
        return in.hasNextLine() ? in.nextLine().trim() : "";
    }

    private int askInt(String prompt) {
        while (true) {
            String s = ask(prompt);
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a whole number.");
                if (!in.hasNextLine()) return -1;
            }
        }
    }

    private double askDouble(String prompt) {
        while (true) {
            String s = ask(prompt);
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a number.");
                if (!in.hasNextLine()) return -1;
            }
        }
    }

    private TableOrder requireOrder(int table) throws ItemNotFoundException {
        TableOrder o = openOrders.get(table);
        if (o == null) throw new ItemNotFoundException("Table " + table + " has no open order.");
        return o;
    }

    // ------------------------------------------------------------------ actions
    private void manageMenu() {
        System.out.println("\n1) Add food  2) Add drink  3) Change price  4) Restock  5) Toggle availability  6) Remove item");
        int c = askInt("Choice: ");
        try {
            switch (c) {
                case 1 -> {
                    String name = ask("Name: ");
                    Category cat = askCategory();
                    double price = askDouble("Price: ");
                    int stock = askInt("Stock: ");
                    boolean fried = ask("Fried? (y/n): ").equalsIgnoreCase("y");
                    menu.add(new FoodItem(menu.nextId(), name, cat, price, stock, fried));
                    System.out.println("Food item added.");
                }
                case 2 -> {
                    String name = ask("Name: ");
                    Category cat = askCategory();
                    double price = askDouble("Price: ");
                    int stock = askInt("Stock: ");
                    boolean alc = ask("Alcoholic? (y/n): ").equalsIgnoreCase("y");
                    menu.add(new DrinkItem(menu.nextId(), name, cat, price, stock, alc));
                    System.out.println("Drink item added.");
                }
                case 3 -> {
                    MenuItem m = menu.get(askInt("Item id: "));
                    m.setPrice(askDouble("New price: "));
                    System.out.println("Price updated.");
                }
                case 4 -> {
                    MenuItem m = menu.get(askInt("Item id: "));
                    m.setStock(askInt("New stock level: "));
                    System.out.println("Stock updated.");
                }
                case 5 -> {
                    MenuItem m = menu.get(askInt("Item id: "));
                    m.setAvailable(!m.getAvailableFlag());
                    System.out.println(m.getName() + (m.getAvailableFlag() ? " is now available." : " is now disabled."));
                }
                case 6 -> {
                    menu.remove(askInt("Item id: "));
                    System.out.println("Item removed.");
                }
                default -> System.out.println("Unknown choice.");
            }
        } catch (ItemNotFoundException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Category askCategory() {
        Category[] cats = Category.values();
        for (int i = 0; i < cats.length; i++) System.out.println("  " + (i + 1) + ") " + cats[i]);
        int n = askInt("Category number: ");
        return (n >= 1 && n <= cats.length) ? cats[n - 1] : cats[0];
    }

    private void openTable() {
        int t = askInt("Table number: ");
        if (openOrders.containsKey(t)) {
            System.out.println("Table " + t + " already has an open order.");
        } else {
            openOrders.put(t, new TableOrder(t));
            System.out.println("Order opened for table " + t + ".");
        }
    }

    private void addItem() {
        try {
            TableOrder o = requireOrder(askInt("Table number: "));
            MenuItem m = menu.get(askInt("Menu item id: "));
            int q = askInt("Quantity: ");
            OrderItem line = o.addItem(m, q);
            System.out.println("Added: " + line);
        } catch (OutOfStockException e) {
            System.out.println("Sorry - out of stock: " + e.getMessage());
        } catch (ItemNotFoundException | InvalidOrderException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeItem() {
        try {
            TableOrder o = requireOrder(askInt("Table number: "));
            showOrder(o);
            o.removeItem(askInt("Order line # to remove: "));
            System.out.println("Line removed.");
        } catch (ItemNotFoundException | InvalidOrderException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showOrder(TableOrder o) {
        System.out.println("Table " + o.getTableNumber() + " order:");
        for (OrderItem i : o.getItems()) System.out.println("  " + i);
        System.out.printf("  Subtotal: KES %.2f%n", o.getSubtotal());
    }

    private void sendToKitchen() {
        try {
            TableOrder o = requireOrder(askInt("Table number: "));
            int n = kitchen.sendOrder(o);
            System.out.println(n + " item(s) sent to the kitchen.");
        } catch (ItemNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void kitchenMenu() {
        kitchen.printStatus();
        System.out.println("1) Start next in queue  2) Mark READY  3) Mark SERVED");
        int c = askInt("Choice: ");
        try {
            switch (c) {
                case 1 -> kitchen.startNext();
                case 2 -> { kitchen.markReady(askInt("Order line #: ")); System.out.println("Marked ready."); }
                case 3 -> { kitchen.markServed(askInt("Order line #: ")); System.out.println("Marked served."); }
                default -> System.out.println("Unknown choice.");
            }
        } catch (ItemNotFoundException | InvalidOrderException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void billAndPay() {
        try {
            int t = askInt("Table number: ");
            TableOrder o = requireOrder(t);
            if (o.getItems().isEmpty()) throw new InvalidOrderException("Order is empty.");
            if (!o.allServed()) {
                String ok = ask("Not all items are served yet. Bill anyway? (y/n): ");
                if (!ok.equalsIgnoreCase("y")) return;
            }
            System.out.println("Discount: 1) None  2) Happy hour  3) Student  4) Loyalty");
            Discount d;
            switch (askInt("Choice: ")) {
                case 2 -> d = new HappyHourDiscount();
                case 3 -> d = new StudentDiscount();
                case 4 -> d = new LoyaltyDiscount(askInt("Loyalty points: "));
                default -> d = new NoDiscount();
            }
            double tip = askDouble("Tip % (0 for none): ") / 100.0;
            Bill bill = new Bill(o, Bill.DEFAULT_TAX_RATE, d, Math.max(0, tip));
            bill.print();
            int people = askInt("Split between how many people? (1 = no split): ");
            if (people > 1) System.out.printf("Each person pays: KES %.2f%n", bill.splitShare(people));
            sales.record(bill);
            o.close();
            openOrders.remove(t);
            System.out.println("Payment recorded. Table " + t + " is free.");
        } catch (ItemNotFoundException | InvalidOrderException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void endOfDay() {
        System.out.println();
        System.out.print(sales.buildSummary());
        try {
            Path p = sales.writeSummary();
            System.out.println("Summary written to " + p.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Could not write summary file: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------ main loop
    public void run() {
        System.out.println("=== Restaurant Order and Billing System ===");
        while (true) {
            System.out.println("\n1) View menu        2) Manage menu     3) Open table order");
            System.out.println("4) Add item         5) Remove item     6) View table order");
            System.out.println("7) Send to kitchen  8) Kitchen board   9) Bill and pay");
            System.out.println("10) End of day summary                 0) Exit");
            int c = askInt("Choice: ");
            switch (c) {
                case 1 -> menu.print();
                case 2 -> manageMenu();
                case 3 -> openTable();
                case 4 -> addItem();
                case 5 -> removeItem();
                case 6 -> {
                    try { showOrder(requireOrder(askInt("Table number: "))); }
                    catch (ItemNotFoundException e) { System.out.println("Error: " + e.getMessage()); }
                }
                case 7 -> sendToKitchen();
                case 8 -> kitchenMenu();
                case 9 -> billAndPay();
                case 10 -> endOfDay();
                case 0, -1 -> {
                    if (sales.billCount() > 0) endOfDay();
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Unknown choice.");
            }
        }
    }

    // ------------------------------------------------------------------ scripted demo
    public void runDemo() throws Exception {
        System.out.println("=== DEMO MODE ===");
        menu.print();

        System.out.println("\n> Open table 5 and take an order");
        TableOrder t5 = new TableOrder(5);
        openOrders.put(5, t5);
        t5.addItem(menu.get(3), 2);   // Nyama Choma
        t5.addItem(menu.get(6), 2);   // Chips (stock 3)
        t5.addItem(menu.get(9), 3);   // Soda
        showOrder(t5);

        System.out.println("\n> Try to order 5 more Chips (only 1 left) -> custom exception");
        try {
            t5.addItem(menu.get(6), 5);
        } catch (OutOfStockException e) {
            System.out.println("Handled gracefully: " + e.getMessage());
        }

        System.out.println("\n> Remove Soda before sending to the kitchen");
        t5.removeItem(t5.getItems().get(2).getId());
        t5.addItem(menu.get(13), 2);  // Tusker
        showOrder(t5);

        System.out.println("\n> Send to kitchen and process the queue");
        System.out.println(kitchen.sendOrder(t5) + " items queued.");
        try {
            t5.removeItem(t5.getItems().get(0).getId());
        } catch (InvalidOrderException e) {
            System.out.println("Handled gracefully: " + e.getMessage());
        }
        while (kitchen.queueSize() > 0) {
            OrderItem it = kitchen.startNext();
            kitchen.markReady(it.getId());
            kitchen.markServed(it.getId());
        }
        kitchen.printStatus();

        System.out.println("\n> Bill table 5 with Happy Hour discount, 10% tip, split 3 ways");
        Bill b1 = new Bill(t5, Bill.DEFAULT_TAX_RATE, new HappyHourDiscount(), 0.10);
        b1.print();
        System.out.printf("Each of 3 pays: KES %.2f%n", b1.splitShare(3));
        sales.record(b1);
        t5.close();
        openOrders.remove(5);

        System.out.println("\n> Second table, student discount");
        TableOrder t2 = new TableOrder(2);
        t2.addItem(menu.get(5), 1);
        t2.addItem(menu.get(11), 1);
        kitchen.sendOrder(t2);
        while (kitchen.queueSize() > 0) {
            OrderItem it = kitchen.startNext();
            kitchen.markReady(it.getId());
            kitchen.markServed(it.getId());
        }
        Bill b2 = new Bill(t2, new StudentDiscount());
        b2.print();
        sales.record(b2);

        System.out.println("\n> End of day");
        endOfDay();
    }

    public static void main(String[] args) throws Exception {
        RestaurantSystem app = new RestaurantSystem(new Scanner(System.in));
        if (args.length > 0 && args[0].equalsIgnoreCase("--demo")) {
            app.runDemo();
        } else {
            app.run();
        }
    }
}
