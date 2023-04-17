package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aeroxlive.aeroxapplication.R;

import java.util.ArrayList;
import java.util.List;

import static org.apache.fontbox.ttf.IndexToLocationTable.TAG;


public class PaymentPage extends AppCompatActivity{
    TextView printColorDisplay,SelectedColorPrice,print_sides_display,TotalPageforPages,print_sheet_side_price;
    TextView print_sheet_side_total_pages_price,print_sheets_total_price;
    TextView bindingDisplay,displayBindingColor,print_binding_price;

    TextView print_total_price;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    String PHONEPE_PACKAGE_NAME = "com.phonepe.app";
    String PAYTM_PACKAGE_NAME = "net.one97.paytm";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    ImageView PrintSides;
    Button button_print_pay;
    private static final int TEZ_REQUEST_CODE = 789999;

    private int printFinalTotalPrice =0;

    private static final String GOOGLE_TEZ_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    Bundle extras ;

    Intent intentPayment;



    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        extras = getIntent().getExtras();
        intentPayment = new Intent(getApplicationContext(),PaymenyCheck.class);
        intentPayment.putExtras(extras);
        intentPayment.putExtra("printFinalTotalPrice","10");

        String SelectPrintColour = extras.getString("selected_color");
        String Store_storeBlackAndWhitePrice = extras.getString("storeBlackAndWhitePrice");
        String storeColorPrice = extras.getString("storeColorPrice");
        String Selected_side = extras.getString("print_sides");
        String all_pages_selected = extras.getString("all_pages_selected");
        String customPagesCount = extras.getString("customPagesCount");
        String numberPages = extras.getString("numberPages");
        String selected_binding = extras.getString("selected_binding");
        String bindingColor = extras.getString("bindingColor");
        //


        //
        printColorDisplay = findViewById(R.id.printColor_name);
        SelectedColorPrice = findViewById(R.id.Color_print_price);
        PrintSides = findViewById(R.id.image_sides);
        print_sides_display = findViewById(R.id.print_sides_display);
        TotalPageforPages = findViewById(R.id.TotalPageforPages);
        print_sheet_side_price = findViewById(R.id.print_sheet_side_price);
        print_sheet_side_total_pages_price = findViewById(R.id.print_sheet_side_total_pages_price);
        print_sheets_total_price = findViewById(R.id.print_sheets_total_price);
        bindingDisplay = findViewById(R.id.bindingDisplay);
        displayBindingColor = findViewById(R.id.displayBindingColor);
        print_binding_price = findViewById(R.id.print_binding_price);
        print_total_price = findViewById(R.id.print_total_price);
        button_print_pay = findViewById(R.id.button_print_pay);


        printColorDisplay.setText(SelectPrintColour);
        if (SelectPrintColour.equals("Color")) {
            SelectedColorPrice.setText(storeColorPrice);
            printFinalTotalPrice = 10;
        } else {
            SelectedColorPrice.setText(Store_storeBlackAndWhitePrice);
            printFinalTotalPrice = 2;
        }

        if (Selected_side.equals("Single-Side")) {
            PrintSides.setImageResource(R.drawable.baseline_filter_1_);
            print_sides_display.setText(Selected_side);

        } else {
            PrintSides.setImageResource(R.drawable.baseline_filter_2_24);
            print_sides_display.setText(Selected_side);
        }
        if (all_pages_selected.equals("Custom")) {
            TotalPageforPages.setText(customPagesCount);
            int totalPagesPrice = (int) (Integer.parseInt(customPagesCount) * 1.5);
            print_sheet_side_total_pages_price.setText(String.valueOf(totalPagesPrice));
            print_sheets_total_price.setText(String.valueOf(totalPagesPrice));
            printFinalTotalPrice = printFinalTotalPrice * totalPagesPrice;

        } else {
            TotalPageforPages.setText(numberPages);
            int totalPagesPrice = (int) (Integer.parseInt(numberPages) * 1.5);
            print_sheet_side_total_pages_price.setText(String.valueOf(totalPagesPrice));
            print_sheets_total_price.setText(String.valueOf(totalPagesPrice));
            printFinalTotalPrice = printFinalTotalPrice * Integer.parseInt(numberPages);


        }
        bindingDisplay.setText(selected_binding);
        displayBindingColor.setText(bindingColor);
        if (selected_binding.equals("No binding")) {
            print_binding_price.setText("0");

        } else {
            print_binding_price.setText("15");
            printFinalTotalPrice = printFinalTotalPrice + 15;

        }
        print_total_price.setText(String.valueOf(printFinalTotalPrice));

        intentPayment.putExtra("printFinalTotalPrice",String.valueOf(printFinalTotalPrice));





        button_print_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (!isConnected) {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();

                }else{

                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", "95400765000019@cnrb")
                                .appendQueryParameter("pn", "AEROX")
                                .appendQueryParameter("mc", "5411")
                                .appendQueryParameter("tn", "Aerox bill")
                                .appendQueryParameter("am", "1500")
                                .appendQueryParameter("cu", "INR")
                                .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);


                PackageManager pm = getPackageManager();
                List<ResolveInfo> availableApps = pm.queryIntentActivities(intent, 0);

                List<Intent> targetedIntents = new ArrayList<>();

                for (ResolveInfo availableApp : availableApps) {
                    String packageName = availableApp.activityInfo.packageName;
                    Intent targetedIntent = new Intent(intent);
                    targetedIntent.setPackage(packageName);
                    if (packageName.equals(GOOGLE_PAY_PACKAGE_NAME) || packageName.equals(PHONEPE_PACKAGE_NAME) || packageName.equals(PAYTM_PACKAGE_NAME)) {
                        targetedIntents.add(targetedIntent);
                    }
                }

                if (targetedIntents.size() > 0) {

                    Intent chooser = Intent.createChooser(targetedIntents.remove(0), "Pay with...");
                    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedIntents.toArray(new Parcelable[]{}));
                    startActivityForResult(chooser, GOOGLE_PAY_REQUEST_CODE);
                } else {
                    Toast.makeText(getApplicationContext(), "Google Pay, PhonePe, or Paytm is not installed on your device", Toast.LENGTH_SHORT).show();
                }
            }
        }
        });




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_PAY_REQUEST_CODE) {
            try{
                Log.d("result",data.getStringExtra("Status") );
                System.out.println("------------------------>");
                if(data.getStringExtra("Status").toUpperCase().equals("SUCCESS")){
                    Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                    intentPayment.putExtras(data.getExtras());
                    intentPayment.putExtras(extras);
                    startActivity(intentPayment);

            }else{
                    Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    intentPayment.putExtras(data.getExtras());
                    intentPayment.putExtras(extras);
                    startActivity(intentPayment);

                }
            
           

            } catch (Exception e){
                System.out.println(e);
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "Payment Security Error", Toast.LENGTH_SHORT).show();
        }
        
    }


}
