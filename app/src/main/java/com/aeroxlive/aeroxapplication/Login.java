package com.aeroxlive.aeroxapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aeroxlive.aeroxapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextInputEditText etLoginEmail;
    TextInputEditText etLoginPassword;
    TextView tvRegisterHere;
    Button btnLogin;
    LinearLayout layo;

    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        btnLogin = findViewById(R.id.btnLogin);
        layo = findViewById(R.id.layoutlogin);


        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            final VibrationEffect vibrationEffect3;

            // this type of vibration requires API 29
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

                // create vibrator effect with the constant EFFECT_DOUBLE_CLICK
                vibrationEffect3 = VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);

                // it is safe to cancel other vibrations currently taking place
                vibrator.cancel();

                vibrator.vibrate(vibrationEffect3);
            }
            loginUser();

        });
        tvRegisterHere.setOnClickListener(view ->{
            startActivity(new Intent(Login.this, SignUp.class));
            final VibrationEffect vibrationEffect3;

            // this type of vibration requires API 29
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

                // create vibrator effect with the constant EFFECT_DOUBLE_CLICK
                vibrationEffect3 = VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);

                // it is safe to cancel other vibrations currently taking place
                vibrator.cancel();

                vibrator.vibrate(vibrationEffect3);
            }
        });
    }
    private void loginUser(){
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Login.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, MainActivity.class));
                    }else{
                        Toast.makeText(Login.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        final VibrationEffect vibrationEffect3;
                        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        // this type of vibration requires API 29
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                            // create vibrator effect with the constant EFFECT_DOUBLE_CLICK
                            vibrationEffect3 = VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK);
                            // it is safe to cancel other vibrations currently taking place
                            vibrator.cancel();
                            vibrator.vibrate(vibrationEffect3);
                        }
                        // this type of vibration requires API 29


                    }
                }
            });
        }
    }

}