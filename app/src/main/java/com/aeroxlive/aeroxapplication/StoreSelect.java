package com.aeroxlive.aeroxapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;

public class StoreSelect extends AppCompatActivity implements StoresFragment.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_select2);
        StoresFragment storesFragment = new StoresFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutContainer, storesFragment);
        fragmentTransaction.commit();

    }



    @Override
    public void onItemSelected(StoreModel storeModel) {
        if(storeModel != null){
            if(storeModel.getPower() != "offline"){
                System.out.println(storeModel.getPrinterModels().size());
                onStoreSelectNext(storeModel);
            }

        }else{
            Toast.makeText(this, "Error in the selected Store", Toast.LENGTH_SHORT).show();
        }

    }

    private void onStoreSelectNext(StoreModel storeModel) {
        Intent pdf = getIntent();

        if(storeModel.getPower().equals("Offline")){
            Toast.makeText(this, "Currently this Store is Offline", Toast.LENGTH_SHORT).show();

        }else{
            if(pdf != null){
                if(storeModel != null){
                    Intent StoreData = new Intent(getApplicationContext(),PdfOptions.class);
                    StoreData.putExtras(pdf.getExtras());
                    StoreData.putExtra("StoreName",storeModel.getShopName());
                    StoreData.putExtra("StoreId",storeModel.getStoreId());
                    StoreData.putExtra("storeColorPrice", storeModel.getColorPrice());
                    StoreData.putExtra("storeBlackAndWhitePrice", storeModel.getBwPrice());

                    startActivity(StoreData);
                }else{
                    Toast.makeText(this, "Error in the selected Store", Toast.LENGTH_SHORT).show();
                }

            }else{

                Toast.makeText(this, "Error in the selected Store", Toast.LENGTH_SHORT).show();

            }


        }


    }
    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout1);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);

        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Upload a Video is clicked",Toast.LENGTH_SHORT).show();

            }
        });







        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}