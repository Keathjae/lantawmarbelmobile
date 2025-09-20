package com.example.lantawmarbelmobileapp;


public class Cottage {
    private int cottageID;
    private String cottageName;
    private int capacity;
    private String image;
    private double price;
    private String status;
    private int amenityID;

    private Amenity amenity; // Relationship (optional)

    // --- Getters & Setters ---
    public int getCottageID() {
        return cottageID;
    }

    public void setCottageID(int cottageID) {
        this.cottageID = cottageID;
    }

    public String getCottageName() {
        return cottageName;
    }

    public void setCottageName(String cottageName) {
        this.cottageName = cottageName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmenityID() {
        return amenityID;
    }

    public void setAmenityID(int amenityID) {
        this.amenityID = amenityID;
    }

    public Amenity getAmenity() {
        return amenity;
    }

    public void setAmenity(Amenity amenity) {
        this.amenity = amenity;
    }
}

