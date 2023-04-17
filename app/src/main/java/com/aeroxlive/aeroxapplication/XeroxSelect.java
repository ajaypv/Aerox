package com.aeroxlive.aeroxapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aeroxlive.aeroxapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class XeroxSelect extends AppCompatActivity {
    TextView time,status;
    AlertDialog.Builder alertDialog;
    Button submit;
    DatabaseReference database , database2;
    RetrofitInterface retrofitInterface;
    String BASE_URL = "https://jzwwm6lnek.execute-api.ap-south-1.amazonaws.com/";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xerox_select);
        time = findViewById(R.id.printtime);
        status = findViewById(R.id.xstatus);
        submit = findViewById(R.id.xsubmit);
        alertDialog = new AlertDialog.Builder(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        database = FirebaseDatabase.getInstance().getReference("XeroxMachines/aeroxclinetTest");
        database2 = FirebaseDatabase.getInstance().getReference("XeroxMachinesdata/aeroxclinetTest");
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String power = snapshot.child("power").getValue(String.class);
                status.setText(power);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database2.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Long pages = snapshot.child("time").getValue(Long.class);
                time.setText(String.valueOf(pages)+"Sec");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.setIcon(android.R.drawable.alert_dark_frame);
                alertDialog.setTitle("Continue to print");
                alertDialog.setMessage("Do you want to print ??");
                alertDialog.setPositiveButton(
                        "Print",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i1 = getIntent();
                                String userUid =  i1.getStringExtra("userUid");
                                String pdfName =  i1.getStringExtra("pdfName");
                                String pdfLink =  i1.getStringExtra("pdfLink");
                                String pdfId = i1.getStringExtra("pdfId");
                                String numberPages =  i1.getStringExtra("numberPages");
                                Log.d("--->",pdfId);

                                printpaper(pdfName,pdfLink,pdfId,numberPages);
                                dialog.cancel();
                            }
                        });

                alertDialog.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();



            }
        });


    }

    public void printpaper(String pdfName,String url , String  pdfid, String numberPages){


        FirebaseUser user = mAuth.getCurrentUser();
        HashMap<String, String> map = new HashMap<>();
        map.put("userUid", user.getUid());
        map.put("pdfName",pdfName);
        map.put("pdfLink", url);
        map.put("pdfId", pdfid);
        map.put("numberPages",numberPages);




        Call<Void> call = retrofitInterface.executePrint(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Intent ii =  new Intent(getApplicationContext(),printstatus.class);
                    ii.putExtra("pdfidd",pdfid);
                    ii.putExtra("userid",user.getUid());
                    startActivity(ii);



                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(),
                            "server down", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "server down", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });



    }



}