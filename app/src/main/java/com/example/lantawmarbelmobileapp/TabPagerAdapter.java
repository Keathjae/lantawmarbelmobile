package com.example.lantawmarbelmobileapp; // change to your package name

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabPagerAdapter extends FragmentStateAdapter {

    public TabPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new RoomsFragment();
            case 1:
                return new AmenityFragment();
            case 2:
                return new CottageFragment();
            case 3:
             return new MenuFragment();
            default:
                return new RoomsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // number of tabs
    }
}
