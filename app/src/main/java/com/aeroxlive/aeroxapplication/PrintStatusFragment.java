package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class PrintStatusFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView textView2, textView3;
    private long waitingTimeInMillis = 0;
    private boolean hasValidWaitTime = false;
    private DatabaseReference databaseRef;
    private ValueEventListener valueEventListener;
    private CircularProgressBar circularProgressBar, circularProgressBar2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String transactionKey = requireArguments().getString("transactionKey");
        databaseRef = FirebaseDatabase.getInstance().getReference("Users/" + user.getUid() + "/transactions/" + transactionKey);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String waitTimeStr = dataSnapshot.child("waitTime").getValue(String.class);
                String printStatus = dataSnapshot.child("printStatus").getValue(String.class);
                if (printStatus != null) {
                    switch (printStatus) {
                        case "downloading":
                            circularProgressBar.setProgress(50f);
                            textView3.setText("Your File Downloading");
                            break;
                        case "downloaded":
                            circularProgressBar.setProgress(100f);
                            textView3.setText("Your File Downloaded");
                            break;
                        default:
                            circularProgressBar.setProgress(6f);
                            textView3.setText("");
                    }
                }

                if (waitTimeStr != null) {
                    waitingTimeInMillis = Long.parseLong(waitTimeStr);
                    textView2.setText(String.format("%d", waitingTimeInMillis / 1000));
                    hasValidWaitTime = true;

                    new CountDownTimer(waitingTimeInMillis, 1000) {
                        public void onTick(long millisUntilFinished) {
                            long secondsRemaining = millisUntilFinished / 1000;
                            if (secondsRemaining >= 60) {
                                long minutesRemaining = secondsRemaining / 60;
                                textView2.setText(String.format("%d mins", minutesRemaining + 1));
                            } else {
                                textView2.setText(String.format("%d sec", secondsRemaining));
                            }
                            int progress = (int) ((waitingTimeInMillis - millisUntilFinished) * 100 / waitingTimeInMillis);
                            circularProgressBar2.setProgress(progress);
                        }

                        public void onFinish() {
                            textView2.setText("0");
                            circularProgressBar2.setProgress(100);
                        }
                    }.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        };
        databaseRef.addValueEventListener(valueEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseRef.removeEventListener(valueEventListener);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_print_status, container, false);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);

        return view;
    }
}

