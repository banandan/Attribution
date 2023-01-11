package main.java.com.bala;

public class Sale implements Comparable<Sale>{
    public String userId;
    public Long timestamp;
    public double amount;

    public Sale(String userId, long timestamp, double amount) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.amount = amount;
    }

    @Override
    public int compareTo(Sale other) {
        return this.timestamp.compareTo(other.timestamp);
    }
}
