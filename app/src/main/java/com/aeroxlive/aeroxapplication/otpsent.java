package com.aeroxlive.aeroxapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.aeroxlive.aeroxapplication.R;

public class otpsent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpsent);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent newIntent = new Intent(getApplicationContext(), otpVerifaction.class);
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    newIntent.putExtras(bundle);
                }
                startActivity(newIntent);
                finish();
            }
        }, 4000);



    }
}