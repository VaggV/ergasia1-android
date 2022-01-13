package com.example.ergasia1;

import java.util.Date;

public class Event {
    boolean acceleration; // boolean gia na deiksoume an einai epitaxynsh h frenarisma
    double speedDifference;
    String date;
    double previousSpeed;
    double latitude;
    double longtitude;

    public Event(boolean acceleration, double speedDifference, double previousSpeed, String date, double latitude, double longtitude) {
        this.acceleration = acceleration;
        this.speedDifference = speedDifference;
        this.previousSpeed = previousSpeed;
        this.date = date;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public Event(){

    }

    public boolean isAcceleration() {
        return acceleration;
    }

    public void setAcceleration(boolean acceleration) {
        this.acceleration = acceleration;
    }

    public double getSpeedDifference() {
        return speedDifference;
    }

    public void setSpeedDifference(double speedDifference) {
        this.speedDifference = speedDifference;
    }

    public double getPreviousSpeed() {
        return previousSpeed;
    }

    public void setPreviousSpeed(double previousSpeed) {
        this.previousSpeed = previousSpeed;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

