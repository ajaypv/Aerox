package com.example.aexox;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class otpVerifaction extends AppCompatActivity {

    OtpTextView otpTextView;
    private RetrofitInterface retrofitInterface;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final String BASE_URL = "http://3.110.157.19";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp_verifaction);
        otpTextView = findViewById(R.id.otpnum);

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);

                try {
                    HashMap<String, String> map = new HashMap<>();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent3 = getIntent();
                    String otpNumber = intent3.getStringExtra("otpMobileNumber");
                    Log.d("------>",otpNumber);
                    map.put("mobileNumber",otpNumber);
                    map.put("otp", otp);
                    map.put("userUid",user.getUid());

                    Call<ProfileModal> call = retrofitInterface.executeLogin(map);

                    call.enqueue(new Callback<ProfileModal>() {
                        @Override
                        public void onResponse(Call<ProfileModal> call, Response<ProfileModal> response) {

                            if (response.code() == 200) {

                                ProfileModal profileModal = response.body();
                                String profilepic =  profileModal.profilepic;
                                String profileName = profileModal.profilename;


                                Toast.makeText(otpVerifaction.this,
                                        "verification Done", Toast.LENGTH_LONG).show();
                                Intent i1 = new Intent(otpVerifaction.this,UserProfilePage.class);
                                i1.putExtra("profilepic",profilepic);
                                i1.putExtra("profileName",profileName);
                                startActivity(i1);
                            } else{
                                otpTextView.showError();
                                Toast.makeText(otpVerifaction.this,
                                        "enter a Correct Otp", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ProfileModal> call, Throwable t) {
                            Toast.makeText(otpVerifaction.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }catch (Exception e){


                }
            }
        });


    }
}