package com.example.lantawmarbelmobileapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import com.google.android.flexbox.FlexboxLayout;




public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 2;
    private static final int VIEW_TYPE_TYPING = 3;

    private List<Message> messages;
    // FIX 2: Changed from MainActivity to Go_To_Inquiry to match your activity name
    private Go_To_Inquiry context;

    public ChatAdapter(List<Message> messages, Go_To_Inquiry context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.isTyping()) {
            return VIEW_TYPE_TYPING;
        } else if (message.isUser()) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_BOT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_USER:
                View userView = inflater.inflate(R.layout.item_message_user, parent, false);
                return new UserMessageViewHolder(userView);
            case VIEW_TYPE_BOT:
                View botView = inflater.inflate(R.layout.item_message_bot, parent, false);
                return new BotMessageViewHolder(botView);
            case VIEW_TYPE_TYPING:
                View typingView = inflater.inflate(R.layout.item_typing_indicator, parent, false);
                return new TypingViewHolder(typingView);
            default:
                throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(message);
        } else if (holder instanceof BotMessageViewHolder) {
            ((BotMessageViewHolder) holder).bind(message);
        }
        // TypingViewHolder doesn't need binding as it's just an animation
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // ViewHolder for user messages
    class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
        }

        void bind(Message message) {
            messageText.setText(message.getContent());
        }
    }

    // ViewHolder for bot messages
    class BotMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        com.google.android.flexbox.FlexboxLayout quickRepliesContainer;

        BotMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            quickRepliesContainer = itemView.findViewById(R.id.quickRepliesContainer);
        }

        void bind(Message message) {
            messageText.setText(message.getContent());

            // Clear previous quick replies
            quickRepliesContainer.removeAllViews();

            // Add quick reply buttons
            if (message.getQuickReplies() != null && !message.getQuickReplies().isEmpty()) {
                quickRepliesContainer.setVisibility(View.VISIBLE);

                for (String reply : message.getQuickReplies()) {
                    MaterialButton quickReplyButton = new MaterialButton(context, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
                    quickReplyButton.setText(reply);
                    quickReplyButton.setTextSize(12);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 16, 8);
                    quickReplyButton.setLayoutParams(params);

                    quickReplyButton.setOnClickListener(v -> context.onQuickReplyClick(reply));

                    quickRepliesContainer.addView(quickReplyButton);
                }
            } else {
                quickRepliesContainer.setVisibility(View.GONE);
            }
        }
    }

    // ViewHolder for typing indicator
    class TypingViewHolder extends RecyclerView.ViewHolder {
        TypingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}