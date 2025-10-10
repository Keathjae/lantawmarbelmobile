package com.example.lantawmarbelmobileapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.time.LocalDate;
import java.util.List;
public class DatePagerAdapter extends FragmentStateAdapter {
    private List<LocalDate> dateList;

    public DatePagerAdapter(@NonNull Fragment fragment, List<LocalDate> dates) {
        super(fragment);
        this.dateList = dates;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        LocalDate date = dateList.get(position);
        return SingleDateFragment.newInstance(date.toString());
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public LocalDate getDate(int position) {
        return dateList.get(position);
    }

    // ✅ update dates dynamically
    public void setDates(List<LocalDate> dates) {
        this.dateList = dates;
        notifyDataSetChanged();
    }
}
