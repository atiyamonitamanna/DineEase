package DineEase;

public class MenuItem {


    private final String name;
    private final double price;
    private int availableQuantity;

    public MenuItem(String name, double price, int availableQuantity) {
        this.name = name;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public boolean isAvailable(int quantity) {
        return availableQuantity >= quantity;
    }

    public void decreaseQuantity(int quantity) {
        if (isAvailable(quantity)) {
            availableQuantity -= quantity;
        }
    }

    @Override
    public String toString() {
        return name + ": $" + price + " (" + availableQuantity + " available)";
    }

}
