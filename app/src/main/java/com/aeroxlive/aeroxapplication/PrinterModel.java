package com.aeroxlive.aeroxapplication;



public class PrinterModel {
    public int PrinterID;
    public String PrinterName;
    public String PrinterStatus;
    public String PrinterPower;

    public String getPrinterColorSupport() {
        return PrinterColorSupport;
    }

    public void setPrinterColorSupport(String printerColorSupport) {
        PrinterColorSupport = printerColorSupport;
    }

    public int getPrinterSpeedPerPage() {
        return PrinterSpeedPerPage;
    }

    public void setPrinterSpeedPerPage(int printerSpeedPerPage) {
        PrinterSpeedPerPage = printerSpeedPerPage;
    }

    public String PrinterColorSupport;
    public int PrinterSpeedPerPage;
    public int PrinterPrintsCount;
    public int TotalPages;
    public int waitingTime;

    public int getPrinterID() {
        return PrinterID;
    }

    public void setPrinterID(int printerID) {
        PrinterID = printerID;
    }

    public String getPrinterName() {
        return PrinterName;
    }

    public void setPrinterName(String printerName) {
        PrinterName = printerName;
    }

    public String getPrinterStatus() {
        return PrinterStatus;
    }

    public void setPrinterStatus(String printerStatus) {
        PrinterStatus = printerStatus;
    }

    public String getPrinterPower() {
        return PrinterPower;
    }

    public void setPrinterPower(String printerPower) {
        PrinterPower = printerPower;
    }

    public int getPrinterPrintsCount() {
        return PrinterPrintsCount;
    }

    public void setPrinterPrintsCount(int printerPrintsCount) {
        PrinterPrintsCount = printerPrintsCount;
    }

    public int getTotalPages() {
        return TotalPages;
    }

    public void setTotalPages(int totalPages) {
        TotalPages = totalPages;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }




    public PrinterModel(int printerID, String printerName, String printerStatus, String PrinterColorSupport, String printerPower, int PrinterSpeedPerPage, int printerPrintsCount, int totalPages, int waitingTime) {
        this.PrinterID = printerID;
        this.PrinterName = printerName;
        this.PrinterStatus = printerStatus;
        this.PrinterPower = printerPower;
        this.PrinterPrintsCount = printerPrintsCount;
        this.TotalPages = totalPages;
        this.waitingTime = waitingTime;
        this.PrinterColorSupport = PrinterColorSupport;
        this.PrinterSpeedPerPage = PrinterSpeedPerPage;
    }

    public PrinterModel() {

    }
}