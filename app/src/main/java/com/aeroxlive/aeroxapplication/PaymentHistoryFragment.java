package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;


public class PaymentHistoryFragment extends Fragment {

    AlertDialog.Builder alertDialog;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference database,database2;
    PaymentHistoryAdapter myAdapter;
    ArrayList<PaymentModel> list;

    private PaymentSharedPreferences paymentSharedPreferences;



    public PaymentHistoryFragment() {

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        paymentSharedPreferences = new PaymentSharedPreferences(getContext());

        View view  = LayoutInflater.from(getContext()).inflate(R.layout.fragment1_layout,container,false);
        RecyclerView recyclerView =view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        FirebaseUser user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("Users/"+user.getUid() + "/transactions");

        list = (ArrayList<PaymentModel>) paymentSharedPreferences.getPaymentList();


        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    System.out.println(dataSnapshot);
                    PaymentModel payment = dataSnapshot.getValue( PaymentModel.class);
                    list.add(payment);
                }
//                }catch (Exception e){
//                    Toast.makeText(getApplicationContext(),"Error while get Data from server",Toast.LENGTH_LONG).show();
//                }
                paymentSharedPreferences.savePaymentList(list);
                myAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        myAdapter = new PaymentHistoryAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);


        return view;
    }
}