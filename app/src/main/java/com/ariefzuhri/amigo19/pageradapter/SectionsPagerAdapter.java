package com.ariefzuhri.amigo19.pageradapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.ariefzuhri.amigo19.fragment.EducationFragment;
import com.ariefzuhri.amigo19.fragment.HomeFragment;
import com.ariefzuhri.amigo19.fragment.PersonalFragment;
import com.ariefzuhri.amigo19.fragment.StatisticFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @SuppressWarnings("deprecation")
    public SectionsPagerAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new HomeFragment();
            case 1: return new PersonalFragment();
            case 2: return new StatisticFragment();
            case 3: return new EducationFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
