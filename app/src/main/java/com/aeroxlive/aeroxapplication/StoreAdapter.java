package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aeroxlive.aeroxapplication.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;


public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {

    Context context;
    ArrayList<StoreModel> list;
    public OnItemClickListener itemClickListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
        void StoreSelect(int position);
        void onPdfDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        itemClickListener = listener;
    }


    public StoreAdapter(Context context, ArrayList<StoreModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.shop_item,parent,false);
        return  new MyViewHolder(v,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        StoreModel storeModel = list.get(position);
        holder.StoreName.setText(storeModel.getShopName());
        holder.colorPrintCost.setText(storeModel.getColorPrice());
        long waitingTimeInMillis = Long.parseLong(storeModel.getWaitingTime());
        long waitingTimeInSeconds = waitingTimeInMillis / 1000;

        if (waitingTimeInSeconds < 60) {
            holder.waiting_time.setText(waitingTimeInSeconds + " sec");
        } else {
            long waitingTimeInMinutes = waitingTimeInSeconds / 60;
            holder.waiting_time.setText(waitingTimeInMinutes + " min");
        }

        holder.StoreStatus.setText(storeModel.getPower());

        holder.PeopleCount.setText(( String.valueOf(storeModel.getQueueCount())));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView StoreName, whiteandblackprintcost, colorPrintCost,waiting_time,StoreStatus,PeopleCount;
        Button StoreSelect;
        LinearLayout moreinfo;
        ImageView pdfDelete;

        public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            StoreName = itemView.findViewById(R.id.shopName);
            whiteandblackprintcost = itemView.findViewById(R.id.bwprice);
            colorPrintCost = itemView.findViewById(R.id.colorprice);
            StoreSelect = itemView.findViewById(R.id.shopselectbutton);
            waiting_time = itemView.findViewById(R.id.waiting_time);
            StoreStatus = itemView.findViewById(R.id.StoreStatus);
            PeopleCount=itemView.findViewById(R.id.PeopleCount);
            moreinfo = itemView.findViewById(R.id.moreinfo);


            moreinfo.setOnClickListener(new View.OnClickListener() {
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

            StoreSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.StoreSelect(position);
                        }
                    }

                }
            });


        }
    }

}
