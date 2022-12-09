package com.aeroxlive.aeroxapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.aeroxlive.aeroxapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class printStatus extends AppCompatActivity {
    DatabaseReference database;
    TextView printstatus;
    ProgressBar progressBar;
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
        progressBar = findViewById(R.id.progress_bar);
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
                printstatus.setText("your Pdf is "+staus);
                if(staus.equals("printing")){
                    lottieAnimationView.setAnimation(images[2]);

                }else if(staus.equals("downloaded")){
                    Long waitingTime =  snapshot.child("waitingTime").getValue(Long.class);
                    Log.d("--->",String.valueOf(waitingTime));
                    final int[] num = {(int) (100 / waitingTime)};
                    final int[] no = {0};
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if ( no[0] <= 100) {
                                progressBar.setProgress(no[0]);
                                Log.d("--->",String.valueOf(num[0]));
                               no[0] = no[0] + num[0];
                                handler.postDelayed(this, 2000);
                            } else {
                                handler.removeCallbacks(this);
                            }
                        }
                    }, 200);

                }
                else if(staus.equals("done")){
                    lottieAnimationView.setAnimation(images[1]);
                }else{
                    lottieAnimationView.setAnimation(images[0]);
                }
                lottieAnimationView.playAnimation();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



}