package com.aeroxlive.aeroxapplication;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.aeroxlive.aeroxapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class printstatus extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView textView2,textView,textView3;
    long waitingTimeInMillis = 0;
    boolean hasValidWaitTime = false;

    ArrayList<PrintRequest> list;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printstatus);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView =  findViewById(R.id.textView);
        Intent intent = getIntent();
        String transactionKey = intent.getStringExtra("transactionKey");
        String storeId = intent.getStringExtra("storeId");
        FirebaseUser user = mAuth.getCurrentUser();
        CircularProgressBar circularProgressBar = findViewById(R.id.circularProgressBar);
        CircularProgressBar circularProgressBar2 = findViewById(R.id.circularProgressBar2);
        circularProgressBar.setProgress(6f);

        // Call displayNotification() method to create the initial notification
        NotificationHelper.displayNotification(getApplicationContext(), "Your File Sent to Server", "Downloading file...");



        // Get the user's ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Get a reference to the "print requests" node in the Firebase Realtime Database
        DatabaseReference  printRequestsRef =  FirebaseDatabase.getInstance().getReference("Stores/"+storeId+"/queue/");
        list = new ArrayList<>();
        QueueAdapter adapter = new QueueAdapter(getApplicationContext(), list);

        printRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve all the print requests and sort them by timestamp
                List<PrintRequest> printRequests = new ArrayList<>();
                for (DataSnapshot printRequestSnapshot : dataSnapshot.getChildren()) {
                    String userId = printRequestSnapshot.child("userId").getValue(String.class);
                    String documentName = printRequestSnapshot.child("payment_txnRef").getValue(String.class);
                    long timestamp = printRequestSnapshot.child("timestamp").getValue(Long.class);
                    String UserImage = printRequestSnapshot.child("UserImage").getValue(String.class);
                    String usersName = printRequestSnapshot.child("usersName").getValue(String.class);

                    PrintRequest printRequest = new PrintRequest(userId, documentName, timestamp,usersName,UserImage);
                    printRequests.add(printRequest);
                }
                Collections.sort(printRequests, new Comparator<PrintRequest>() {
                    @Override
                    public int compare(PrintRequest r1, PrintRequest r2) {
                        return Long.compare(r1.getTimestamp(), r2.getTimestamp());
                    }
                });

                // Find the user's print request in the sorted list

                list.clear();
                for (int i = 0; i < printRequests.size(); i++) {

                    PrintRequest printRequest = printRequests.get(i);
                    printRequest.position = i + 1;
                    list.add(printRequest);
                }

                int position = -1;
                for (int i = 0; i < printRequests.size(); i++) {
                    if (printRequests.get(i).getUserId().equals(transactionKey)) {
                        position = i;
                        break;
                    }
                }

                // Display the user's position in the queue to the user
                if (position == -1) {
                    // User has no print requests in the queue
                    // Display appropriate message to the user
                } else {
                   textView.setText(String.valueOf(position+1));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });




        RecyclerView recyclerView = findViewById(R.id.queueRecycleView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));








        DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference("Users/"+user.getUid() + "/transactions/"+transactionKey);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
                String waitTimeStr = dataSnapshot.child("waitTime").getValue(String.class);
                String printStatus = dataSnapshot.child("printStatus").getValue(String.class);
                if(printStatus.equals("downloading")){
                    circularProgressBar.setProgress(50f);
                    textView3.setText("Your File Downloading");
                    // Vibrate the device for 500 milliseconds
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);

                } else if (printStatus.equals("downloaded")) {
                    circularProgressBar.setProgress(100f);
                    textView3.setText("Your File Downloaded");
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                }

                // Convert the waiting time from seconds to milliseconds

                if (waitTimeStr != null &&  Long.parseLong(waitTimeStr) >= 1) {
                    // Convert the waiting time from seconds to milliseconds
                    waitingTimeInMillis = Long.parseLong(waitTimeStr);
                    System.out.println(waitingTimeInMillis);


                    // Update the text view to display the initial waiting time in seconds
                    textView2.setText(String.format("%d", waitingTimeInMillis / 1000));


                    hasValidWaitTime = true;



                    new CountDownTimer(waitingTimeInMillis, 1000) {

                        public void onTick(long millisUntilFinished) {
                            long secondsRemaining = millisUntilFinished / 1000;
                            String timrr;

                            if (secondsRemaining >= 60) {
                                // Convert remaining time to minutes and display it
                                long minutesRemaining = secondsRemaining / 60;
                                textView2.setText(String.format("%d mins", minutesRemaining+1));
                                timrr = String.format("%d mins", minutesRemaining+1);

                            } else {
                                // Display remaining time in seconds
                                textView2.setText(String.format("%d sec", secondsRemaining));
                                timrr = String.format("%d sec", secondsRemaining);
                                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(50);
                            }
                            int progress = (int) ((waitingTimeInMillis - millisUntilFinished) * 100 / waitingTimeInMillis);
                            circularProgressBar2.setProgress(progress);
                            NotificationHelper.updateProgress(getApplicationContext(), progress,timrr);


                        }


                        public void onFinish() {
                            // Move this code inside onFinish():
                            textView2.setText("0");
                            circularProgressBar2.setProgress(100);
                            adapter.notifyDataSetChanged();

                            NotificationHelper.displayDownloadCompleteNotification(getApplicationContext());
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(200);
                        }
                    }.start();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
            }
        };

        databaseRef.addValueEventListener(valueEventListener);
    }
    @Override
    public void onBackPressed() {
        // Call cancel() on the call object to cancel the ongoing network request


        // Create an intent to navigate the user back to the main page
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        // Call finish() to remove the current activity from the back stack
        finish();
    }

}
