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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<PdfModel> list;
    public OnItemClickListener itemClickListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
        void onPrintPdf(int position);
        void onPdfDelete(int position);
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

        try {

            PdfModel pdfModel = list.get(position);

            holder.pdfName.setText(pdfModel.getName());
            holder.pageCount.setText(pdfModel.getNumberPages());
            String timestamp = pdfModel.getDate();
            SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.getDefault());
            try {
                Date date = sdf1.parse(timestamp);
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                String formattedDate = outputDateFormat.format(date);
                holder.pdfdate.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Get the first page of the PDF as a bitmap

            String pdfDownloadurl = pdfModel.getUrl();

            String folderName = "privatePdfs" + File.separator ;
            File file = new File(context.getExternalFilesDir(folderName),  pdfModel.getName());

            if (file.exists()) {
                // PDF is alrFFeady downloaded, load bitmap from file
                try {
                    new AsyncTask<File, Void, Bitmap>() {

                        @Override
                        protected Bitmap doInBackground(File... files) {
                            PdfRenderer renderer = null;
                            try {
                                renderer = new PdfRenderer(ParcelFileDescriptor.open(files[0], ParcelFileDescriptor.MODE_READ_ONLY));
                            } catch (IOException e) {

                            }
                            try {
                                PdfRenderer.Page page = renderer.openPage(0);
                                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                                page.close();
                                renderer.close();
                                return bitmap;
                            } catch (Exception e) {
                                return null;

                            }

                        }

                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            if(bitmap == null){
                                holder.imagepdf.setImageResource(R.drawable.pdfe);
                            }
                            holder.imagepdf.setImageBitmap(bitmap);
                        }
                    }.execute(file);
                }catch (Exception e){

                }
            } else {

                holder.imagepdf.setImageResource(R.drawable.pdfe);
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }





    @Override
    public int getItemCount() {
        return list.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView pdfName, pdfdate,pageCount;
        Button pdfPrint;
        ImageView pdfDelete,imagepdf;

        public MyViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.name);
            pdfdate = itemView.findViewById(R.id.pdfDate);
            pdfPrint = itemView.findViewById(R.id.pdfprint);
            pdfDelete = itemView.findViewById(R.id.pdfDelete);
            imagepdf = itemView.findViewById(R.id.imagepdf);
            pageCount= itemView.findViewById(R.id.pageCount);

            pdfDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onPdfDelete(position);
                        }
                    }

                }
            });

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
