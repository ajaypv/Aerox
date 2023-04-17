package com.aeroxlive.aeroxapplication;


import android.graphics.Bitmap;

public class PdfModel {

    String name;
    String url;
    String size;
    String date;
    String discription;
    String pdfId;
    String numberPages;

    public Bitmap getFirstPageBitmap() {
        return firstPageBitmap;
    }

    public void setFirstPageBitmap(Bitmap firstPageBitmap) {
        this.firstPageBitmap = firstPageBitmap;
    }

    Bitmap firstPageBitmap;

    public String getNumberPages() {
        return numberPages;
    }


    boolean pdfVisibility;
    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }

    public String getDiscription() {
        return discription;
    }

    public String getPdfId() {
        return pdfId;
    }

    public boolean getPdfVisibility() {
        return pdfVisibility;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
