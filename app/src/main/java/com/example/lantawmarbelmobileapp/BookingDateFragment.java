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
    TextView startDateInput;
    TextView endDateInput;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_date, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

        EditText child = view.findViewById(R.id.childGuestInput);
        EditText adult = view.findViewById(R.id.adultGuestInput);
        Button nextBtn = view.findViewById(R.id.nextButton);
        startDateInput = view.findViewById(R.id.startDateInput);
        endDateInput = view.findViewById(R.id.endDateInput);

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
        startDateInput.setOnClickListener(v -> showDatePickerDialog(startDateInput, true));
        endDateInput.setOnClickListener(v -> showDatePickerDialog(endDateInput, false));


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

    private void showDatePickerDialog(TextView targetView, boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();

        // For start date: default to tomorrow if empty
        if (isStartDate) {
            calendar.add(Calendar.DAY_OF_MONTH, 1); // tomorrow
            if (targetView.getText().toString().isEmpty()) {
                targetView.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
            } else {
                String[] parts = targetView.getText().toString().split("-");
                calendar.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
            }
        } else {
            // For end date: default to start date or today if empty
            String startDateStr = startDateInput.getText().toString();
            if (!startDateStr.isEmpty()) {
                String[] parts = startDateStr.split("-");
                calendar.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    targetView.setText(selectedDate);

                    // Ensure end date >= start date
                    if (isStartDate) {
                        String endStr = endDateInput.getText().toString();
                        if (!endStr.isEmpty()) {
                            String[] endParts = endStr.split("-");
                            Calendar endCal = Calendar.getInstance();
                            endCal.set(Integer.parseInt(endParts[0]), Integer.parseInt(endParts[1]) - 1, Integer.parseInt(endParts[2]));
                            if (endCal.before(calendar)) {
                                endDateInput.setText(selectedDate); // adjust end date
                            }
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set minimum selectable date
        if (isStartDate) {
            Calendar minStart = Calendar.getInstance();
            minStart.add(Calendar.DAY_OF_MONTH, 1); // tomorrow
            datePickerDialog.getDatePicker().setMinDate(minStart.getTimeInMillis());
        } else {
            // End date cannot be before start date
            String startStr = startDateInput.getText().toString();
            if (!startStr.isEmpty()) {
                String[] startParts = startStr.split("-");
                Calendar startCal = Calendar.getInstance();
                startCal.set(Integer.parseInt(startParts[0]), Integer.parseInt(startParts[1]) - 1, Integer.parseInt(startParts[2]));
                datePickerDialog.getDatePicker().setMinDate(startCal.getTimeInMillis());
            }
        }

        datePickerDialog.show();
    }

}
