package com.example.aexox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PdfPage extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<PdfModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        recyclerView = findViewById(R.id.userList);
        FirebaseUser user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("uploadPDF/"+user.getUid());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                gotoPDf(list.get(position).getName(),list.get(position).getUrl());

            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    PdfModel pdfModel = dataSnapshot.getValue(PdfModel.class);
                    list.add(pdfModel);
                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void gotoPDf(String pdfName, String url){
        Intent intent = new Intent(getApplicationContext(), PdfPrint.class);
        intent.putExtra("name_key", pdfName);
        intent.putExtra("url_key",url);
        startActivity(intent);
//
    }

    private void ShowToast(String pdfName,String url){
        Intent intent = new Intent(getApplicationContext(), PdfPrint.class);
        intent.putExtra("name_key", pdfName);
        intent.putExtra("url_key",url);
        startActivity(intent);

    }
}