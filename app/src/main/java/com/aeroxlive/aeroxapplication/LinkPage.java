package com.aeroxlive.aeroxapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aeroxlive.aeroxapplication.R;

public class LinkPage extends AppCompatActivity {
    Button Whatsapp , mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_page);
        Whatsapp = findViewById(R.id.whatsapplink);
        mail = findViewById(R.id.maillink);
        Whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),OtpverificationPage.class));
                finish();
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LinkPage.this, "fixing the mail link soon you can access", Toast.LENGTH_SHORT).show();
            }
        });
    }
}