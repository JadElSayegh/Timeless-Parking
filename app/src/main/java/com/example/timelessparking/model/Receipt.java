package com.example.timelessparking.model;

public class Receipt {
    private String licensePlate;
    private String name;
    private long timestamp;
    private int total;
    private String uid;
    private String duration;

    public Receipt() {
        // Needed for Firestore
    }

    // Getters and setters
    public String getLicensePlate() { return licensePlate; }
    public String getName() { return name; }
    public long getTimestamp() { return timestamp; }
    public int getTotal() { return total; }
    public String getUid() { return uid; }
    public String getDuration() { return duration; }
}
