public class Transaction {
    public Transaction(String description, double price, int day) {
        this.description = description;
        this.price = price;
        this.day = day;
    }

    public String description;
    public double price;
    public int day;
}