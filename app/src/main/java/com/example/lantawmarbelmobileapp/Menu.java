package com.example.lantawmarbelmobileapp;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Menu implements Serializable {

    @SerializedName("menuID")
    private int menuID;
    private int qty;
    @SerializedName("menuname")
    private String menuname;

    @SerializedName("itemtype")
    private String itemtype;

    @SerializedName("image")
    private String image;

    @SerializedName("image_url")
    private String imageUrl; // full URL

    @SerializedName("price")
    private String price;

    @SerializedName("status")
    private String status;
    private boolean selected;

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    // Constructor
    public Menu(int menuID, String menuname, String itemtype, String image, String imageUrl, String price, String status) {
        this.menuID = menuID;
        this.menuname = menuname;
        this.itemtype = itemtype;
        this.image = image;
        this.imageUrl = imageUrl;
        this.price = price;
        this.status = status;
    }

    public Menu() { }

    // --- Getters ---
    public int getMenuID() { return menuID; }
    public int getqty() { return qty; }
    public String getMenuname() { return menuname; }
    public String getItemtype() { return itemtype; }
    public String getImage() { return image; }
    public String getImageUrl() { return imageUrl; }
    public String getPrice() { return price; }
    public String getStatus() { return status; }

    // --- Setters ---
    public void setMenuID(int menuID) { this.menuID = menuID; }
    public void setqty(int qty) { this.qty = qty; }
    public void setMenuname(String menuname) { this.menuname = menuname; }
    public void setItemtype(String itemtype) { this.itemtype = itemtype; }
    public void setImage(String image) { this.image = image; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPrice(String price) { this.price = price; }
    public void setStatus(String status) { this.status = status; }
}
