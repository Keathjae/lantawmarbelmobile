package com.example.lantawmarbelmobileapp;

import com.google.gson.annotations.SerializedName;

public class Menu {

    @SerializedName("menuID")
    private int menuID;

    @SerializedName("menuname")
    private String menuname;

    @SerializedName("itemtype")
    private String itemtype;

    @SerializedName("image")
    private String image;

    @SerializedName("price")
    private String price;

    @SerializedName("status")
    private String status;

    // Constructor
    public Menu(int menuID, String menuname, String itemtype, String image, String price, String status) {
        this.menuID = menuID;
        this.menuname = menuname;
        this.itemtype = itemtype;
        this.image = image;
        this.price = price;
        this.status = status;
    }

    // Getters
    public int getMenuID() {
        return menuID;
    }

    public String getMenuname() {
        return menuname;
    }

    public String getItemtype() {
        return itemtype;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
