package com.aeroxlive.aeroxapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class ppfragemnts extends Fragment {
    private TabLayout tableLayout;
    private ViewPager viewPager;
    VPAdatapter vpAdatapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ppfragemnts, container, false);


        tableLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpage);
        tableLayout.setupWithViewPager(viewPager);


        vpAdatapter = new VPAdatapter(getChildFragmentManager());
        vpAdatapter.addFragment(new privatepdfs(), "Your Files");
        vpAdatapter.addFragment(new publicpdf(), "Open Files");
        viewPager.setAdapter(vpAdatapter);

        return view;
    }
}
