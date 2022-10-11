package com.example.aexox;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button btnLogOut ,b1;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView selectpdf, userid;
    EditText e1;

    StorageReference storageReference ;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, SignUp.class));
        });

        e1 = findViewById(R.id.s1);
        b1 = findViewById(R.id.b1);
        selectpdf = findViewById(R.id.selectpdf);
        userid = findViewById(R.id.userid);

        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF/"+user.getUid());
        b1.setEnabled(false);


        selectpdf.setOnClickListener(view -> selectPDF());

    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select pdf file"),12);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==12 && resultCode==RESULT_OK && data != null && data.getData()!=null){
            b1.setEnabled(true);
            e1.setText(data.getDataString()
                    .substring(data.getDataString().lastIndexOf("/")+1));
            b1.setOnClickListener(view -> uploadPDFFILEFirebase(data.getData()));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();
            userid.setText(uid);
        }
        if (user == null){
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }

    private void uploadPDFFILEFirebase(Uri data) {
        final ProgressDialog progressDialog =  new ProgressDialog(this);
        progressDialog.setTitle("file is loading");
        progressDialog.show();

        StorageReference reference = storageReference.child("upload"+System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        putPf putPf = new putPf(e1.getText().toString(),uri.toString());
                        databaseReference.child(Objects.requireNonNull(databaseReference.push().getKey())).setValue(putPf);
                        Toast.makeText(MainActivity.this,"file upload",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress  =(100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("File Uploading...."+(int) progress +" %");

                    }
                });
    }
}