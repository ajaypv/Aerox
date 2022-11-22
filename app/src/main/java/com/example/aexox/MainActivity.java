package com.example.aexox;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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


import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    Button btnLogOut ,b1,pdfpage,payment;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView selectpdf, userid, pdfSize, pgCount;
    EditText e1, discription;
    Switch pdfView;

    StorageReference storageReference ;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btnLogOut = findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, SignUp.class));
        });
        pdfpage = findViewById(R.id.pdfPage);
        e1 = findViewById(R.id.s1);
        b1 = findViewById(R.id.b1);
        selectpdf = findViewById(R.id.selectpdf);
        userid = findViewById(R.id.userid);
        pdfSize = findViewById(R.id.pdfsize);
        payment = findViewById(R.id.payment);
        pdfView = findViewById(R.id.pdfviewswitch);
        discription = findViewById(R.id.discription);
        pgCount = findViewById(R.id.pgcount);
        payment.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),"payment ",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),MainPage.class));
        });
        storageReference = FirebaseStorage.getInstance().getReference();

        pdfpage.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, PdfPage.class));


        });

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
            Uri returnUri = data.getData();



            try {
                PDDocument doc = PDDocument.load( getContentResolver().openInputStream(returnUri));
                int count = doc.getNumberOfPages();
                pgCount.setText(Integer.toString(count));

            } catch (IOException e) {
                e.printStackTrace();
            }

            Cursor returnCursor =  getContentResolver().query(returnUri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            Long nu = returnCursor.getLong(sizeIndex);
            DecimalFormat df = new DecimalFormat("0.00");
            String ss = null;

            float sizeKb = 1024.0f;
            float sizeMb = sizeKb * sizeKb;
            float sizeGb = sizeMb * sizeKb;
            float sizeTerra = sizeGb * sizeKb;


            if(nu< sizeMb)
                ss = df.format(nu/ sizeKb)+ " Kb";
            else if(nu< sizeGb)
                ss = df.format(nu/ sizeMb) + " Mb";
            else if(nu< sizeTerra)
                ss = df.format(nu/ sizeGb) + " Gb";
            e1.setText(returnCursor.getString(nameIndex));
            pdfSize.setText(ss);

            b1.setOnClickListener(view -> uploadPDFFILEFirebase(data.getData()));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(MainActivity.this, MainPage.class));
        }
        if (user == null){
            startActivity(new Intent(MainActivity.this, onBording.class));
        }
    }

    private void uploadPDFFILEFirebase(Uri data) {
        final ProgressDialog progressDialog =  new ProgressDialog(this);
        progressDialog.setTitle("file is loading");
        progressDialog.show();
        FirebaseUser user = mAuth.getCurrentUser();
        StorageReference reference = storageReference.child(user.getUid()+"/"+System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        Date date = new Date();
                        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF/"+user.getUid());
                        String key = databaseReference.push().getKey();
                        putPf putPf = new putPf(e1.getText().toString(),uri.toString(),key,pdfSize.getText().toString(),pdfView.isChecked(),date.toString(),discription.getText().toString(),pgCount.getText().toString());
                        databaseReference.child(Objects.requireNonNull(key)).setValue(putPf);
                        Toast.makeText(MainActivity.this,"file upload",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(MainActivity.this,PdfPage.class));
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