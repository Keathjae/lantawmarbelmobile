package com.example.lantawmarbelmobileapp;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ApiService {
    @GET("rooms")
    Call<List<Room>> getRooms();
    @GET("rooms/available")
    Call<List<Room>> getAvailableRooms(@Query("date") String date);
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
    Call<List<BookingRequest>> getBookingsForGuest(@Path("guestID") int guestID);
    @GET("notifications/{user_id}")
    Call<List<NotificationModel>> getNotifications(@Path("user_id") int userId);

    @GET("bookings/{id}")
    Call<BookingRequest> getBookingbyId(@Path("id") int id);
    @GET("bookingsForEdit/{id}")
    Call<BookingDTO> getBookingForEditbyId(@Path("id") int id);
    @POST("bookings")
    Call<BookingRequest> storeBooking(@Body BookingDTO booking);
    @PUT("bookings/{id}")
    Call<BookingRequest> updateBooking(@Path("id") int id, @Body BookingDTO booking);


    @GET("chats")
    Call<List<Chat>> getChats();

    @POST("save-fcm-token")
    Call<Void> sendFcmToken(
            @Header("Authorization") String authHeader,
            @Body Map<String, Object> body
    );


    @POST("chats")
    Call<Chat> sendChat(@Body Chat chat);

    @GET("chats/guest/{guestID}")
    Call<List<Chat>> getChatsForGuest(@Path("guestID") int guestID);

    @POST("send-otp")
    Call<ApiResponse> sendOTP(@Body Map<String, String> body);

    @POST("verify-otp")
    Call<ApiResponse> verifyOTP(@Body Map<String, String> body);

    @POST("reset-password")
    Call<ApiResponse> resetPassword(@Body Map<String, String> body);

    @POST("feedback")
    Call<FeedbackResponse> sendFeedback(@Body Map<String, Object> body);

    @GET("feedback/{guestID}")
    Call<FeedbackListResponse> getFeedbackList(@Path("guestID") int guestID);

    @GET("qrcodeByGuest/{guestID}")
    Call<ApiResponse<List<QRCode>>> getQRCodesByGuest(@Path("guestID") int guestID);
}
