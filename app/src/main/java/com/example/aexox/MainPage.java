package com.example.aexox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {

    private TabLayout tableLayout;
    private ViewPager viewPager;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_page);
        tableLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpage);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navbarK);

        tableLayout.setupWithViewPager(viewPager);
        VPAdatapter vpAdatapter = new VPAdatapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdatapter.addFragment(new publicpdf(), "Public Pdfs");
        vpAdatapter.addFragment(new privatepdfs(), "private Pdfs");
        viewPager.setAdapter(vpAdatapter);


        FloatingActionButton fab = findViewById(R.id.upload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UploadPdf.class));
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.link:
                        startActivity(new Intent(getApplicationContext(),LinkPage.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.person:
                        startActivity(new Intent(getApplicationContext(),UserPage.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


    }
}