package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aeroxlive.aeroxapplication.R;
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


public class publicpdf extends Fragment {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<PdfModel> list;
    private RetrofitInterface retrofitInterface;
    private final String BASE_URL = "http://15.207.15.29";


    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = LayoutInflater.from(getContext()).inflate(R.layout.fragment1_layout,container,false);
        RecyclerView recyclerView =view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        database = FirebaseDatabase.getInstance().getReference("PublicPDF");
        list = new ArrayList<>();
        myAdapter = new MyAdapter(getContext(), list);
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

            @Override
            public void onPdfDelete(int position) {


            }
        });



        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PdfModel pdfModel = dataSnapshot.getValue(PdfModel.class);

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

        return view;


    }

    private void gotoPDf(String pdfName, String url,String pdfid,String numberPages){
        try {

            Toast.makeText(getActivity(),
                    "pdf sent", Toast.LENGTH_LONG).show();
            FirebaseUser user = mAuth.getCurrentUser();
            Intent pdf = new Intent(getActivity(),PdfOptions.class);
            pdf.putExtra("userUid", user.getUid());
            pdf.putExtra("pdfName",pdfName);
            pdf.putExtra("pdfLink", url);
            pdf.putExtra("pdfId", pdfid);
            pdf.putExtra("numberPages",numberPages);
            startActivity(pdf);

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
                        Toast.makeText(getActivity(),
                                "pdf sent", Toast.LENGTH_LONG).show();

                    } else if (response.code() == 400) {
                        Toast.makeText(getActivity(),
                                "server down", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });


        }catch (Exception e){


        }


//
    }

    private void ShowToast(String pdfName,String url){
        Intent intent = new Intent(getActivity(), PdfView.class);
        intent.putExtra("name_key", pdfName);
        intent.putExtra("url_key",url);


        startActivity(intent);

    }





}