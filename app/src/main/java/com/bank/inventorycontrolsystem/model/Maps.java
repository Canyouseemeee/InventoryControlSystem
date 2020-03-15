package com.bank.inventorycontrolsystem.model;

public class Maps {
    private String map_id;
    private String user_id;
    private double latitude;
    private double longitude;


    public Maps() {

    }

    public Maps(String map_id, String user_id, double latitude, double longitude) {
        this.map_id = map_id;
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
