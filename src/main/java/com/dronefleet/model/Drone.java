package com.dronefleet.model;

public class Drone {
    private int id;
    private String code;
    private String model;
    private int battery;
    private String status;
    private double payload;

    public Drone(int id, String code, String model, int battery, String status, double payload) {
        this.id = id; this.code = code; this.model = model;
        this.battery = battery; this.status = status; this.payload = payload;
    }
    public int getId() { return id; }
    public String getCode() { return code; }
    public String getModel() { return model; }
    public int getBattery() { return battery; }
    public String getStatus() { return status; }
    public double getPayload() { return payload; }
}
