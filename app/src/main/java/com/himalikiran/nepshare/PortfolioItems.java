package com.himalikiran.nepshare;

/**
 * Created by himalikiran on 9/16/2016.
 */
public class PortfolioItems {
    String mSymbol;
    int mQuantity;
    double mPrice;
    double mNetGain;

    public PortfolioItems() {
    }

    public PortfolioItems(String Symbol, int Quantity, double Price, double NetGain) {
        mSymbol = Symbol;
        mQuantity = Quantity;
        mPrice = Price;
        mNetGain = NetGain;
    }


    public String getSymbol() {
        return mSymbol;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public double getPrice() {
        return mPrice;
    }

    public double getNetGain(){
        mNetGain = mQuantity * mPrice;
        return mNetGain;
    }

    public void setSymbol(String Symbol) {
        mSymbol = Symbol;
    }

    public void setQuantity(int Quantity) {
        mQuantity = Quantity;
    }

    public void setPrice(double Price) {
        mPrice = Price;
    }

    public void setNetGain (double NetGain){
        mNetGain = NetGain;
    }
}

