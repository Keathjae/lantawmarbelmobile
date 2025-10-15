package com.example.lantawmarbelmobileapp;

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

        // ✅ Use safe copies of lists before looping
        if (dto.roomBookings != null) {
            for (BookingDTO.RoomBookingDTO r : new java.util.ArrayList<>(dto.roomBookings)) {
                vm.addRoomBooking(r);
            }
        }

        if (dto.cottageBookings != null) {
            for (BookingDTO.CottageBookingDTO c : new java.util.ArrayList<>(dto.cottageBookings)) {
                vm.addCottageBooking(c);
            }
        }

        if (dto.menuBookings != null) {
            for (BookingDTO.MenuBookingDTO m : new java.util.ArrayList<>(dto.menuBookings)) {
                vm.addMenuBooking(m);
            }
        }

        if (dto.billing != null && dto.billing.payments != null) {
            for (BookingDTO.PaymentDTO p : new java.util.ArrayList<>(dto.billing.payments)) {
                vm.addPayment(p);
            }
        }
    }
}
