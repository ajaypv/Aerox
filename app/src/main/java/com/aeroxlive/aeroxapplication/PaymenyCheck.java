package com.aeroxlive.aeroxapplication;

import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaymenyCheck extends AppCompatActivity {
    private RetrofitInterface retrofitInterface;
    private final String BASE_URL = "https://jzwwm6lnek.execute-api.ap-south-1.amazonaws.com/";

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private boolean isApiRequestMade = false;
    DatabaseReference databaseReference, databaseReference2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymeny_check);

        Bundle extras = getIntent().getExtras();
        String payment_status = extras.getString("Status");
        String payment_txnRef = extras.getString("txnRef");
        String payment_responseCode = extras.getString("responseCode");
        String payment_txnId = extras.getString("txnId");

        String storeName = extras.getString("StoreName");
        String storeId = extras.getString("StoreId");
        String printFinalTotalPrice = getIntent().getStringExtra("printFinalTotalPrice");




        System.out.println(printFinalTotalPrice);
        System.out.println(payment_status);
        System.out.println(payment_txnRef);
        System.out.println(payment_responseCode);
        System.out.println(payment_txnId);

        String selectedColor = extras.getString("selected_color");
        int numCopies = Integer.parseInt(extras.getString("num_copies"));
        String allPagesSelected = extras.getString("all_pages_selected");
        String customPageRange = extras.getString("custom_page_range");
        String selectedBinding = extras.getString("selected_binding");




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid() + "/transactions");



        // Get a reference to the "print requests" node in the Firebase Realtime Database
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Stores/"+storeId+"/queue/");


        FirebaseUser FirebaseUser = FirebaseAuth.getInstance().getCurrentUser();




        Date date = new Date();
        PaymentModel payment = new PaymentModel();
        payment.setShopName(storeName);
        payment.setShopId(storeId);
        payment.setShopImageUrl("null");
        payment.setShopLocation("null");
        payment.setPaymentDate(date.toString());
        payment.setAmount(printFinalTotalPrice);
        payment.setMode("null");
        payment.setTransactionId(payment_txnId);
        payment.setUtrNumber(payment_txnId);
        payment.setPaymentChooseApp("null");
        payment.setPaymentStatus(payment_status);
        payment.setPrintStatus("SentToServer");
        String key = databaseReference.push().getKey();
        databaseReference.child(Objects.requireNonNull(key)).setValue(payment);

        Map<String, Object> printRequestData = new HashMap<>();
        printRequestData.put("userId", key);
        printRequestData.put("payment_txnRef", "payment_txnId");
        printRequestData.put("timestamp", ServerValue.TIMESTAMP);
        printRequestData.put("usersName",FirebaseUser.getDisplayName().toString());
        printRequestData.put("UserImage",FirebaseUser.getPhotoUrl().toString());
        databaseReference2.child(key).setValue(printRequestData);



        if(!payment_status.toUpperCase().equals("SUCCESS")&& !isApiRequestMade){
            isApiRequestMade = true;
            HashMap<String, String> map = new HashMap<>();
            map.put("userUid", getIntent().getStringExtra("userUid"));
            map.put("userName", getIntent().getStringExtra("userName"));
            map.put("pdfName", getIntent().getStringExtra("pdfName"));
            map.put("userPhoneNumber",mAuth.getCurrentUser().getPhoneNumber());
            map.put("pdfLink", getIntent().getStringExtra("pdfLink"));
            map.put("StoreId",getIntent().getStringExtra("StoreId"));
            map.put("pdfId", getIntent().getStringExtra("pdfId"));
            map.put("customPagesCount",getIntent().getStringExtra("customPagesCount"));
            map.put("numberPages", getIntent().getStringExtra("numberPages"));
            map.put("printSide",getIntent().getStringExtra("print_sides"));
            map.put("bindingColor",getIntent().getStringExtra("bindingColor"));
            map.put("selectedColor", selectedColor);
            map.put("numCopies", String.valueOf(numCopies));
            map.put("allPagesSelected", String.valueOf(allPagesSelected));
            map.put("customPageRange", customPageRange);
            map.put("selectedBinding", selectedBinding);
            map.put("Status",payment_status);
            map.put("txnRef",payment_txnRef);
            map.put("transactionKey",key);
            map.put("responseCode",payment_responseCode);
            map.put("printAmount",printFinalTotalPrice);

            Call<Void> call = retrofitInterface.executePrint(map);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.code() == 200) {
                        Intent intent = new Intent(getApplicationContext(), printstatus.class);
                        intent.putExtra("transactionKey",key);
                        intent.putExtra("storeId",storeId);
                        startActivity(intent);
                        finish();

                    } else {
                        String errorMessage = "";
                        if (response.code() == 400) {
                            errorMessage = "Bad request";
                        } else if (response.code() == 401) {
                            errorMessage = "Unauthorized";
                        } else if (response.code() == 403) {
                            errorMessage = "Forbidden";
                        } else if (response.code() == 404) {
                            errorMessage = "Not found";
                        } else if (response.code() == 500) {
                            errorMessage = "Internal server error";
                        } else {
                            errorMessage = "Unknown error occurred";
                        }
                        Toast.makeText(getApplicationContext(),
                                "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });




        }
    }
}