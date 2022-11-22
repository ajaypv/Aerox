package com.example.aexox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class UserPage extends AppCompatActivity {
    private DatabaseReference mDatabase;
    ImageView profilePic;
    EditText profileName;
    Button submit;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

// ...
        mDatabase = FirebaseDatabase.getInstance().getReference();
        profilePic = findViewById(R.id.profilepic);
        profileName = findViewById(R.id.userName);
        submit = findViewById(R.id.submitprofile);
        mDatabase.child("Users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    DataSnapshot dataSnapshot =  task.getResult();
                    String Role = dataSnapshot.child("profilepic").getValue().toString();
                    Log.d("firebase---->", Role);
                }
                else {
                    Log.d("firebaseoomm", String.valueOf(task.getResult().getValue()));
                    String ds =  task.getResult().child("profilepic").getValue(String.class);
                    String name =  task.getResult().child("profilename").getValue(String.class);
                            Picasso.get().load(ds).into(profilePic);
                    profileName.setText(name);


//                    String passwordUser = ds.child("LoginPassword").getValue(String.class);

                }
            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));

            }
        });
    }

}