package restaurant.model;

public class DrinkItem extends MenuItem {
    private final boolean alcoholic;

    public DrinkItem(int id, String name, Category category, double price, int stock, boolean alcoholic) {
        super(id, name, category, price, stock);
        this.alcoholic = alcoholic;
    }

    public boolean isAlcoholic() { return alcoholic; }

    @Override
    public StationType getStationType() { return StationType.BAR; }

    @Override
    public String getKind() { return "DRINK"; }
}
