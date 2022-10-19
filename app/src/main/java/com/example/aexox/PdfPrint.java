package com.example.aexox;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PdfPrint extends AppCompatActivity {
    TextView PdfName;
    Button Download;
    ImageView image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_print);
        Intent intent = getIntent();
        String  pdfName = intent.getStringExtra("name_key");
        String  pdfUrl = intent.getStringExtra("url_key");
//        Bundle intent1 = getIntent().getExtras();
//        String pdfName = intent1.getString("PdfName");

        PdfName = findViewById(R.id.PdfName);
        image = findViewById(R.id.pdfimage);
        image.setOnClickListener(view -> {
            Intent intent3 = new Intent(getApplicationContext(), PdfView.class);
            intent3.putExtra("name_key", pdfName);
            intent3.putExtra("url_key",pdfUrl);
            startActivity(intent3);

        });
        PdfName.setOnClickListener(view -> {
            Intent intent3 = new Intent(getApplicationContext(), PdfView.class);
            intent3.putExtra("name_key", pdfName);
            intent3.putExtra("url_key",pdfUrl);
            startActivity(intent3);
        });

        Download = findViewById(R.id.Pdfdownload);

        PdfName.setText(pdfName);
        Download.setOnClickListener(view -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl));
            startActivity(browserIntent);

        });


    }
}