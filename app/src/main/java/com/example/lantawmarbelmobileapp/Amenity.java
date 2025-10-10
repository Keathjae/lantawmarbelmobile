package com.example.lantawmarbelmobileapp;

import java.io.Serializable;

public class Amenity implements Serializable {
    private int amenityID;
    private String amenityname;
    private String description;
    private String adultprice;
    private String childprice;
    private String image;
    private String image_url;
    private String status;
    private boolean selected;

    // --- Getters & Setters ---
    public int getAmenityID() { return amenityID; }
    public void setAmenityID(int amenityID) { this.amenityID = amenityID; }

    public String getAmenityname() { return amenityname; }
    public void setAmenityname(String amenityname) { this.amenityname = amenityname; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAdultprice() { return adultprice; }
    public void setAdultprice(String adultprice) { this.adultprice = adultprice; }

    public String getChildprice() { return childprice; }
    public void setChildprice(String childprice) { this.childprice = childprice; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}
