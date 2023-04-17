package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class privatepdfs extends Fragment {
    AlertDialog.Builder alertDialog;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference database,database2;
    MyAdapter myAdapter;
    ArrayList<PdfModel> list;
    private PdfSharedPreferences mPdfSharedPreferences;


    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        mPdfSharedPreferences = new PdfSharedPreferences(getContext());

        alertDialog = new AlertDialog.Builder(getContext());

        View view  = LayoutInflater.from(getContext()).inflate(R.layout.fragment1_layout,container,false);
        RecyclerView recyclerView =view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        FirebaseUser user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance().getReference("uploadPDF/"+user.getUid());

        list = (ArrayList<PdfModel>) mPdfSharedPreferences.getPdfList();
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
                alertDialog.setIcon(android.R.drawable.alert_dark_frame);
                alertDialog.setTitle("Delete PDF");
                alertDialog.setMessage("Do you want to Delete "+ list.get(position).getName());
                alertDialog.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                database2 = FirebaseDatabase.getInstance().getReference("uploadPDF/"+user.getUid()+"/"+list.get(position).getPdfId());
                                database2.removeValue();
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

        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PdfModel pdfModel = dataSnapshot.getValue(PdfModel.class);
                    list.add(pdfModel);
                }
//                }catch (Exception e){
//                    Toast.makeText(getApplicationContext(),"Error while get Data from server",Toast.LENGTH_LONG).show();
//                }
                mPdfSharedPreferences.savePdfList(list);
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
            FirebaseUser user = mAuth.getCurrentUser();
            Intent previousIntent = getActivity().getIntent();
            if(previousIntent.getExtras() == null){
                Intent pdf = new Intent(getActivity(), StoreSelect.class);
                pdf.putExtra("userUid", user.getUid());
                pdf.putExtra("userName",user.getDisplayName());
                pdf.putExtra("pdfName",pdfName);
                pdf.putExtra("pdfLink", url);
                pdf.putExtra("pdfId", pdfid);
                pdf.putExtra("numberPages",numberPages);
                startActivity(pdf);

            }else{
                Intent pdf = new Intent(getActivity(), PdfOptions.class);
                pdf.putExtra("userUid", user.getUid());
                pdf.putExtra("userName",user.getDisplayName());
                pdf.putExtra("pdfName",pdfName);
                pdf.putExtra("pdfLink", url);
                pdf.putExtra("pdfId", pdfid);
                pdf.putExtras(previousIntent.getExtras());
                pdf.putExtra("numberPages",numberPages);
                startActivity(pdf);
            }










        }catch (Exception e){
            System.out.println(e);
            Toast.makeText(getActivity(),"Not able to make request",Toast.LENGTH_SHORT).show();

        }


//
    }

    private void ShowToast(String pdfName,String url){
        Intent intent = new Intent(getActivity(), PdfView.class);
        intent.putExtra("name_key", pdfName);
        intent.putExtra("url_key",url);
        startActivity(intent);


    }
    private void deleteUnusedPDFs(List<PdfModel> currentList) {
        List<PdfModel> savedList = mPdfSharedPreferences.getPdfList();
        System.out.println("---------->");
        for (PdfModel savedPdf : savedList) {
            boolean pdfFound = false;
            for (PdfModel currentPdf : currentList) {
                if (savedPdf.getPdfId().equals(currentPdf.getPdfId())) {
                    pdfFound = true;
                    break;
                }
            }
            if (!pdfFound) {
                deletePDFFile(savedPdf.getName());
            }
        }
    }


    private void deletePDFFile(String fileName) {
        String folderName = "privatePdfs";
        System.out.println("---------->");
        File folder = new File(getContext().getFilesDir() + File.separator + folderName);
        File file = new File(folder, fileName);
        if (file.exists()) {
            file.delete();
        }
    }

}