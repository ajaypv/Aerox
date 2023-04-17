package com.aeroxlive.aeroxapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VPAdatapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();
    private final FragmentManager fragmentManager;

    public VPAdatapter(FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);

    }

    @Override
    public int getCount() {
        System.out.println("getcount");
        System.out.println();
       return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }

    public void clearFragments() {
        for (Fragment fragment : fragmentArrayList) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
        fragmentArrayList.clear();
        fragmentTitle.clear();
        notifyDataSetChanged();
    }
    public void removeFragment(int position) {
        fragmentArrayList.remove(position);
        fragmentTitle.remove(position);
        notifyDataSetChanged();
    }
}
