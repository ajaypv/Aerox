package com.example.aexox;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentPage extends AppCompatActivity implements PaymentStatusListener {
    TextView fee,fileName,pagesv;



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

                // on below line we are validating our text field.

                    makePayment(pay_amount+".0", "7899993939@axl", "ajaypv", "pay","48304934t304");

            }
        });
    }

    private void makePayment(String amount, String upi, String name, String desc, String transactionId) {
        // on below line we are calling an easy payment method and passing
        // all parameters to it such as upi id,name, description and others.
        final EasyUpiPayment easyUpiPayment = new EasyUpiPayment.Builder()
                .with(this)
                // on below line we are adding upi id.
                .setPayeeVpa(upi)
                // on below line we are setting name to which we are making payment.
                .setPayeeName(name)
                // on below line we are passing transaction id.
                .setTransactionId(transactionId)
                // on below line we are passing transaction ref id.
                .setTransactionRefId(transactionId)
                // on below line we are adding description to payment.
                .setDescription(desc)
                // on below line we are passing amount which is being paid.
                .setAmount(amount)
                // on below line we are calling a build method to build this ui.
                .build();
        // on below line we are calling a start
        // payment method to start a payment.
        easyUpiPayment.startPayment();
        // on below line we are calling a set payment
        // status listener method to call other payment methods.
        easyUpiPayment.setPaymentStatusListener(this);
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        // on below line we are getting details about transaction when completed.
        String transcDetails = transactionDetails.getStatus().toString() + "\n" + "Transaction ID : " + transactionDetails.getTransactionId();

    }

    @Override
    public void onTransactionSuccess() {
        // this method is called when transaction is successful and we are displaying a toast message.
        Toast.makeText(this, "Transaction successfully completed..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionSubmitted() {
        // this method is called when transaction is done
        // but it may be successful or failure.
        Log.e("TAG", "TRANSACTION SUBMIT");
    }

    @Override
    public void onTransactionFailed() {
        // this method is called when transaction is failure.
        Toast.makeText(this, "Failed to complete transaction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTransactionCancelled() {
        // this method is called when transaction is cancelled.
        Toast.makeText(this, "Transaction cancelled..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAppNotFound() {
        // this method is called when the users device is not having any app installed for making payment.
        Toast.makeText(this, "No app found for making transaction..", Toast.LENGTH_SHORT).show();
    }
}
