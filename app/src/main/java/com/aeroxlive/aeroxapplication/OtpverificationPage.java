package com.aeroxlive.aeroxapplication;

import android.app.ProgressDialog;
import android.content.SyncAdapterType;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aeroxlive.aeroxapplication.R;
import com.fredporciuncula.phonemoji.PhonemojiTextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

    ProgressDialog progress;



    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otpverification_page);
        b1 = findViewById(R.id.button);
        phoneNumber= findViewById(R.id.phone_number);
        progress = new ProgressDialog(this);





        b1.setOnClickListener(view -> {
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false);
            progress.show();

            String optNumber = phoneNumber.getText().toString();
            FirebaseUser user = mAuth.getCurrentUser();
            Log.d("---->",optNumber);
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(optNumber, "IN");

                boolean isValid = phoneUtil.isValidNumber(swissNumberProto);
                if(isValid){
                    Long WhatsappNumbner =swissNumberProto.getNationalNumber();
                    System.out.println(WhatsappNumbner);
                    try {
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91"+ String.valueOf(WhatsappNumbner),        // The user's phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                this,               // Activity (for callback binding)
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


                                    // Called when the verification is successfully sent
                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(s, forceResendingToken);
                                       Intent verify =  new Intent(getApplicationContext(),otpVerifaction.class);
                                       verify.putExtra("Verification_id", s);
                                       verify.putExtra("phoneNumber", String.valueOf(WhatsappNumbner));
                                       startActivity(verify);
                                        progress.dismiss();


                                    }

                                    // Called when the verification fails
                                    @Override
                                    public void onVerificationFailed(FirebaseException e) {
                                        Toast.makeText(getApplicationContext(),"Error User Different Phone Number", Toast.LENGTH_LONG);
                                        progress.dismiss();
                                    }

                                    // Called when the verification is successfully completed
                                    @Override
                                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                        // You don't need to manually enter the code, so this method should not be called
                                        progress.dismiss();
                                    }
                                });




                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Enter a Valid Number",Toast.LENGTH_LONG).show();

                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Enter a Valid Number",Toast.LENGTH_LONG).show();
                }
            } catch (NumberParseException e) {
                Toast.makeText(getApplicationContext(),"Error ",Toast.LENGTH_LONG).show();
                System.err.println("NumberParseException was thrown: " + e.toString());
            }





        });


    }

}