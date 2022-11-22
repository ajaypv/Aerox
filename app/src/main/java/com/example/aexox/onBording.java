package com.example.aexox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class onBording extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    Button setstarted,signup;

    SliderAdatapter sliderAdatapter;
    TextView[] dots;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_bording);


        viewPager = findViewById(R.id.viewPager2);
        dotsLayout = findViewById(R.id.dots);
        setstarted = findViewById(R.id.get_started_button);
        signup = findViewById(R.id.signuser);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });
        setstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        sliderAdatapter = new SliderAdatapter(this);
        viewPager.setAdapter(sliderAdatapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    private void addDots(int pos) {
        dots = new TextView[3];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);


        }
        if(dots.length>0){
            dots[pos].setTextColor(getResources().getColor(R.color.purple_500));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}