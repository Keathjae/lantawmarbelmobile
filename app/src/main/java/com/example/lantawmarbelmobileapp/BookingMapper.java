package com.example.lantawmarbelmobileapp;

import com.example.lantawmarbelmobileapp.BookingDTO;
import com.example.lantawmarbelmobileapp.BookingViewModel;
import com.example.lantawmarbelmobileapp.Amenity;

public class BookingMapper {

    public static void toViewModel(BookingDTO dto, BookingViewModel vm) {
        if (dto == null || vm == null) return;

        // Set top-level booking info
        vm.setBooking(dto);

        // Amenity
        if (dto.amenity != null) {
            Amenity amenity = new Amenity();
            amenity.setAmenityID(dto.amenity.getAmenityID());
            amenity.setAmenityname(dto.amenity.getAmenityname());
            amenity.setDescription(dto.amenity.getDescription());
            vm.setAmenity(amenity);
        }

        // Guests
        vm.setAdultGuest(dto.adultGuest);
        vm.setChildGuest(dto.childGuest);

        // Dates
        vm.setBookingStart(dto.bookingStart);
        vm.setBookingEnd(dto.bookingEnd);

        // Bookings
        if (dto.roomBookings != null) {
            for (BookingDTO.RoomBookingDTO r : dto.roomBookings) {
                vm.addRoomBooking(r);
            }
        }
        if (dto.cottageBookings != null) {
            for (BookingDTO.CottageBookingDTO c : dto.cottageBookings) {
                vm.addCottageBooking(c);
            }
        }
        if (dto.menuBookings != null) {
            for (BookingDTO.MenuBookingDTO m : dto.menuBookings) {
                vm.addMenuBooking(m);
            }
        }

        // Payments
        if (dto.billing != null && dto.billing.payments != null) {
            for (BookingDTO.PaymentDTO p : dto.billing.payments) {
                vm.addPayment(p);
            }
        }
    }
}
