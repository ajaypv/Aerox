package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class Uploading extends Fragment {
    Button uploadButton,UploadFile;
    File pdfFile;

    StorageReference storageReference ;

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    LottieAnimationView fileselect;

    LinearLayout fileInfo;
    TextView fileName,fileSize,filePageCount,fileSelectStatus;
    ImageView slectStaus;



    public Uploading() {

    }




    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uploading, container, false);


        uploadButton = view.findViewById(R.id.upload_button);
        UploadFile = view.findViewById(R.id.button_upload);
        fileName = view.findViewById(R.id.fileName);
        fileSize = view.findViewById(R.id.fileSize);
        fileInfo = view.findViewById(R.id.FileinfoItem);
        filePageCount = view.findViewById(R.id.filePageCount);
        fileSelectStatus = view.findViewById(R.id.select_Status);
        slectStaus = view.findViewById(R.id.imageViewStatus);
        storageReference = FirebaseStorage.getInstance().getReference();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    selectPDF();
                Context context = getContext();
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50);

            }
        });


        return view;
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");

//        intent.setType("*/*");
//        String[] mimetypes = {"application/pdf", "image/*"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select pdf file"),12);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==12 && resultCode==RESULT_OK && data != null && data.getData()!=null){
            Uri returnUri = data.getData();
            Cursor returnCursor =  getActivity().getContentResolver().query(returnUri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);




            PDDocument doc = null;





            returnCursor.moveToFirst();
            Long nu = returnCursor.getLong(sizeIndex);
            DecimalFormat df = new DecimalFormat("0.00");
            String ss = null;

            float sizeKb = 1024.0f;
            float sizeMb = sizeKb * sizeKb;
            float sizeGb = sizeMb * sizeKb;
            float sizeTerra = sizeGb * sizeKb;

            if(nu > 10*1024*1024){
                Toast.makeText(getActivity(), "File size should be less than 10 MB", Toast.LENGTH_SHORT).show();
                return;
            }
            fileInfo.setVisibility(View.VISIBLE);
            fileSelectStatus.setVisibility(View.GONE);
            slectStaus.setVisibility(View.GONE);

            int count;
            try {
                doc = PDDocument.load(getActivity().getContentResolver().openInputStream(returnUri));
                count = doc.getNumberOfPages();
                filePageCount.setText(String.valueOf(count));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            if(nu< sizeMb)
                ss = df.format(nu/ sizeKb)+ " Kb";
            else if(nu< sizeGb)
                ss = df.format(nu/ sizeMb) + " Mb";
            else if(nu< sizeTerra)
                ss = df.format(nu/ sizeGb) + " Gb";
            fileName.setText(returnCursor.getString(nameIndex));
            fileSize.setText(ss);

            // calling createFile Method
            String finalSs = ss;
            UploadFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    uploadPDFFILEFirebase(returnUri,returnCursor.getString(nameIndex), finalSs, String.valueOf(count));
                }
            });


        }
    }





    private void uploadPDFFILEFirebase(Uri data,String fileName,String fileSize,String pageCount) {
        final ProgressDialog progressDialog =  new ProgressDialog(getContext());
        progressDialog.setTitle("file is loading");
        progressDialog.show();


        StorageReference reference = storageReference.child(user.getUid()+"/"+fileName+".pdf");
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
                        putPf putPf = new putPf(fileName,uri.toString(),key,fileSize,false,date.toString(),"null",pageCount,"None");
                        databaseReference.child(Objects.requireNonNull(key)).setValue(putPf);

                        File pdfFile = new File(data.getPath());

                        savePDFToLocalStorage(fileName,data);

                        Toast.makeText(getActivity(), "PDF file uploaded successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(getContext(),MainPage.class));
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress  =(100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("File Uploading...."+(int) progress +" %");

                        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                        long[] pattern = {0, 50, 50, 50, 50, 50, 50, 50};
                        vibrator.vibrate(pattern, -1);


                    }
                });
    }

    private void savePDFToLocalStorage(String fileName,Uri data) {
        String filePath = null;
        String[] filePathColumn = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = getContext().getContentResolver().query(data, filePathColumn, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        if (filePath != null) {
           System.out.println(filePath + "------------------------------------------------>");
        }


        String folderName = "privatePdfs" + File.separator;
        File file = new File(getContext().getExternalFilesDir(folderName),fileName);

        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(data);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}