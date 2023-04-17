package com.aeroxlive.aeroxapplication;

public class PaymentModel {
    private String shopName;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    private String shopId;
    private String shopImageUrl;
    private String shopLocation;
    private String paymentDate;
    private String amount;
    private String mode;

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setPaymentChooseApp(String paymentChooseApp) {
        this.paymentChooseApp = paymentChooseApp;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setUtrNumber(String utrNumber) {
        this.utrNumber = utrNumber;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    private String paymentChooseApp;
    private String transactionId;
    private String utrNumber;
    private String paymentStatus;

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    private String waitTime;

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    private String printStatus;

    public PaymentModel(String shopName, String shopImageUrl, String shopLocation,
                        String paymentDate, String amount, String mode,
                        String paymentChooseApp, String transactionId,
                        String utrNumber, String paymentStatus) {
        this.shopName = shopName;
        this.shopImageUrl = shopImageUrl;
        this.shopLocation = shopLocation;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.mode = mode;
        this.paymentChooseApp = paymentChooseApp;
        this.transactionId = transactionId;
        this.utrNumber = utrNumber;
        this.paymentStatus = paymentStatus;
    }

    public PaymentModel() {

    }

    public String getShopName() {
        return shopName;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getAmount() {
        return amount;
    }

    public String getMode() {
        return mode;
    }

    public String getPaymentChooseApp() {
        return paymentChooseApp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getUtrNumber() {
        return utrNumber;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
