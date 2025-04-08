package com.app.dto;

public class DriverDto {
    private int driverId;
    private String driverName;
    private String vehicleNo;
    private String address;
    private double ratePer100Km;
    private int maxCapacity;
    private int drivingExperience;
    private String phoneNo;
    private double rating;
    private String driverPhotoBase64; // 👈 image encoded as Base64 string

    // Getters and Setters

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRatePer100Km() {
        return ratePer100Km;
    }

    public void setRatePer100Km(double ratePer100Km) {
        this.ratePer100Km = ratePer100Km;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getDrivingExperience() {
        return drivingExperience;
    }

    public void setDrivingExperience(int drivingExperience) {
        this.drivingExperience = drivingExperience;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDriverPhotoBase64() {
        return driverPhotoBase64;
    }

    public void setDriverPhotoBase64(String driverPhotoBase64) {
        this.driverPhotoBase64 = driverPhotoBase64;
    }
}
