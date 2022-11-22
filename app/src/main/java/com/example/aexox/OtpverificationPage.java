package com.example.aexox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fredporciuncula.phonemoji.PhonemojiFlagTextView;
import com.fredporciuncula.phonemoji.PhonemojiTextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtpverificationPage extends AppCompatActivity {
    EditText e1;
    Button b1;
    TextView v1;
    PhonemojiTextInputEditText phoneNumber;


    private RetrofitInterface retrofitInterface;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String BASE_URL = "http://3.110.157.19";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otpverification_page);
        b1 = findViewById(R.id.button);
        phoneNumber= findViewById(R.id.phone_number);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        b1.setOnClickListener(view -> {

            String optNumber = phoneNumber.getText().toString();
            FirebaseUser user = mAuth.getCurrentUser();
            Log.d("---->",optNumber);
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(optNumber, "IN");

                boolean isValid = phoneUtil.isValidNumber(swissNumberProto);
                if(isValid){
                    Long WhatsappNumbner =swissNumberProto.getNationalNumber();
                    try {
                        HashMap<String, String> map = new HashMap<>();

                        map.put("mobileNumber", String.valueOf(WhatsappNumbner));
                        map.put("userUid", user.getUid());

                        Call<Void> call = retrofitInterface.executeSignup(map);

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                                if (response.code() == 200) {
                                    Toast.makeText(OtpverificationPage.this,
                                            "OTP sent Successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), otpsent.class);
                                    intent.putExtra("otpMobileNumber",String.valueOf(WhatsappNumbner) );
                                    startActivity(intent);
                                } else if (response.code() == 400) {
                                    Toast.makeText(OtpverificationPage.this,
                                            "server down", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(OtpverificationPage.this, t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    }catch (Exception e){


                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Enter a Valid Number",Toast.LENGTH_LONG).show();
                }
            } catch (NumberParseException e) {
                System.err.println("NumberParseException was thrown: " + e.toString());
            }





        });


    }

}