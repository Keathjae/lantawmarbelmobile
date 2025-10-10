package com.example.lantawmarbelmobileapp;

import java.io.Serializable;

public class Cottage implements Serializable {
    private int cottageID;
    private String cottagename;
    private int capacity;
    private String image;
    private String image_url;
    private String price;
    private String status;
    private int amenityID;
    private boolean selected;
    private Amenity amenity; // optional relationship

    // --- Getters & Setters ---
    public int getCottageID() { return cottageID; }
    public void setCottageID(int cottageID) { this.cottageID = cottageID; }

    public String getCottagename() { return cottagename; }
    public void setCottagename(String cottagename) { this.cottagename = cottagename; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getAmenityID() { return amenityID; }
    public void setAmenityID(int amenityID) { this.amenityID = amenityID; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    public Amenity getAmenity() { return amenity; }
    public void setAmenity(Amenity amenity) { this.amenity = amenity; }
}
