package com.example.aexox;

public class putPf {

    public String name;
    public  String url;
    public String pdfId;

    public String getPdfId() {
        return pdfId;
    }

    public void setPdfId(String pdfId) {
        this.pdfId = pdfId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        size = size;
    }

    public boolean isPdfVisibility() {
        return pdfVisibility;
    }

    public void setPdfVisibility(boolean pdfVisibility) {
        this.pdfVisibility = pdfVisibility;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String size;
    public boolean pdfVisibility;
    public String date;
    public String discription;


    public String getNumberPages() {
        return numberPages;
    }

    public void setNumberPages(String numberPages) {
        this.numberPages = numberPages;
    }

    public String numberPages;
    public putPf(String name, String url,String pdfId,String size,boolean pdfVisibility,String date, String discription, String numberPages){
        this.name = name;
        this.url = url;
        this.pdfId = pdfId;
        this.size = size;
        this.pdfVisibility = pdfVisibility;
        this.date = date;
        this.discription = discription;
        this.numberPages = numberPages;
    }
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name  = name;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

}
