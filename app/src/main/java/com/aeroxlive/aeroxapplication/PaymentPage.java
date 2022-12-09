package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aeroxlive.aeroxapplication.R;


public class PaymentPage extends AppCompatActivity{
    TextView fee,fileName,pagesv;
    private static final int TEZ_REQUEST_CODE = 123;

    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";



    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        Intent pay = getIntent();
        String filename = pay.getStringExtra("pdfName");
        String pages = pay.getStringExtra("numberPages");
        fileName = findViewById(R.id.FileName);
        pagesv = findViewById(R.id.pagecount);
        fileName.setText(filename);
        pagesv.setText(pages);
        fee = findViewById(R.id.paymentfee);

        int pp = Integer.parseInt(pages);
        int amount = pp*5;
        String pay_amount =  Integer.toString(amount);
        fee.setText("RS : " +pay_amount );




         Button makePaymentBtn = findViewById(R.id.pay);
            makePaymentBtn.setText("Pay: "+pay_amount);

        // on below line we are getting date and then we are setting this date as transaction id.

        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", "9036700273@ybl")
                                .appendQueryParameter("pn", "Aerox")
                                .appendQueryParameter("mc", "BCR2DN4TQDELBUAM")
                                .appendQueryParameter("tr", "789459434345")
                                .appendQueryParameter("tn", "Aerox payment for your Xerox")
                                .appendQueryParameter("am", "3")
                                .appendQueryParameter("cu", "INR")
                                .appendQueryParameter("url", "https://ajaypv.me/contact.html")
                                .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setPackage(GOOGLE_TEZ_PACKAGE_NAME);
                startActivityForResult(intent, TEZ_REQUEST_CODE);



            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEZ_REQUEST_CODE) {
            // Process based on the data in response.
            Log.d("result", data.getStringExtra("Status"));
        }
    }

}
