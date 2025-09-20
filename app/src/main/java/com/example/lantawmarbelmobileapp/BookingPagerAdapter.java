package com.example.lantawmarbelmobileapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BookingPagerAdapter extends FragmentStateAdapter {

    public BookingPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new BookingDateFragment();
            case 1: return new BookingRoomFragment();
            case 2: return new BookingAmenitiesFragment();
            case 3: return new BookCottageFragment();
            case 4: return new BookMenuFragment();
            case 5: return new PaymentFragment();
            default: return new BookingDateFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
