package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.aeroxlive.aeroxapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements StoresFragment.OnItemSelectedListener{

    private ppfragemnts ppFragment;
    private Uploading uploadingFragment;

    private ProfileFragment profileFragment;
    private PaymentHistoryFragment paymentHistoryFragment;
    private StoresFragment storesFragment;
    private BottomNavigationView bottomNavigationView;
    private boolean doubleBackToExitPressedOnce = false;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, onBording.class));
            finish();
        }else if (user.getPhoneNumber().length() < 10){
            startActivity(new Intent(MainActivity.this, OtpverificationPage.class));
            finish();
        }else{


            bottomNavigationView = (BottomNavigationView) findViewById(R.id.navbarK);
            ppFragment = new ppfragemnts();
            uploadingFragment = new Uploading();
            storesFragment = new StoresFragment();
            paymentHistoryFragment = new PaymentHistoryFragment();
            profileFragment = new ProfileFragment();



            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, ppFragment);
            fragmentTransaction.commit();


            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId())
                    {
                        case R.id.home:
                            replaceFragment(ppFragment);
                            MenuButtonVibrate();
                            return true;

                        case R.id.stores:
                            replaceFragment(storesFragment);
                            MenuButtonVibrate();
                            return true;

                        case R.id.upload:
                            replaceFragment(uploadingFragment);
                            MenuButtonVibrate();
                            return true;

                        case R.id.history:
                            replaceFragment(paymentHistoryFragment);
                            MenuButtonVibrate();
                            return true;

                        case R.id.profile:
                            replaceFragment(profileFragment);
                            MenuButtonVibrate();
                            return true;

                    }
                    return false;
                }
            });



        }


    }

    private void MenuButtonVibrate(){
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        if (currentFragment instanceof ppfragemnts) {
            // Close the app
            finishAffinity();
            System.exit(0);

        } else {
            // Navigate back to the home fragment
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            replaceFragment(ppFragment);
            bottomNavigationView.setSelectedItemId(R.id.home);
        }


        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, onBording.class));
            finish();
        }else if(user.getPhoneNumber().length() < 10){
            startActivity(new Intent(getApplicationContext(), OtpverificationPage.class));
            finish();
        }
    }


    @Override
    public void onItemSelected(StoreModel storeModel) {
        if(storeModel.getPower().equals("Offline")){
            Toast.makeText(this, "Currently Store is Offline", Toast.LENGTH_SHORT).show();
        }else{
            if(storeModel != null){
                Intent StoreData = new Intent(getApplicationContext(),MainActivity.class);
                StoreData.putExtra("StoreName",storeModel.getShopName());
                StoreData.putExtra("StoreId",storeModel.getStoreId());
                StoreData.putExtra("storeColorPrice", storeModel.getColorPrice());
                StoreData.putExtra("storeBlackAndWhitePrice", storeModel.getBwPrice());

                startActivity(StoreData);
            }else{
                Toast.makeText(this, "Error in the selected Store", Toast.LENGTH_SHORT).show();
            }

        }


    }
}