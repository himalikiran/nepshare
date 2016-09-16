package com.himalikiran.nepshare.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by himalikiran on 9/1/2016.
 */

// [START post_class]
@IgnoreExtraProperties
public class Share {
    public String uid;
    public String symbol;
    public int quantity;
    public double price;
    public String type;
   // public Map<String, Boolean> stars = new HashMap<>();

    public Share() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Share(String uid, String symbol, int quantity, double price, String type) {
        this.uid = uid;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

//    public void setUid(String uid) {
//        this.uid = uid;
//    }

    public String getSymbol() {
        return symbol;
    }

//    public void setSymbol(String symbol) {
//        this.symbol = symbol;
//    }

    public int getQuantity() {
        return quantity;
    }

//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }

    public double getPrice() {
        return price;
    }

//    public void setPrice(float price) {
//        this.price = price;
//    }

    public String getType() {
        return type;
    }

//    public void setType(String type) {
//        this.type = type;
//    }

    // [START post_to_map]
//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("uid", uid);
//        result.put("author", author);
//        result.put("title", title);
//        result.put("body", body);
//        result.put("starCount", starCount);
//        result.put("stars", stars);
//
//        return result;
//    }
    // [END post_to_map]
}
