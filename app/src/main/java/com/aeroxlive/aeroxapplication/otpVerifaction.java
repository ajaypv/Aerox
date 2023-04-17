package com.aeroxlive.aeroxapplication;

import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.aeroxlive.aeroxapplication.R;
import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.apache.fontbox.ttf.KerningTable.TAG;

public class otpVerifaction extends AppCompatActivity {

    OtpTextView otpTextView;
    TextView errors;
    ProgressDialog progress;
    private RetrofitInterface retrofitInterface;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final String BASE_URL = "http://15.207.15.29";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verifaction);
        progress = new ProgressDialog(this);
//        otpTextView = findViewById(R.id.otpnum);
        PinView pinView = findViewById(R.id.pin_view);

        errors = findViewById(R.id.errors);

      pinView.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
              if(s.toString().length()==6){
                  try {

                      FirebaseUser user = mAuth.getCurrentUser();
                      Intent intent3 = getIntent();
                      String otpNumber = intent3.getStringExtra("phoneNumber");
                      String Verification_id = intent3.getStringExtra("Verification_id");
                      PhoneAuthCredential credential = PhoneAuthProvider.getCredential(Verification_id, s.toString());
                      signInWithPhoneAuthCredential(credential);
                      progress.setTitle("Loading");
                      progress.setMessage("Wait while loading...");
                      progress.setCancelable(false);
                      progress.show();

                  }catch (Exception e){
                      Toast.makeText(getApplicationContext(), "Phone number update failed: " ,Toast.LENGTH_SHORT);
                  }

              }
              Log.d(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
          }

          @Override
          public void afterTextChanged(Editable editable) {

          }
      });




    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.getCurrentUser().updatePhoneNumber(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Phone number updated successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            progress.dismiss();

                        } else {
                            String errorMessage = task.getException().getMessage();
                            if (errorMessage.contains("This credential is already associated with a different user account.")) {
                                Toast.makeText(getApplicationContext(), "This phone number is already associated with another account", Toast.LENGTH_LONG).show();
                                errors.setText("This phone number is already associated with another account, Use different Number");
                                progress.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Phone number update failed: " + errorMessage, Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                        }

                    }

                });
    }
}