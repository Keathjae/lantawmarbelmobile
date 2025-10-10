package com.example.lantawmarbelmobileapp;

import java.io.Serializable;
import java.util.Date;

// ==================== CottageBooking ====================
public class CottageBooking implements Serializable {
        private static final long serialVersionUID = 1L;

        public int cottagebookID;
        public int bookingID;
        public int cottageID; // matches JSON
        public Cottage cottage;  // optional, may be null
        public Date bookingDate;
}
