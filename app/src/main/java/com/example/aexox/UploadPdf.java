package com.example.aexox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;

public class UploadPdf extends AppCompatActivity {
    TextView fileName, fileSize,filepageCount, textview2;
    EditText description;
    Switch privateFile;
    Button fileUpload;
    StorageReference storageReference ;

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    LottieAnimationView fileselect;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        fileName = findViewById(R.id.filename);
        fileSize = findViewById(R.id.filesize);
        filepageCount = findViewById(R.id.filepagecount);
        description = findViewById(R.id.filedescription);
        privateFile = findViewById(R.id.fileprivate);
        fileUpload = findViewById(R.id.filesubmit);
        fileselect = findViewById(R.id.fileupload);
        textview2 = findViewById(R.id.des);
        storageReference = FirebaseStorage.getInstance().getReference();
        fileselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }

        });
        privateFile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    description.setVisibility(View.VISIBLE);
                    textview2.setVisibility(View.VISIBLE);
                }else{
                    description.setVisibility(View.INVISIBLE);
                    textview2.setVisibility(View.INVISIBLE);
                }

            }

        });


    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select pdf file"),12);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==12 && resultCode==RESULT_OK && data != null && data.getData()!=null){
            fileUpload.setEnabled(true);
            Uri returnUri = data.getData();
            try {
                PDDocument doc = PDDocument.load( getContentResolver().openInputStream(returnUri));
                int count = doc.getNumberOfPages();
                filepageCount.setText(Integer.toString(count));

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
            fileName.setText(returnCursor.getString(nameIndex));
            fileSize.setText(ss);

            fileUpload.setOnClickListener(view -> uploadPDFFILEFirebase(data.getData()));
        }
    }
    private void uploadPDFFILEFirebase(Uri data) {
        final ProgressDialog progressDialog =  new ProgressDialog(this);
        progressDialog.setTitle("file is loading");
        progressDialog.show();


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
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("PublicPDF");

                        putPf putPf = new putPf(fileName.getText().toString(),uri.toString(),key,fileSize.getText().toString(),privateFile.isChecked(),date.toString(),description.getText().toString(),filepageCount.getText().toString(),"None");
                        databaseReference.child(Objects.requireNonNull(key)).setValue(putPf);
                        if(privateFile.isChecked()){
                            databaseReference2.child(Objects.requireNonNull(key)).setValue(putPf);
                        }

                        Toast.makeText(UploadPdf.this,"file upload",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(UploadPdf.this,MainPage.class));
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