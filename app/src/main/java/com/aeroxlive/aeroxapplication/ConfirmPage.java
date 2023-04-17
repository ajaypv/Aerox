package com.aeroxlive.aeroxapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;

public class ConfirmPage extends AppCompatActivity {

    private Button confirmButton;
    private RetrofitInterface retrofitInterface;
    private final String BASE_URL = "https://jzwwm6lnek.execute-api.ap-south-1.amazonaws.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_page);

        // Retrieve the data passed from the previous activity
        Bundle extras = getIntent().getExtras();
        confirmButton = findViewById(R.id.confirmButton);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(retrofit);
                String selectedColor = extras.getString("selected_color");
                int numCopies = extras.getInt("num_copies");
                boolean allPagesSelected = extras.getBoolean("all_pages_selected");
                String customPageRange = extras.getString("custom_page_range");
                String selectedBinding = extras.getString("selected_binding");

                HashMap<String, String> map = new HashMap<>();
                map.put("userUid", getIntent().getStringExtra("userUid"));
                map.put("userName", getIntent().getStringExtra("userName"));
                map.put("pdfName", getIntent().getStringExtra("pdfName"));
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


                Call<Void> call = retrofitInterface.executePrint(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            Intent intent = new Intent(getApplicationContext(), printstatus.class);
                            intent.putExtra("pdfidd",getIntent().getStringExtra("pdfId"));
                            intent.putExtra("userid",getIntent().getStringExtra("userUid"));
                            startActivity(intent);


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
        });



    }
}
