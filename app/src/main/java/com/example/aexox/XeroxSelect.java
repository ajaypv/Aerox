package com.example.aexox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class XeroxSelect extends AppCompatActivity {
    TextView time,status;
    Button submit;
     DatabaseReference mDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xerox_select);
        time = findViewById(R.id.printtime);
        status = findViewById(R.id.xstatus);
        submit = findViewById(R.id.xsubmit);
        mDatabase = FirebaseDatabase.getInstance().getReference();



        mDatabase = FirebaseDatabase.getInstance().getReference("");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int carid = dataSnapshot.child("user").child("carid").getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        };


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PaymentPage.class));
            }
        });

    }

}