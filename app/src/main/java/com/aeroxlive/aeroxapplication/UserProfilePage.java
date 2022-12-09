package com.aeroxlive.aeroxapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aeroxlive.aeroxapplication.R;
import com.squareup.picasso.Picasso;

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