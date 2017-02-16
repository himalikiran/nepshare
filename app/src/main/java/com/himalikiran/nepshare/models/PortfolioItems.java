package com.himalikiran.nepshare.models;


/**
 * Created by himalikiran on 9/16/2016.
 */
public class PortfolioItems {
    String symbol;
    int quantity;
    double buyPrice;
    String type;

    public PortfolioItems() {

    }

    public PortfolioItems(String symbol, int quantity, double buyPrice, String type) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.type = type;
    }

    public String getShareType() {
        return type;
    }

    public void setShareType(String type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getBuyPrice() {
        return buyPrice;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }
}

