package com.example.lantawmarbelmobileapp;

import java.io.Serializable;
import java.util.Date;

// ==================== MenuBooking ====================
public class MenuBooking implements Serializable {
        private static final long serialVersionUID = 1L;

        public int id;
        public int booking_id;
        public int menu_id; // matches JSON
        public int quantity;
        public Double adultPrice;
        public Double childPrice;
        public Menu menu; // optional, may be null
        public Date bookingDate;
}
