package com.example.lantawmarbelmobileapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.DatePickerDialog;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

public class BookingDateFragment extends Fragment {

    private BookingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_date, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        EditText child = view.findViewById(R.id.childGuestInput);
        EditText adult = view.findViewById(R.id.adultGuestInput);
        Button nextBtn = view.findViewById(R.id.nextButton);
        TextView startDateInput = view.findViewById(R.id.startDateInput);
        TextView endDateInput = view.findViewById(R.id.endDateInput);

        // 🔄 Observe LiveData (auto-update if data changes)
        viewModel.getBookingStart().observe(getViewLifecycleOwner(), value -> {
            if (value != null) startDateInput.setText(value);
        });
        viewModel.getBookingEnd().observe(getViewLifecycleOwner(), value -> {
            if (value != null) endDateInput.setText(value);
        });
        viewModel.getChildGuest().observe(getViewLifecycleOwner(), value -> {
            if (value != null) child.setText(String.valueOf(value));
        });
        viewModel.getAdultGuest().observe(getViewLifecycleOwner(), value -> {
            if (value != null) adult.setText(String.valueOf(value));
        });

        // 📅 Show date picker
        startDateInput.setOnClickListener(v -> showDatePickerDialog(startDateInput));
        endDateInput.setOnClickListener(v -> showDatePickerDialog(endDateInput));

        // 👉 Save values into ViewModel before moving to next tab
        nextBtn.setOnClickListener(v -> {
            viewModel.setBookingStart(startDateInput.getText().toString());
            viewModel.setBookingEnd(endDateInput.getText().toString());

            try {
                viewModel.setChildGuest(Integer.parseInt(child.getText().toString()));
            } catch (NumberFormatException e) {
                viewModel.setChildGuest(0);
            }

            try {
                viewModel.setAdultGuest(Integer.parseInt(adult.getText().toString()));
            } catch (NumberFormatException e) {
                viewModel.setAdultGuest(0);
            }

            ViewPager2 pager = getActivity().findViewById(R.id.viewPager);
            pager.setCurrentItem(1);
        });


        return view;
    }

    private void showDatePickerDialog(TextView targetView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    targetView.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
}
