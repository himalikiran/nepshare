package com.himalikiran.nepshare;

/**
 * Created by himalikiran on 9/16/2016.
 */
public class PortfolioItem {
    String symbol;
    int quantity;
    String lastPrice;

    public PortfolioItem() {
    }

    public PortfolioItem(String symbol, int quantity, String lastPrice) {
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

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }
}

