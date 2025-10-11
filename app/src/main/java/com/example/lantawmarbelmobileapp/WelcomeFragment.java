package com.example.lantawmarbelmobileapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WelcomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        // Initialize and set click listener for Facebook link
        TextView fbLink = view.findViewById(R.id.tvFbLink);
        fbLink.setOnClickListener(v -> {
            String fbUrl = "https://www.facebook.com/profile.php?id=100063575334668"; // Replace with your actual page URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbUrl));
            startActivity(intent);
        });

        return view;
    }
}
