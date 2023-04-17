package com.aeroxlive.aeroxapplication;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.aeroxlive.aeroxapplication.R;
import com.github.barteksc.pdfviewer.PDFView;
import java.io.*;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class PdfView extends AppCompatActivity {
    private String url;
    private PDFView pdfView;
    private long downloadId;

    // BroadcastReceiver to show a toast with the downloaded file name when the download is completed
    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id == downloadId) {
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        @SuppressLint("Range") String fileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                        Toast.makeText(context, fileName + " downloaded", Toast.LENGTH_LONG).show();
                        pdfView = findViewById(R.id.pdfView);
                        String folderName = "privatePdfs" + File.separator;
                        File file = new File(getExternalFilesDir(folderName), fileName);
                            try {
                                InputStream inputStream = new FileInputStream(file);
                                pdfView.fromStream(inputStream).load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                    }
                }
                cursor.close();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        Intent intent3 = getIntent();
        url = intent3.getStringExtra("url_key");

        String pdfName = intent3.getStringExtra("name_key");
        pdfView = findViewById(R.id.pdfView);
        String folderName = "privatePdfs" + File.separator;
        File file = new File(getExternalFilesDir(folderName), pdfName);
        if (file.exists()) {
            // Load the PDF from local storage
            try {
                InputStream inputStream = new FileInputStream(file);
                pdfView.fromStream(inputStream).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Download the PDF from the URL
            downloadFile(url, pdfName);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the BroadcastReceiver to show a toast with the downloaded file name when the download is completed
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadCompleteReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the BroadcastReceiver
        unregisterReceiver(downloadCompleteReceiver);
    }

    private void downloadFile(String downloadUrl, String fileName) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        String folderName = "privatePdfs";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(fileName)
                .setDescription("Downloading " + fileName)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(this, folderName, fileName);
        downloadId = downloadManager.enqueue(request);
    }
}
