package com.aeroxlive.aeroxapplication;

import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class RecentFilesFragment extends Fragment {

    MyAdapter myAdapter;
    ArrayList<PdfModel> list;



    public RecentFilesFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = LayoutInflater.from(getContext()).inflate(R.layout.fragment1_layout,container,false);
        RecyclerView recyclerView =view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        list = new ArrayList<>();

        File externalStorage = Environment.getRootDirectory();
        File[] files = externalStorage.listFiles();
        System.out.println(files+"------------------------------------------------>");





        for (File file : files) {
            System.out.println("------->");
            if (file.getName().endsWith(".pdf")) {
                PdfModel pdf = new PdfModel();
                pdf.name = file.getName();
                System.out.println("------->"+file.getName());
                pdf.url = file.getAbsolutePath();
                pdf.size = String.format("%.2f MB", file.length() / (1024.0f * 1024.0f)); // convert bytes to MB
                pdf.date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).format(new Date(file.lastModified()));
                pdf.numberPages = "2";
               list.add(pdf);
            }
        }

        myAdapter = new MyAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);

        myAdapter.notifyDataSetChanged();



        return  view;
    }
}