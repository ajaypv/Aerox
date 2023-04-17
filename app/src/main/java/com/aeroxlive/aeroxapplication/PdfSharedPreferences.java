package com.aeroxlive.aeroxapplication;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PdfSharedPreferences {
    private static final String PREFS_NAME = "PdfPrefs";
    private static final String KEY_PDF_LIST = "pdfList";
    
    private SharedPreferences mPrefs;

    public PdfSharedPreferences(Context context) {
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public void savePdfList(List<PdfModel> pdfList) {
        Gson gson = new Gson();
        String json = gson.toJson(pdfList);
        mPrefs.edit().putString(KEY_PDF_LIST, json).apply();
    }

    public List<PdfModel> getPdfList() {
        Gson gson = new Gson();
        String json = mPrefs.getString(KEY_PDF_LIST, "");
        Type type = new TypeToken<List<PdfModel>>() {}.getType();
        List<PdfModel> pdfList = gson.fromJson(json, type);
        if (pdfList == null) {
            mPrefs.edit().clear().apply();
            return new ArrayList<>();
        }
        return pdfList;
    }

}
