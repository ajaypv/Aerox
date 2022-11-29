package com.example.aexox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PdfPage extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<PdfModel> list;
    private RetrofitInterface retrofitInterface;
    private final String BASE_URL = "http://43.205.215.187";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);


        recyclerView = findViewById(R.id.userList);
        FirebaseUser user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("uploadPDF/"+user.getUid());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ShowToast(list.get(position).getName(),list.get(position).getUrl());
            }

            @Override
            public void onPrintPdf(int position) {
                System.out.print(list.get(position));
                gotoPDf(list.get(position).getName(),
                        list.get(position).getUrl(),
                        list.get(position).getPdfId(),
                        list.get(position).getNumberPages());

            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PdfModel pdfModel = dataSnapshot.getValue(PdfModel.class);
                        list.add(pdfModel);
                    }
//                }catch (Exception e){
//                    Toast.makeText(getApplicationContext(),"Error while get Data from server",Toast.LENGTH_LONG).show();
//                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void gotoPDf(String pdfName, String url,String pdfid,String numberPages){
        try {
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
                        Toast.makeText(PdfPage.this,
                                "pdf sent", Toast.LENGTH_LONG).show();

                    } else if (response.code() == 400) {
                        Toast.makeText(PdfPage.this,
                                "server down", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PdfPage.this, t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){


        }


//
    }

    private void ShowToast(String pdfName,String url){
        Intent intent = new Intent(getApplicationContext(), PdfPrint.class);
        intent.putExtra("name_key", pdfName);
        intent.putExtra("url_key",url);
        startActivity(intent);

    }
}