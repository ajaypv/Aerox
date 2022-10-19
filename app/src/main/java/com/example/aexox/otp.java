package com.example.aexox;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class

otp extends AppCompatActivity {
    EditText e1 ,e2;
    Button b1 , b2 ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        e1 = findViewById(R.id.otpNumber);
        e2 = findViewById(R.id.otp);
        b1 = findViewById(R.id.m1);
        b2 = findViewById(R.id.button2);


        b1.setOnClickListener(view -> {
           String mon =  e1.getText().toString();
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet("http://13.235.165.123/?mobileNumber="+mon);
            try {
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity);
                Log.d("ddd===========",content);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });



    }
}