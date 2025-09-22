package com.example.lantawmarbelmobileapp;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static BookingRequest fromViewModel(BookingViewModel vm) {
        BookingRequest dto = new BookingRequest();

        dto.bookingID = safeInt(vm.getBookingID().getValue());
        dto.guestID = safeInt(vm.getGuestID().getValue());
        dto.childGuest = safeInt(vm.getChildGuest().getValue());
        dto.adultGuest = safeInt(vm.getAdultGuest().getValue());
        dto.guestamount=dto.adultGuest+dto.childGuest;
        dto.totalPrice = safeDouble(vm.getTotalPrice().getValue());
        dto.bookingCreated = vm.getBookingCreated().getValue();
        dto.bookingStart = vm.getBookingStart().getValue();
        dto.bookingEnd = vm.getBookingEnd().getValue();
        dto.status = vm.getStatus().getValue();

        // Guest
        if (vm.getGuest().getValue() != null) {
            BookingViewModel.Guest g = vm.getGuest().getValue();
            GuestDTO guestDTO = new GuestDTO();
            guestDTO.guestID = g.guestID;
            guestDTO.firstname = g.firstname;
            guestDTO.lastname = g.lastname;
            guestDTO.email = g.email;
            dto.guest = guestDTO;
        }

        // Collections
        dto.amenityBook = mapAmenities(vm.getAmenityBook().getValue());
        dto.roomBookings = mapRooms(vm.getRoomBookings().getValue());
        dto.cottageBookings = mapCottages(vm.getCottageBookings().getValue());
        dto.billing = mapBillings(vm.getBilling().getValue());
        dto.menuBookings = mapMenus(vm.getMenuBookings().getValue());

        return dto;
    }

    private static int safeInt(Integer v) { return v == null ? 0 : v; }
    private static double safeDouble(Double v) { return v == null ? 0.0 : v; }

    private static List<AmenityDTO> mapAmenities(List<Amenity> list) {
        List<AmenityDTO> out = new ArrayList<>();
        if (list != null) {
            for (Amenity a : list) {
                AmenityDTO dto = new AmenityDTO();
                dto.amenityID = a.getAmenityID();
                dto.amenityname = a.getAmenityname();
                dto.price = a.getAdultprice();
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

    private static List<BillingDTO> mapBillings(List<BookingViewModel.Billing> list) {
        List<BillingDTO> out = new ArrayList<>();
        if (list != null) {
            for (BookingViewModel.Billing b : list) {
                BillingDTO dto = new BillingDTO();
                dto.bookingID = b.bookingID;
                dto.totalamount = b.totalamount;
                dto.status = b.status;
                out.add(dto);
            }
        }
        return out;
    }

    private static List<MenuDTO> mapMenus(List<Menu> list) {
        List<MenuDTO> out = new ArrayList<>();
        if (list != null) {
            for (Menu m : list) {
                MenuDTO dto = new MenuDTO();
                dto.menuID = m.getMenuID();
                dto.menuname = m.getMenuname();
                dto.price = Double.parseDouble(m.getPrice());
                out.add(dto);
            }
        }
        return out;
    }
}

