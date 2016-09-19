package com.himalikiran.nepshare;

/**
 * Created by himalikiran on 9/16/2016.
 */
public class PortfolioItems {
    String symbol;
    int quantity;
    int lastPrice;

    public PortfolioItems() {
    }

    public PortfolioItems(String symbol, int quantity, int lastPrice) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.lastPrice = lastPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(int lastPrice) {
        this.lastPrice = lastPrice;
    }
}

