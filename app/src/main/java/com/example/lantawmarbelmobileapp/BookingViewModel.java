package com.example.lantawmarbelmobileapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookingViewModel extends ViewModel {

    private final MutableLiveData<BookingDTO> booking = new MutableLiveData<>(new BookingDTO());

    // Amenity LiveData
    private final MutableLiveData<Amenity> selectedAmenity = new MutableLiveData<>();

    // Guest counts as LiveData
    private final MutableLiveData<Integer> childGuest = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> adultGuest = new MutableLiveData<>(0);

    // Booking dates as LiveData
    private final MutableLiveData<String> bookingStart = new MutableLiveData<>();
    private final MutableLiveData<String> bookingEnd = new MutableLiveData<>();

    public LiveData<BookingDTO> getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO dto) {
        booking.setValue(dto);
        selectedAmenity.setValue(dto.amenity);
        childGuest.setValue(dto.childGuest);
        adultGuest.setValue(dto.adultGuest);
        bookingStart.setValue(dto.bookingStart);
        bookingEnd.setValue(dto.bookingEnd);
        recalcTotalPrice();
    }

    // =================== Booking Dates ===================
    public LiveData<String> getBookingStart() { return bookingStart; }
    public LiveData<String> getBookingEnd() { return bookingEnd; }

    public void setBookingStart(String start) {
        bookingStart.setValue(start);
        BookingDTO dto = booking.getValue();
        if (dto == null) dto = new BookingDTO();
        dto.bookingStart = start;
        booking.setValue(dto);
        recalcTotalPrice();
    }

    public void setBookingEnd(String end) {
        bookingEnd.setValue(end);
        BookingDTO dto = booking.getValue();
        if (dto == null) dto = new BookingDTO();
        dto.bookingEnd = end;
        booking.setValue(dto);
        recalcTotalPrice();
    }

    // =================== Guest Counts ===================
    public LiveData<Integer> getChildGuest() { return childGuest; }
    public LiveData<Integer> getAdultGuest() { return adultGuest; }

    public void setChildGuest(int count) {
        childGuest.setValue(count);
        BookingDTO dto = booking.getValue();
        if (dto == null) dto = new BookingDTO();
        dto.childGuest = count;
        booking.setValue(dto);
        recalcTotalPrice();
    }

    public void setAdultGuest(int count) {
        adultGuest.setValue(count);
        BookingDTO dto = booking.getValue();
        if (dto == null) dto = new BookingDTO();
        dto.adultGuest = count;
        booking.setValue(dto);
        recalcTotalPrice();
    }

    // =================== Amenity Methods ===================
    public LiveData<Amenity> getAmenity() { return selectedAmenity; }

    public void setAmenity(Amenity a) {
        selectedAmenity.setValue(a);
        BookingDTO dto = booking.getValue();
        if (dto == null) dto = new BookingDTO();
        dto.amenity = a;
        booking.setValue(dto);
        recalcTotalPrice();
    }

    public void selectAmenity(Amenity a) { setAmenity(a); }

    // =================== Collection Helpers ===================
    public LiveData<List<BookingDTO.RoomBookingDTO>> getRoomBookings() {
        MutableLiveData<List<BookingDTO.RoomBookingDTO>> liveData = new MutableLiveData<>();
        liveData.setValue(booking.getValue() != null && booking.getValue().roomBookings != null
                ? booking.getValue().roomBookings : new ArrayList<>());
        return liveData;
    }

    public LiveData<List<BookingDTO.CottageBookingDTO>> getCottageBookings() {
        MutableLiveData<List<BookingDTO.CottageBookingDTO>> liveData = new MutableLiveData<>();
        liveData.setValue(booking.getValue() != null && booking.getValue().cottageBookings != null
                ? booking.getValue().cottageBookings : new ArrayList<>());
        return liveData;
    }

    public LiveData<List<BookingDTO.MenuBookingDTO>> getMenuBookings() {
        MutableLiveData<List<BookingDTO.MenuBookingDTO>> liveData = new MutableLiveData<>();
        liveData.setValue(booking.getValue() != null && booking.getValue().menuBookings != null
                ? booking.getValue().menuBookings : new ArrayList<>());
        return liveData;
    }

    public LiveData<List<BookingDTO.PaymentDTO>> getPayments() {
        MutableLiveData<List<BookingDTO.PaymentDTO>> liveData = new MutableLiveData<>();
        if (booking.getValue() != null && booking.getValue().billing != null
                && booking.getValue().billing.payments != null) {
            liveData.setValue(booking.getValue().billing.payments);
        } else {
            liveData.setValue(new ArrayList<>());
        }
        return liveData;
    }

    // =================== Add / Update Helpers ===================
    public void addRoomBooking(BookingDTO.RoomBookingDTO r) {
        BookingDTO dto = booking.getValue();
        if (dto == null) dto = new BookingDTO();
        if (dto.roomBookings == null) dto.roomBookings = new ArrayList<>();
        dto.roomBookings.add(r);
        booking.setValue(dto);
        recalcTotalPrice();
    }

    public void addCottageBooking(BookingDTO.CottageBookingDTO c) {
        BookingDTO dto = booking.getValue();
        if (dto == null) dto = new BookingDTO();
        if (dto.cottageBookings == null) dto.cottageBookings = new ArrayList<>();
        dto.cottageBookings.add(c);
        booking.setValue(dto);
        recalcTotalPrice();
    }

    public void addMenuBooking(BookingDTO.MenuBookingDTO m) {
        BookingDTO dto = booking.getValue();
        if (dto == null) dto = new BookingDTO();
        if (dto.menuBookings == null) dto.menuBookings = new ArrayList<>();
        dto.menuBookings.add(m);
        booking.setValue(dto);
        recalcTotalPrice();
    }

    public void addPayment(BookingDTO.PaymentDTO payment) {
        BookingDTO dto = booking.getValue();
        if (dto == null) dto = new BookingDTO();
        if (dto.billing == null) dto.billing = new BookingDTO.BillingDTO();
        if (dto.billing.payments == null) dto.billing.payments = new ArrayList<>();
        dto.billing.payments.add(payment);
        booking.setValue(dto);
    }

    public void resetBooking() {
        booking.setValue(new BookingDTO());
        selectedAmenity.setValue(null);
    }

    public void resetMenuBookings() {
        BookingDTO dto = booking.getValue();
        if (dto != null && dto.menuBookings != null) {
            dto.menuBookings.clear();
            booking.setValue(dto);
            recalcTotalPrice();
        }
    }

    // =================== Total Price Calculation ===================
    // =================== Total Price Calculation ===================
    private void recalcTotalPrice() {
        BookingDTO dto = booking.getValue();
        if (dto == null) return;

        double total = 0.0;

        // Rooms
        if (dto.roomBookings != null) {
            for (BookingDTO.RoomBookingDTO r : dto.roomBookings) {
                Double price = r.price != null ? r.price : 0.0;
                total += price;
            }
        }

        // Cottages
        if (dto.cottageBookings != null) {
            for (BookingDTO.CottageBookingDTO c : dto.cottageBookings) {
                Double price = c.price != null ? c.price : 0.0;
                total += price;
            }
        }

        // Menus
        if (dto.menuBookings != null) {
            for (BookingDTO.MenuBookingDTO m : dto.menuBookings) {
                Double price = m.price != 0 ? m.price : 0.0;
                Integer quantity = m.quantity != 0 ? m.quantity : 1;
                total += price * quantity;
            }
        }

        // Amenity (multiplied by number of days)
        if (dto.amenity != null) {
            double adultPrice = 0.0;
            double childPrice = 0.0;
            try {
                adultPrice = dto.amenity.getAdultprice() != null
                        ? Double.parseDouble(dto.amenity.getAdultprice())
                        : 0.0;
                childPrice = dto.amenity.getChildprice() != null
                        ? Double.parseDouble(dto.amenity.getChildprice())
                        : 0.0;
            } catch (NumberFormatException e) {
                adultPrice = 0.0;
                childPrice = 0.0;
            }

            int adults = dto.adultGuest > 0 ? dto.adultGuest : 0;
            int children = dto.childGuest > 0 ? dto.childGuest : 0;

            // calculate number of days between bookingStart and bookingEnd
            int numDays = 1;
            try {
                if (dto.bookingStart != null && dto.bookingEnd != null) {
                    java.time.LocalDate start = java.time.LocalDate.parse(dto.bookingStart);
                    java.time.LocalDate end = java.time.LocalDate.parse(dto.bookingEnd);
                    long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(start, end);
                    numDays = (int) (daysBetween > 0 ? daysBetween : 1);
                }
            } catch (Exception e) {
                numDays = 1; // fallback
            }

            total += numDays * (adultPrice * adults + childPrice * children);
        }

        dto.totalPrice = total;
        dto.guestAmount = dto.adultGuest + dto.childGuest; // update guest amount too

// force LiveData update (even if same object reference)
        booking.setValue(null);
        booking.setValue(dto);

    }


}
