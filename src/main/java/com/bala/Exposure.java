package main.java.com.bala;

public class Exposure implements Comparable<Exposure>{
    public String userId;

    public Long timestamp;
    public String creativeId;

    public boolean hasClicked;

    public Exposure(String userId, long timestamp, String creativeId){
        this.userId = userId;
        this.timestamp = timestamp;
        this.creativeId = creativeId;
    }

    @Override
    public int compareTo(Exposure other) {
        return this.timestamp.compareTo(other.timestamp);
    }
}
