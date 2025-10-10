    package com.example.lantawmarbelmobileapp;

    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.viewpager2.widget.ViewPager2;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import com.google.android.material.tabs.TabLayout;
    import com.google.android.material.tabs.TabLayoutMediator;

    import java.time.LocalDate;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.List;

    public class MultiDateFragment extends Fragment {

        private BookingViewModel bookingViewModel;
        private DatePagerAdapter adapter;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_multi_date, container, false);

            bookingViewModel = new ViewModelProvider(requireActivity()).get(BookingViewModel.class);

            TabLayout tabLayout = view.findViewById(R.id.tabLayout);
            ViewPager2 viewPager = view.findViewById(R.id.viewPager);
            View dateContainer = view.findViewById(R.id.dateContainer);
            View emptyMessage = view.findViewById(R.id.emptyMessage);

            // ✅ Init adapter once
            adapter = new DatePagerAdapter(this, new ArrayList<>());
            viewPager.setAdapter(adapter);

            new TabLayoutMediator(tabLayout, viewPager, (tab, pos) -> {
                tab.setText(adapter.getDate(pos).format(DateTimeFormatter.ofPattern("MMM d")));
            }).attach();

            bookingViewModel.getBooking().observe(getViewLifecycleOwner(), booking -> {
                if (booking == null || booking.bookingStart == null || booking.bookingEnd == null) {
                    dateContainer.setVisibility(View.GONE);
                    emptyMessage.setVisibility(View.VISIBLE);
                    return;
                }

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

                    LocalDate start = LocalDate.parse(booking.bookingStart, formatter);
                    LocalDate end = LocalDate.parse(booking.bookingEnd, formatter);

                    if (end.isBefore(start)) throw new IllegalArgumentException();

                    List<LocalDate> dates = new ArrayList<>();
                    LocalDate current = start;
                    while (!current.isAfter(end)) {
                        dates.add(current);
                        current = current.plusDays(1);
                    }

                    if (dates.isEmpty()) {
                        dateContainer.setVisibility(View.GONE);
                        emptyMessage.setVisibility(View.VISIBLE);
                        return;
                    }

                    dateContainer.setVisibility(View.VISIBLE);
                    emptyMessage.setVisibility(View.GONE);

                    // ✅ Just update data, don’t recreate adapter
                    adapter.setDates(dates);

                } catch (Exception e) {
                    e.printStackTrace();
                    dateContainer.setVisibility(View.GONE);
                    emptyMessage.setVisibility(View.VISIBLE);
                }
            });

            return view;
        }
    }
