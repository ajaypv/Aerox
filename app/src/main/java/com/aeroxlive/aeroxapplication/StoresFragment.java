package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.*;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StoresFragment extends Fragment {

    StoreAdapter myAdapter;
    DatabaseReference database;
    ArrayList<StoreModel> list;
    public ArrayList<PrinterModel> printerList ;
    private ShimmerFrameLayout shimmerFrameLayout;
    public interface OnItemSelectedListener {
        void onItemSelected(StoreModel storeModel);
    }

    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        View view  = LayoutInflater.from(getContext()).inflate(R.layout.storerecycleview,container,false);
        RecyclerView recyclerView =view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();


        database = FirebaseDatabase.getInstance().getReference("Stores/nhce1");


        list = new ArrayList<>();
        printerList = new ArrayList<>();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                System.out.println(dataSnapshot);
                System.out.println(dataSnapshot.getValue());
                Long printerCount = dataSnapshot.child("printers").getChildrenCount();
                String storeId = dataSnapshot.getKey().toString();
                System.out.println(printerCount+storeId);
                Long QueueCount = dataSnapshot.child("queue").getChildrenCount();
                String location = dataSnapshot.child("Location").getValue(String.class);
                String shopName = dataSnapshot.child("StoreName").getValue(String.class);
                String distance = dataSnapshot.child("Distance").getValue(String.class);
                String bwPrice = dataSnapshot.child("BwPrice").getValue(String.class);
                String color = dataSnapshot.child("Color").getValue(String.class);
                String colorPrice = dataSnapshot.child("ColorPrice").getValue(String.class);
                String ShopWaitingTime = dataSnapshot.child("WaitingTime").getValue(String.class);
                String servicesOffered = dataSnapshot.child("ServicesOffered").getValue(String.class);
                String power = dataSnapshot.child("power").getValue(String.class);
                String status = dataSnapshot.child("Status").getValue(String.class);

                ArrayList<PrinterModel> printersList3 = new ArrayList<>();
                for (DataSnapshot printerSnapshot : dataSnapshot.child("printers").getChildren()) {
                    int printerID = printerSnapshot.child("PrinterID").getValue(Integer.class);
                    String printerName = printerSnapshot.child("PrinterName").getValue(String.class);
                    String printerStatus = printerSnapshot.child("PrinterStatus").getValue(String.class);
                    String printerColorSupport = printerSnapshot.child("PrinterColorSupport").getValue(String.class);
                    String printerPower = printerSnapshot.child("PrinterPower").getValue(String.class);
                    int printerSpeedPerPage = printerSnapshot.child("PrinterSpeedPerPage").getValue(Integer.class);
                    int printerPrintsCount = printerSnapshot.child("PrinterPrintsCount").getValue(Integer.class);
                    int totalPages = printerSnapshot.child("TotalPages").getValue(Integer.class);
                    int waitingTime = printerSnapshot.child("waitingTime").getValue(Integer.class);
                        System.out.println(waitingTime+printerCount);
                    PrinterModel printer = new PrinterModel(
                            printerID,
                            printerName,
                            printerStatus,
                            printerColorSupport,
                            printerPower,
                            printerSpeedPerPage,
                            printerPrintsCount,
                            totalPages,
                            waitingTime
                    );
                    printersList3.add(printer);

                }

                StoreModel store = new StoreModel(storeId, location, shopName, distance, bwPrice, color, colorPrice, ShopWaitingTime, servicesOffered, power, status,QueueCount);
                store.printerModels = printersList3;
                System.out.println(printersList3.size());
                list.add(store);
                myAdapter.notifyDataSetChanged();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Log.d("TAG", "Printer count: " + printerCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("TAG", "Failed to read value.", databaseError.toException());
            }
        };

        database.addValueEventListener(valueEventListener);



        myAdapter = new StoreAdapter(getContext(), list);
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StoreModel storeModel = list.get(position);
                showBottomDialog(storeModel);
            }

            @Override
            public void StoreSelect(int position) {
                StoreModel storeModel = list.get(position);
                ((OnItemSelectedListener) getActivity()).onItemSelected(storeModel);


            }

            @Override
            public void onPdfDelete(int position) {

            }


        });

        return view;


    }

    private void showBottomDialog(StoreModel storeModel) {



        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout1);



        LinearLayout videoLayout = dialog.findViewById(R.id.layoutVideo);
        ImageCarousel carousel = dialog.findViewById(R.id.carousel);

        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        carousel.registerLifecycle(getLifecycle());

        List<CarouselItem> list = new ArrayList<>();

// Image URL with caption
        list.add(
                new CarouselItem(
                        "https://firebasestorage.googleapis.com/v0/b/aerox-8c69c.appspot.com/o/shop1.jpg?alt=media&token=9891a385-c8ee-4afb-95cf-0641d4e98d03"

                )
        );

// Just image URL
        list.add(
                new CarouselItem(
                        "https://firebasestorage.googleapis.com/v0/b/aerox-8c69c.appspot.com/o/shop2.jpg?alt=media&token=e5d200de-efc0-4b8b-9b16-92b44db73a4a"

                )
        );
        list.add(
                new CarouselItem(
                        "https://firebasestorage.googleapis.com/v0/b/aerox-8c69c.appspot.com/o/images3.jpg?alt=media&token=609920f4-3621-4336-87b9-e213f8cb5c36"

                )
        );
        list.add(
                new CarouselItem(
                        "https://firebasestorage.googleapis.com/v0/b/aerox-8c69c.appspot.com/o/spho22.png?alt=media&token=09898bbb-7578-4246-9fce-ea5a7cf19dd9"

                )
        );

        carousel.setAutoWidthFixing(true);

// See kotlin code for details.
        carousel.setAutoPlay(true);
        carousel.setAutoPlayDelay(3000); // Milliseconds
        carousel.setShowNavigationButtons(false);

// Touch to pause autoPlay.
        carousel.setTouchToPause(true);

// Infinite scroll for the carousel.
        carousel.setInfiniteCarousel(true);

        carousel.setData(list);


        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(getContext(),"Upload a Video is clicked",Toast.LENGTH_SHORT).show();

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