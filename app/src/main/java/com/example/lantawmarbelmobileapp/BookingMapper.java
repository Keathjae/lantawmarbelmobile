package com.example.lantawmarbelmobileapp;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {

    // ==================== VM → DTO (for API request) ====================
    public static BookingRequest fromViewModel(BookingViewModel vm) {
        BookingRequest dto = new BookingRequest();

        dto.bookingID = safeInt(vm.getBookingID().getValue());
        dto.guestID = safeInt(vm.getGuestID().getValue());
        dto.childGuest = safeInt(vm.getChildGuest().getValue());
        dto.adultGuest = safeInt(vm.getAdultGuest().getValue());
        dto.guestamount = dto.adultGuest + dto.childGuest;
        dto.totalPrice = safeDouble(vm.getTotalPrice().getValue());
        dto.bookingCreated = vm.getBookingCreated().getValue();
        dto.bookingStart = vm.getBookingStart().getValue();
        dto.bookingEnd = vm.getBookingEnd().getValue();
        dto.status = vm.getStatus().getValue();

        // Guest
        if (vm.getGuest().getValue() != null) {
            Guest g = vm.getGuest().getValue();
            GuestDTO guestDTO = new GuestDTO();
            guestDTO.guestID = g.guestID;
            guestDTO.firstname = g.firstName;
            guestDTO.lastname = g.lastName;
            guestDTO.email = g.email;
            dto.guest = guestDTO;
        }

        // Amenity
        if (vm.getAmenity().getValue() != null) {
            Amenity a = vm.getAmenity().getValue();
            AmenityDTO amenityDTO = new AmenityDTO();
            amenityDTO.amenityID = a.getAmenityID();
            amenityDTO.amenityname = a.getAmenityname();
            amenityDTO.description = a.getDescription();
            amenityDTO.adultprice = a.getAdultprice();
            amenityDTO.childprice = a.getChildprice();
            dto.amenity = amenityDTO;
        }

        // Collections
        dto.roomBookings = mapRooms(vm.getRoomBookings().getValue());
        dto.cottageBookings = mapCottages(vm.getCottageBookings().getValue());
        dto.menuBookings = mapMenuBookings(vm.getMenuBookings().getValue());

        // Billing
        if (vm.getBilling().getValue() != null) {
            dto.billing = mapBilling(vm.getBilling().getValue());
        }

        return dto;
    }

    // ==================== DTO → VM (for editing existing bookings) ====================
    public static void toViewModel(Booking booking, BookingViewModel vm) {
        if (booking == null || vm == null) return;

        // Base fields
        vm.bookingIDProperty().setValue(booking.bookingID);
        vm.guestIDProperty().setValue(booking.guestID);
        vm.childGuestProperty().setValue(booking.childGuest);
        vm.adultGuestProperty().setValue(booking.adultGuest);
        vm.totalPriceProperty().setValue( booking.totalPrice);
        vm.bookingCreatedProperty().setValue(booking.bookingCreated);
        vm.bookingStartProperty().setValue(booking.bookingStart);
        vm.bookingEndProperty().setValue(booking.bookingEnd);
        vm.statusProperty().setValue(booking.status);

        // Guest
        if (booking.guest != null) {
            vm.guestProperty().setValue(booking.guest);
        }

        // Amenity
        if (booking.amenity != null) {
            vm.amenityProperty().setValue(booking.amenity);
        }

        // Collections
        if (booking.roomBookings != null) {
            vm.roomBookingsProperty().setValue(booking.roomBookings);
        }
        if (booking.cottageBookings != null) {
            vm.cottageBookingsProperty().setValue(booking.cottageBookings);
        }
        if (booking.menuBookings != null) {
            vm.menuBookingsProperty().setValue(booking.menuBookings);
        }

        // Billing
        if (booking.billing != null) {
            Billing billingVM = new Billing();
            billingVM.billingID = booking.billing.billingID;
            billingVM.bookingID = booking.billing.bookingID;
            billingVM.totalAmount = booking.billing.totalAmount;
            billingVM.dateBilled = booking.billing.dateBilled;
            billingVM.status = booking.billing.status;

            if (booking.billing.payments != null) {
                billingVM.payments = new ArrayList<>();
                for (Payment p : booking.billing.payments) {
                    Payment pVM = new Payment();
                    pVM.paymentID = p.paymentID;
                    pVM.totalTender = p.totalTender;
                    pVM.totalChange = p.totalChange;
                    pVM.datePayment = p.datePayment;
                    pVM.refNumber = p.refNumber;
                    billingVM.payments.add(pVM);
                }
            }
            vm.billingProperty().setValue(billingVM);
        }
    }

    // ==================== Helpers ====================
    private static int safeInt(Integer v) { return v == null ? 0 : v; }
    private static double safeDouble(Double v) { return v == null ? 0.0 : v; }

    private static List<MenuDTO> mapMenuBookings(List<Menu> list) {
        List<MenuDTO> out = new ArrayList<>();
        if (list != null) {
            for (Menu mb : list) {
                MenuDTO dto = new MenuDTO();
                dto.menuID = mb.getMenuID();
                dto.menuname = mb.getMenuname();
                dto.price = Double.parseDouble(mb.getPrice());
                out.add(dto);
            }
        }
        return out;
    }

    private static List<RoomDTO> mapRooms(List<Room> list) {
        List<RoomDTO> out = new ArrayList<>();
        if (list != null) {
            for (Room r : list) {
                RoomDTO dto = new RoomDTO();
                dto.roomID = r.getRoomID();
                dto.price = Double.parseDouble(r.getPrice());
                out.add(dto);
            }
        }
        return out;
    }

    private static List<CottageDTO> mapCottages(List<Cottage> list) {
        List<CottageDTO> out = new ArrayList<>();
        if (list != null) {
            for (Cottage c : list) {
                CottageDTO dto = new CottageDTO();
                dto.cottageID = c.getCottageID();
                dto.cottagename = c.getCottageName();
                dto.price = c.getPrice();
                out.add(dto);
            }
        }
        return out;
    }

    private static BillingDTO mapBilling(Billing b) {
        BillingDTO dto = new BillingDTO();
        dto.billingID = b.billingID;
        dto.bookingID = b.bookingID;
        dto.totalamount = b.totalAmount;
        dto.datebilled = b.dateBilled;
        dto.status = b.status;

        dto.payments = new ArrayList<>();
        if (b.payments != null) {
            for (Payment p : b.payments) {
                PaymentDTO paymentDTO = new PaymentDTO();
                paymentDTO.paymentID = p.paymentID;
                paymentDTO.totaltender = p.totalTender;
                paymentDTO.totalchange = p.totalChange;
                paymentDTO.datepayment = p.datePayment;
                paymentDTO.refNumber = p.refNumber;
                dto.payments.add(paymentDTO);
            }
        }
        return dto;
    }
}
