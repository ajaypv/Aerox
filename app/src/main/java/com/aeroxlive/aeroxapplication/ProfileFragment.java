package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class ProfileFragment extends Fragment {
    TextView profileName,phoneNumber,UserMailId;
    ImageView UserImage,profileBack;
    FirebaseUser firebaseUser;
    LinearLayout logoutLinearLayout;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    LinearLayout websiteLink,mailSupport,contact;



    public ProfileFragment() {

    }




    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profileName);
        UserMailId = view.findViewById(R.id.userMailId);
        UserImage = view.findViewById(R.id.userImage);
        phoneNumber = view.findViewById(R.id.userMobileNumber);
        logoutLinearLayout = view.findViewById(R.id.logoutLinearLayout);
        websiteLink =view.findViewById(R.id.websiteLink);
        mailSupport =view.findViewById(R.id.mailSupport);
        contact= view.findViewById(R.id.contact);
        profileBack = view.findViewById(R.id.ProfileBack);


        profileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.popBackStack(); //
                // Get the menu item for the home fragment
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navbarK);
                bottomNavigationView.setSelectedItemId(R.id.home);

            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://ajaypv.me/contact.html";
                Intent websiteLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(websiteLinkIntent);

            }
        });

        websiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://aerox.live";
                Intent websiteLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(websiteLinkIntent);

            }
        });

        mailSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + "ajaypv44@gmail.com"));
                startActivity(intent);


            }
        });

        firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser !=null){
            UserMailId.setText(firebaseUser.getEmail());
            profileName.setText(firebaseUser.getDisplayName());
            Glide.with(getContext())
                    .load(firebaseUser.getPhotoUrl())
                    .dontTransform()
                    .into(UserImage);
            phoneNumber.setText(firebaseUser.getPhoneNumber());

        }
        logoutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(getContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut();
                startActivity(new Intent(getActivity(),onBording.class));
            }
        });



        return view;
    }

}