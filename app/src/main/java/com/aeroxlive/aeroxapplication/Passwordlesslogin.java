package com.aeroxlive.aeroxapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aeroxlive.aeroxapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Passwordlesslogin extends AppCompatActivity {
    TextInputEditText etRegEmail;

    TextView tvLoginHere;
    Button btnRegister;


    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordlesslogin);
        etRegEmail = findViewById(R.id.maillinkid);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.sendmail);

        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser2();
            }
        });
    }
    private void createUser2() {
        ActionCodeSettings actionCodeSettings;
        String emailID = etRegEmail.getText().toString();
        if (TextUtils.isEmpty(emailID)) {
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("Mysharedmailprrivate", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();

            actionCodeSettings = ActionCodeSettings.newBuilder()
                    .setUrl("https://aexox.page.link")
                    .setHandleCodeInApp(true)
                    .setAndroidPackageName("com.aeroxlive.aeroxapplication", false, null)
                    .build();
            mAuth.sendSignInLinkToEmail(emailID, actionCodeSettings)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "onComplete: ");
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                myEdit.putString("verimailid", emailID);
                                myEdit.commit();
                                startActivity(new Intent(getApplicationContext(), Sentmaillink.class));
                            } else {
                                Objects.requireNonNull(task.getException()).printStackTrace();
                                etRegEmail.setError(task.getException().toString());
                                etRegEmail.requestFocus();
                            }
                        }
                    });

        }
    }
}