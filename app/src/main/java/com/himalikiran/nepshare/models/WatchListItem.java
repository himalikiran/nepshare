package com.himalikiran.nepshare.models;


/**
 * Created by himalikiran on 9/21/2016.
 */

public class WatchListItem {
    String Uid;
    String symbol;
    double price;
    double diff;

    public WatchListItem() {
    }

    public WatchListItem(String uid, String symbol, double price, double diff) {
        Uid = uid;
        this.symbol = symbol;
        this.price = price;
        this.diff = diff;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }
}

