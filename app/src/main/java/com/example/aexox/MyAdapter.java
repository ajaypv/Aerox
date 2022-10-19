package com.example.aexox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<PdfModel> list;
    public OnItemClickListener itemClickListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onPrintPdf(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener = listener;
    }




    public MyAdapter(Context context, ArrayList<PdfModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PdfModel pdfModel = list.get(position);
        holder.pdfName.setText(pdfModel.getName());
        holder.pdfUrl.setText(pdfModel.getUrl());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pdfName, pdfUrl;
        Button pdfPrint;

        public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.name);
            pdfUrl = itemView.findViewById(R.id.url);
            pdfPrint = itemView.findViewById(R.id.pdfprint);

            pdfPrint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onPrintPdf(position);
                        }
                    }

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });

        }
    }

}
