package com.example.lantawmarbelmobileapp;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LantawMarbelPrefs";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_GUEST_ID = "guestID";
    private int getGuestID() {
        return sharedPreferences.getInt(KEY_GUEST_ID, -1);
    }
    RecyclerView recyclerView;
    ChatAdapter adapter;
    EditText inputMessage;
    Button sendButton;
    ApiService apiService;
    List<Chat> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerView);
        inputMessage = findViewById(R.id.inputMessage);
        sendButton = findViewById(R.id.sendButton);

        adapter = new ChatAdapter(chatList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize the class-level apiService
        apiService = ApiClient.getClient().create(ApiService.class);

        loadChats();

        sendButton.setOnClickListener(v -> {
            String message = inputMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                Chat chat = new Chat();
                chat.setChat(message);
                chat.setGuestID(getGuestID());
                chat.setReply("guest");
                chat.setStatus("Unread");
                chat.setStaffID(1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                chat.setDatesent(sdf.format(new Date()));

                apiService.sendChat(chat).enqueue(new Callback<Chat>() {
                    @Override
                    public void onResponse(Call<Chat> call, Response<Chat> response) {
                        if (response.isSuccessful()) {
                            chatList.add(response.body());
                            adapter.notifyItemInserted(chatList.size() - 1);
                            inputMessage.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<Chat> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "Failed to send", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void loadChats() {
        apiService.getChatsForGuest(getGuestID()).enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatList.clear();
                    chatList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Failed to load chats", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
