package com.example.lantawmarbelmobileapp;

public class Amenity {
        private int amenityID;
        private String amenityname;
        private String description;
        private double adultprice;
        private double childprice;

        private String image;
        private String status;

        // Getters and Setters
        public int getAmenityID() {
            return amenityID;
        }

        public void setAmenityID(int amenityID) {
            this.amenityID = amenityID;
        }

        public String getAmenityname() {
            return amenityname;
        }

        public void setAmenityname(String amenityname) {
            this.amenityname = amenityname;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getAdultprice() {
            return adultprice;
        }

        public void setAdultprice(double adultprice) {
            this.adultprice = adultprice;
        }

        public double getChildprice() {
            return childprice;
        }

        public void setChildprice(double childprice) {
            this.childprice = childprice;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
}
