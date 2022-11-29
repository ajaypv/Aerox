package com.example.aexox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class printStatus extends AppCompatActivity {
    DatabaseReference database;
    TextView printstatus;
    int images[] = {
            R.raw.downloading,
            R.raw.downloadedicon,
            R.raw.printingicon

    };

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_status);

        LottieAnimationView lottieAnimationView  = findViewById(R.id.printStatus);
        printstatus = findViewById(R.id.printsatusview);
        Intent pay = getIntent();
        String useruid = pay.getStringExtra("userid");
        String pdfid = pay.getStringExtra("pdfidd");
        database = FirebaseDatabase.getInstance().getReference("uploadPDF/"+useruid +"/" + pdfid);
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String staus =  snapshot.child("currentStatus").getValue(String.class);
                printstatus.setText(staus);
                if(staus.equals("printing")){
                    lottieAnimationView.setAnimation(images[2]);
                }else{
                    lottieAnimationView.setAnimation(images[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



}