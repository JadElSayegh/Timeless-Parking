package com.example.timelessparking.model;

public class ParkingSlot {
    private String name;
    private String status; // "Available" or "Occupied"

    // Required empty constructor for Firestore deserialization
    public ParkingSlot() {}

    public ParkingSlot(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status != null ? status : "Unknown";
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
