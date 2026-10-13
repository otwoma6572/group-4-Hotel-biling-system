package restaurant.model;

public class FoodItem extends MenuItem {
    private final boolean fried;

    public FoodItem(int id, String name, Category category, double price, int stock, boolean fried) {
        super(id, name, category, price, stock);
        this.fried = fried;
    }

    @Override
    public StationType getStationType() {
        return fried ? StationType.FRYER : StationType.GRILL;
    }

    @Override
    public String getKind() { return "FOOD"; }
}
