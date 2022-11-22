package com.example.aexox;


public class PdfModel {

    String name;
    String url;
    String size;
    String date;
    String discription;
    String pdfId;
    String numberPages;

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
