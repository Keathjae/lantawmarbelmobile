package com.example.lantawmarbelmobileapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SingleDatePagerAdapter extends FragmentStateAdapter {

    private final String selectedDate;

    public SingleDatePagerAdapter(@NonNull Fragment fragment, String date) {
        super(fragment);
        this.selectedDate = date;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return BookingRoomFragment.newInstance(selectedDate);
            case 1: return BookCottageFragment.newInstance(selectedDate);
            case 2: return BookMenuFragment.newInstance(selectedDate);
            default: return BookingRoomFragment.newInstance(selectedDate);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    // ✅ Stable IDs so fragments are not re-created unnecessarily
    @Override
    public long getItemId(int position) {
        return (selectedDate + "-" + position).hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        for (int i = 0; i < getItemCount(); i++) {
            if ((selectedDate + "-" + i).hashCode() == itemId) return true;
        }
        return false;
    }
}

