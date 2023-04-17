package com.aeroxlive.aeroxapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.MyViewHolder> {

    Context context;
    ArrayList<PrintRequest> list;
    public OnItemClickListener itemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onPrintPdf(int position);
        void onPdfDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener = listener;
    }


    public QueueAdapter(Context context, ArrayList<PrintRequest> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.queuelist,parent,false);
        return  new MyViewHolder(v,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            PrintRequest printRequest = list.get(position);
            holder.position.setText(String.valueOf(printRequest.getPosition()));
            holder.UserName.setText(printRequest.getUsersName());
            Glide.with(context)
                    .load(printRequest.getUserImage())
                    .dontTransform()
                    .into(holder.UserImage);


        }catch (Exception e){
            System.out.println(e);
        }
    }





    @Override
    public int getItemCount() {
        return list.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView UserName,position;
        ImageView UserImage;

        public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            UserImage = itemView.findViewById(R.id.userImage);
            UserName = itemView.findViewById(R.id.userName);
            position =itemView.findViewById(R.id.position);


        }
    }

}
