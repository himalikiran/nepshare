package com.himalikiran.nepshare.models;

/**
 * Created by hbhusal on 1/20/2017.
 */

public class FavItems {
    String userId;
    String symbol;

    public FavItems(){

    }
    public FavItems(String userId, String symbol){
        this.userId = userId;
        this.symbol = symbol;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
