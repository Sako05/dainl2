package models;

public class Shoe {

    String shoetypename;
    String brandName;
    String size;
    String colorName;
    int price;
    int amount;

    public Shoe(String shoetypename, String size, String colorName, String brandName, int price, int amount) {
        this.shoetypename = shoetypename;
        this.brandName = brandName;
        this.size = size;
        this.colorName = colorName;
        this.price = price;
        this.amount = amount;
    }

    public String getshoeName() {
        return shoetypename;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getSize() {
        return size;
    }

    public String getColorName() {
        return colorName;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return  "Namn: " + shoetypename +
                ", Märke: " + brandName +
                ", Storlek: " + size +
                ", Färg: " + colorName +
                ", Pris: " + price +
                ", Antal i lager: " + amount + " \n";
    }
}
