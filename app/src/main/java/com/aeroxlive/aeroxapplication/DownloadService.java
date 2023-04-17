package com.aeroxlive.aeroxapplication;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import androidx.annotation.Nullable;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadService extends Service {
    private String url;
    private String pdfName;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        url = intent.getStringExtra("url_key");
        pdfName = intent.getStringExtra("name_key");
        new DownloadFilesTask().execute(url, pdfName);
        return START_NOT_STICKY;
    }

    private class DownloadFilesTask extends AsyncTask<String, Void, InputStream> {
        
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                System.out.println("downloading");
                URL url = new URL(strings[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {

            String folderName = "privatePdfs";
            File folder = new File(getApplicationContext().getFilesDir() + File.separator + folderName);

            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder, pdfName);
            try {
                OutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            stopSelf();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
