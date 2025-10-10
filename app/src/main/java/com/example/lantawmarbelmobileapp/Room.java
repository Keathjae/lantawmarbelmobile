package com.example.lantawmarbelmobileapp;

import java.io.Serializable;
import java.util.Objects;

public class Room implements Serializable {
    private int roomID;
    private int roomnum;
    private String description;
    private String roomtype;
    private int roomcapacity;
    private String price;
    private String image_url;
    private String status;
    private boolean selected;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return roomID == room.roomID &&
                roomnum == room.roomnum &&
                roomcapacity == room.roomcapacity &&
                Objects.equals(roomtype, room.roomtype) &&
                Objects.equals(description, room.description) &&
                Objects.equals(price, room.price) &&
                Objects.equals(image_url, room.image_url) &&
                Objects.equals(status, room.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID, roomnum, roomtype, description, price, image_url, status, roomcapacity);
    }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    // Getters
    public int getRoomID() { return roomID; }
    public int getRoomnum() { return roomnum; }
    public String getDescription() { return description; }
    public String getRoomtype() { return roomtype; }
    public int getRoomcapacity() { return roomcapacity; }
    public String getPrice() { return price; }
    public String getImage_url() { return image_url; }
    public String getStatus() { return status; }
}
