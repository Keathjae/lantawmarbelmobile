package com.example.lantawmarbelmobileapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookingViewModel extends ViewModel {

    // --- Booking Info ---
    private final MutableLiveData<Integer> bookingID = new MutableLiveData<>();
    private final MutableLiveData<Integer> guestID = new MutableLiveData<>();
    private final MutableLiveData<Integer> childGuest = new MutableLiveData<>();
    private final MutableLiveData<Integer> adultGuest = new MutableLiveData<>();
    private final MutableLiveData<Double> totalPrice = new MutableLiveData<>();
    private final MutableLiveData<String> bookingCreated = new MutableLiveData<>();
    private final MutableLiveData<String> bookingStart = new MutableLiveData<>();
    private final MutableLiveData<String> bookingEnd = new MutableLiveData<>();
    private final MutableLiveData<String> status = new MutableLiveData<>("pending"); // default

    // --- Guest Info ---
    private final MutableLiveData<Guest> guest = new MutableLiveData<>();

    // --- Collections ---
    private final MutableLiveData<List<Amenity>> amenityBook = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Room>> roomBookings = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Cottage>> cottageBookings = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Billing>> billing = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Menu>> menuBookings = new MutableLiveData<>(new ArrayList<>());

    // =================== Getters =================== //
    public LiveData<Integer> getBookingID() { return bookingID; }
    public LiveData<Integer> getGuestID() { return guestID; }
    public LiveData<Integer> getChildGuest() { return childGuest; }
    public LiveData<Integer> getAdultGuest() { return adultGuest; }
    public LiveData<Double> getTotalPrice() { return totalPrice; }
    public LiveData<String> getBookingCreated() { return bookingCreated; }
    public LiveData<String> getBookingStart() { return bookingStart; }
    public LiveData<String> getBookingEnd() { return bookingEnd; }
    public LiveData<String> getStatus() { return status; }
    public LiveData<Guest> getGuest() { return guest; }

    public LiveData<List<Amenity>> getAmenityBook() { return amenityBook; }
    public LiveData<List<Room>> getRoomBookings() { return roomBookings; }
    public LiveData<List<Cottage>> getCottageBookings() { return cottageBookings; }
    public LiveData<List<Billing>> getBilling() { return billing; }
    public LiveData<List<Menu>> getMenuBookings() { return menuBookings; }

    // =================== Setters =================== //
    public void setBookingID(int id) { bookingID.setValue(id); }
    public void setGuestID(int id) { guestID.setValue(id); }
    public void setChildGuest(int count) { childGuest.setValue(count); }
    public void setAdultGuest(int count) { adultGuest.setValue(count); }
    public void setTotalPrice(Double price) { totalPrice.setValue(price); }
    public void setBookingCreated(String created) { bookingCreated.setValue(created); }
    public void setBookingStart(String start) { bookingStart.setValue(start); }
    public void setBookingEnd(String end) { bookingEnd.setValue(end); }
    public void setStatus(String s) { status.setValue(s); }
    public void setGuest(Guest g) { guest.setValue(g); }

    // Collections – Add/Replace
    public void addRoom(Room room) {
        List<Room> current = roomBookings.getValue();
        current.add(room);
        roomBookings.setValue(current);
    }

    public void addAmenity(Amenity a) {
        List<Amenity> current = amenityBook.getValue();
        current.add(a);
        amenityBook.setValue(current);
    }

    public void addCottage(Cottage c) {
        List<Cottage> current = cottageBookings.getValue();
        current.add(c);
        cottageBookings.setValue(current);
    }

    public void addMenu(Menu m) {
        List<Menu> current = menuBookings.getValue();
        current.add(m);
        menuBookings.setValue(current);
    }

    public void addBilling(Billing b) {
        List<Billing> current = billing.getValue();
        current.add(b);
        billing.setValue(current);
    }

    public void addToTotalPrice(double price) {
        Double current = totalPrice.getValue();
        if (current == null) current = 0.0;
        totalPrice.setValue(current + price);
    }
    // =================== Nested Classes =================== //
    public static class Guest {
        public int guestID;
        public String firstname;
        public String lastname;
        public String email;
    }

    public static class Billing {
        public int bookingID;
        public String totalamount;
        public String status;
    }
}
