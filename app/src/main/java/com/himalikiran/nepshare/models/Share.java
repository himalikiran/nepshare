//package com.himalikiran.nepshare.models;
//
//import com.google.firebase.database.Exclude;
//import com.google.firebase.database.IgnoreExtraProperties;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by himalikiran on 9/1/2016.
// */
//
//// [START post_class]
//@IgnoreExtraProperties
//public class Share {
//    public String symbol;
//    public int quantity;
//    public double buyPrice;
//    public String type;
//   // public Map<String, Boolean> stars = new HashMap<>();
//
//    public Share() {
//        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
//    }
//
//    public Share(String symbol, int quantity, double buyPrice, String type) {
//        this.symbol = symbol;
//        this.quantity = quantity;
//        this.buyPrice = buyPrice;
//        this.type = type;
//    }
//
//
//    public String getSymbol() {
//        return symbol;
//    }
//
//    public void setSymbol(String symbol) {
//        this.symbol = symbol;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public double getBuyPrice() {
//        return buyPrice;
//    }
//
//    public void setBuyPrice(double buyPrice) {
//        this.buyPrice = buyPrice;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//}
