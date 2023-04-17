package com.aeroxlive.aeroxapplication;

import java.util.ArrayList;

public class StoreModel {
    private String storeId;

    public long getQueueCount() {
        return QueueCount;
    }

    public void setQueueCount(long queueCount) {
        QueueCount = queueCount;
    }

    private long QueueCount;

    public ArrayList<PrinterModel> getPrinterModels() {
        return printerModels;
    }

    public void setPrinterModels(ArrayList<PrinterModel> printerModels) {
        this.printerModels = printerModels;
    }

    ArrayList<PrinterModel> printerModels;

    public StoreModel(String storeId, String location, String shopName, String distance, String bwPrice, String color, String colorPrice, String waitingTime, String servicesOffered, String power, String status,long count) {
        this.storeId = storeId;
        this.location = location;
        this.shopName = shopName;
        this.distance = distance;
        this.bwPrice = bwPrice;
        this.color = color;
        this.colorPrice = colorPrice;
        this.waitingTime = waitingTime;
        this.servicesOffered = servicesOffered;
        this.power = power;
        this.Status = status;
        this.QueueCount = count;
    }

    private String location;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getBwPrice() {
        return bwPrice;
    }

    public void setBwPrice(String bwPrice) {
        this.bwPrice = bwPrice;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorPrice() {
        return colorPrice;
    }

    public void setColorPrice(String colorPrice) {
        this.colorPrice = colorPrice;
    }

    public String getWaitingTime() {
        return waitingTime.replaceAll("[^\\d]", "");
    }

    public void setWaitingTime(String waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(String servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    private String shopName;
    private String distance;
    private String bwPrice;
    private String color;
    private String colorPrice;
    private String waitingTime;
    private String servicesOffered;
    private String power;
    private String Status;

}