package com.example.lantawmarbelmobileapp;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ApiService {
    @GET("rooms")
    Call<List<Room>> getRooms();

    @GET("amenities")
    Call<List<Amenity>> getAmenities();

    @GET("cottages")
    Call<List<Cottage>> getCottages();

    @GET("menus")
    Call<List<Menu>> getMenus();

    @POST("login")
    Call<LoginResponse> login(@Body Map<String, String> body);

    @Multipart
    @POST("signup")
    Call<SignUpResponse> signupMultipart(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("cpassword") RequestBody cpassword,
            @Part("firstname") RequestBody firstname,
            @Part("lastname") RequestBody lastname,
            @Part("email") RequestBody email,
            @Part("mobilenum") RequestBody mobilenum,
            @Part("gender") RequestBody gender,
            @Part("birthday") RequestBody birthday,
            @Part MultipartBody.Part avatar,
            @Part MultipartBody.Part validID
    );
    @GET("bookings/guest/{guestID}")
    Call<List<Booking>> getBookingsForGuest(@Path("guestID") int guestID);
    @POST("bookings")
    Call<BookingRequest> storeBooking(@Body BookingRequest booking);
    @GET("chats")
    Call<List<Chat>> getChats();

    @POST("chats")
    Call<Chat> sendChat(@Body Chat chat);

    @GET("chats/guest/{guestID}")
    Call<List<Chat>> getChatsForGuest(@Path("guestID") int guestID);
}