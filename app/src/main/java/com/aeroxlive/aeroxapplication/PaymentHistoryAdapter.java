package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aeroxlive.aeroxapplication.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;


public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<PaymentModel> list;
    public OnItemClickListener itemClickListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
        void StoreSelect(int position);
        void onPdfDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener = listener;
    }


    public PaymentHistoryAdapter(Context context, ArrayList<PaymentModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.history,parent,false);
        return  new MyViewHolder(v,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PaymentModel paymentModel = list.get(position);
        holder.shopName.setText(paymentModel.getShopName());

        holder.printAmount.setText(paymentModel.getAmount());
        try {
            String timestamp = paymentModel.getPaymentDate();
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.getDefault());
            Date date = sdf1.parse(timestamp);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String formattedDate = outputDateFormat.format(date);
            holder.PaymentDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ("success".equals(paymentModel.getPaymentStatus().toLowerCase())) {
            holder.Status.setText("successful");
            holder.Status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), com.jpardogo.android.googleprogressbar.library.R.color.green));

        } else {
            holder.Status.setText("Failed");
            holder.Status.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), in.aabhasjindal.otptextview.R.color.red));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView shopName ,printAmount,PaymentDate,Status;
        Button StoreSelect;
        ImageView pdfDelete;

        public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shopName);
            printAmount =itemView.findViewById(R.id.printAmount);
            PaymentDate =itemView.findViewById(R.id.PaymentDate);
            Status = itemView.findViewById(R.id.Status);



        }
    }

}
