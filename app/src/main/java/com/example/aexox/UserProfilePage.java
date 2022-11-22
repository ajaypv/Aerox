package com.example.aexox;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UserProfilePage extends AppCompatActivity {
    ImageView profilePic;
    EditText profileName;
    Button submit,signin;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i1 = getIntent();
        String url = i1.getStringExtra("profilepic");
        String name = i1.getStringExtra("profileName");
        setContentView(R.layout.activity_user_profile_page);
        profilePic = findViewById(R.id.profilepic);
        profileName = findViewById(R.id.userName);
        submit = findViewById(R.id.submitprofile);

        Picasso.get().load(url).into(profilePic);
        profileName.setText(name);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }


}