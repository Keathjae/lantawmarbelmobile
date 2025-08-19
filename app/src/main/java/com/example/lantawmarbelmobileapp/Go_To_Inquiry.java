package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout; // ✅ Added import for quick replies layout

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Go_To_Inquiry extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageButton sendButton, attachmentButton;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private Handler handler;
    private Map<String, ResponseData> responses;

    private ImageButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_inquiry); // Ensure this matches your actual layout

        initViews();
        setupRecyclerView();
        setupListeners();
        initializeResponses();
        addWelcomeMessage();
    }

    private void initViews() {
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        attachmentButton = findViewById(R.id.attachmentButton);
        handler = new Handler();
    }

    private void setupRecyclerView() {
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    private void setupListeners() {
        sendButton.setOnClickListener(v -> sendMessage());

        attachmentButton.setOnClickListener(v ->
                Toast.makeText(this, "Photo attachment feature coming soon! 📸", Toast.LENGTH_SHORT).show()
        );

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendButton.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        messageEditText.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });
    }


    private void initializeResponses() {
        responses = new HashMap<>();

        List<String> roomRatesReplies = new ArrayList<>();
        roomRatesReplies.add("Standard Room");
        roomRatesReplies.add("Deluxe Room");
        roomRatesReplies.add("Premium Suite");
        roomRatesReplies.add("Package Deals");

        responses.put("Room Rates", new ResponseData(
                "Here are our current room rates:\n\n🏨 Standard Room: ₱2,500/night\n🏖️ Deluxe Room: ₱3,500/night\n🌊 Premium Suite: ₱5,000/night\n\nAll rates include breakfast for 2 and access to all amenities. Would you like more details about any specific room type?",
                roomRatesReplies
        ));

        List<String> amenitiesReplies = new ArrayList<>();
        amenitiesReplies.add("Swimming Pool");
        amenitiesReplies.add("Restaurant");
        amenitiesReplies.add("Beach Access");
        amenitiesReplies.add("Activities");

        responses.put("Amenities", new ResponseData(
                "Our resort offers amazing amenities:\n\n🏊 Swimming Pool\n🏖️ Private Beach Access\n🍽️ Restaurant & Bar\n🎵 Karaoke\n🎮 Game Room\n🚗 Free Parking\n📶 Free WiFi\n\nWhat would you like to know more about?",
                amenitiesReplies
        ));

        List<String> bookingReplies = new ArrayList<>();
        bookingReplies.add("This Weekend");
        bookingReplies.add("Next Month");
        bookingReplies.add("Holiday Season");
        bookingReplies.add("Call Now");

        responses.put("Booking", new ResponseData(
                "I'd be happy to help you with your booking! 📅\n\nTo make a reservation, I'll need:\n• Check-in & Check-out dates\n• Number of guests\n• Room preference\n• Special requests\n\nYou can also call us directly at +63 912 345 6789. When would you like to stay with us?",
                bookingReplies
        ));

        List<String> locationReplies = new ArrayList<>();
        locationReplies.add("Get Directions");
        locationReplies.add("Transportation");
        locationReplies.add("Nearby Attractions");
        locationReplies.add("Airport Transfer");

        responses.put("Location", new ResponseData(
                "📍 Lantaw Marbel Resort is located in:\n\nBarangay Marbel, Koronadal City\nSouth Cotabato, Philippines\n\n🚗 About 15 minutes from Koronadal City Center\n✈️ 30 minutes from General Santos Airport\n\nWould you like directions or transportation options?",
                locationReplies
        ));
    }

    private void addWelcomeMessage() {
        List<String> welcomeReplies = new ArrayList<>();
        welcomeReplies.add("💰 Room Rates");
        welcomeReplies.add("🏊 Amenities");
        welcomeReplies.add("📅 Booking");
        welcomeReplies.add("📍 Location");

        Message welcomeMessage = new Message(
                "Welcome to Lantaw Marbel Resort! 🌴 I'm here to help you with your inquiry. How can I assist you today?",
                false,
                System.currentTimeMillis(),
                welcomeReplies
        );

        messageList.add(welcomeMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (messageText.isEmpty()) return;

        Message userMessage = new Message(messageText, true, System.currentTimeMillis(), null);
        messageList.add(userMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();

        messageEditText.setText("");

        showTypingIndicator();

        handler.postDelayed(() -> {
            hideTypingIndicator();
            generateBotResponse(messageText);
        }, 1500);
    }

    private void showTypingIndicator() {
        Message typingMessage = new Message("", false, System.currentTimeMillis(), null);
        typingMessage.setTyping(true);
        messageList.add(typingMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void hideTypingIndicator() {
        if (!messageList.isEmpty() && messageList.get(messageList.size() - 1).isTyping()) {
            messageList.remove(messageList.size() - 1);
            chatAdapter.notifyItemRemoved(messageList.size());
        }
    }

    private void generateBotResponse(String userMessage) {
        String response = "Thank you for your message! Our team will get back to you shortly. For immediate assistance, please call us at +63 912 345 6789.";
        List<String> quickReplies = new ArrayList<>();
        quickReplies.add("Room Rates");
        quickReplies.add("Amenities");
        quickReplies.add("Booking");
        quickReplies.add("Location");

        String lowerMessage = userMessage.toLowerCase();

        if (lowerMessage.contains("rate") || lowerMessage.contains("price") || lowerMessage.contains("cost")) {
            ResponseData responseData = responses.get("Room Rates");
            if (responseData != null) {
                response = responseData.getMessage();
                quickReplies = responseData.getQuickReplies();
            }
        } else if (lowerMessage.contains("amenity") || lowerMessage.contains("facility") || lowerMessage.contains("pool")) {
            ResponseData responseData = responses.get("Amenities");
            if (responseData != null) {
                response = responseData.getMessage();
                quickReplies = responseData.getQuickReplies();
            }
        } else if (lowerMessage.contains("book") || lowerMessage.contains("reserve") || lowerMessage.contains("available")) {
            ResponseData responseData = responses.get("Booking");
            if (responseData != null) {
                response = responseData.getMessage();
                quickReplies = responseData.getQuickReplies();
            }
        } else if (lowerMessage.contains("location") || lowerMessage.contains("address") || lowerMessage.contains("direction")) {
            ResponseData responseData = responses.get("Location");
            if (responseData != null) {
                response = responseData.getMessage();
                quickReplies = responseData.getQuickReplies();
            }
        }

        Message botMessage = new Message(response, false, System.currentTimeMillis(), quickReplies);
        messageList.add(botMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    public void onQuickReplyClick(String reply) {
        messageEditText.setText(reply);
        sendMessage();
    }

    private void scrollToBottom() {
        handler.post(() -> {
            if (messageList.size() > 0) {
                chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
            }
        });
    }

    private static class ResponseData {
        private final String message;
        private final List<String> quickReplies;

        public ResponseData(String message, List<String> quickReplies) {
            this.message = message;
            this.quickReplies = quickReplies;
        }

        public String getMessage() {
            return message;
        }

        public List<String> getQuickReplies() {
            return quickReplies;
        }
    }
}
