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

    // --- Amenity ---
    private final MutableLiveData<Amenity> amenity = new MutableLiveData<>();

    // --- Collections ---
    private final MutableLiveData<List<Room>> roomBookings = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Cottage>> cottageBookings = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<Menu>> menuBookings = new MutableLiveData<>(new ArrayList<>());

    // --- Billing ---
    private final MutableLiveData<Billing> billing = new MutableLiveData<>();

    // =================== LiveData Getters (read-only for UI) =================== //
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
    public LiveData<Amenity> getAmenity() { return amenity; }
    public LiveData<List<Room>> getRoomBookings() { return roomBookings; }
    public LiveData<List<Cottage>> getCottageBookings() { return cottageBookings; }
    public LiveData<List<Menu>> getMenuBookings() { return menuBookings; }
    public LiveData<Billing> getBilling() { return billing; }

    // =================== Property Getters (for Mapper / internal use) =================== //
    public MutableLiveData<Integer> bookingIDProperty() { return bookingID; }
    public MutableLiveData<Integer> guestIDProperty() { return guestID; }
    public MutableLiveData<Integer> childGuestProperty() { return childGuest; }
    public MutableLiveData<Integer> adultGuestProperty() { return adultGuest; }
    public MutableLiveData<Double> totalPriceProperty() { return totalPrice; }
    public MutableLiveData<String> bookingCreatedProperty() { return bookingCreated; }
    public MutableLiveData<String> bookingStartProperty() { return bookingStart; }
    public MutableLiveData<String> bookingEndProperty() { return bookingEnd; }
    public MutableLiveData<String> statusProperty() { return status; }
    public MutableLiveData<Guest> guestProperty() { return guest; }
    public MutableLiveData<Amenity> amenityProperty() { return amenity; }
    public MutableLiveData<List<Room>> roomBookingsProperty() { return roomBookings; }
    public MutableLiveData<List<Cottage>> cottageBookingsProperty() { return cottageBookings; }
    public MutableLiveData<List<Menu>> menuBookingsProperty() { return menuBookings; }
    public MutableLiveData<Billing> billingProperty() { return billing; }

    // =================== Setters (convenience for UI) =================== //
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
    public void setAmenity(Amenity a) { amenity.setValue(a); }
    public void setBilling(Billing b) { billing.setValue(b); }

    // =================== Collection Helpers =================== //
    public void addRoom(Room room) {
        List<Room> current = roomBookings.getValue();
        current.add(room);
        roomBookings.setValue(current);
    }

    public void addCottage(Cottage c) {
        List<Cottage> current = cottageBookings.getValue();
        current.add(c);
        cottageBookings.setValue(current);
    }

    public void addMenu(Menu mb) {
        List<Menu> current = menuBookings.getValue();
        current.add(mb);
        menuBookings.setValue(current);
        addToTotalPrice(Double.parseDouble(mb.getPrice())); // auto-update total
    }

    public void addToTotalPrice(double price) {
        Double current = totalPrice.getValue();
        if (current == null) current = 0.0;
        totalPrice.setValue(current + price);
    }

    // =================== Billing Helpers =================== //
    public void addPayment(Payment p) {
        Billing currentBilling = billing.getValue();
        if (currentBilling == null) {
            currentBilling = new Billing();
        }
        if (currentBilling.payments == null) {
            currentBilling.payments = new ArrayList<>();
        }
        currentBilling.payments.add(p);
        billing.setValue(currentBilling);
    }
    public void resetBooking() {
        // --- Booking Info ---
        bookingID.setValue(null);
        guestID.setValue(null);
        childGuest.setValue(0);
        adultGuest.setValue(0);
        totalPrice.setValue(0.0);
        bookingCreated.setValue(null);
        bookingStart.setValue(null);
        bookingEnd.setValue(null);
        status.setValue("pending");

        // --- Guest / Amenity ---
        guest.setValue(null);
        amenity.setValue(null);

        // --- Collections ---
        roomBookings.setValue(new ArrayList<>());
        cottageBookings.setValue(new ArrayList<>());
        menuBookings.setValue(new ArrayList<>());

        // --- Billing ---
        billing.setValue(new Billing());
    }

}
