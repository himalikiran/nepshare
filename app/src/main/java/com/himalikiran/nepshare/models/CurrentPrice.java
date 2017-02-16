package com.himalikiran.nepshare.models;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by hbhusal on 2/15/2017.
 */

public class CurrentPrice {
    private double currentPrice;
    private FirebaseDatabase mDatabase;
    private double mCurrentPrice;

    public CurrentPrice(){

    }

    public CurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getCurrentPrice(String symbol) {

        DatabaseReference stockRef = mDatabase.getReference("Stocks").child(symbol);
        stockRef.keepSynced(true);
        stockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot priceSnapshot) {
//                for (DataSnapshot priceSnapshot: dataSnapshot.getChildren()) {
                //mCurrentPrice=10;
                mCurrentPrice = priceSnapshot.child("price").getValue(double.class);
                // mCurrentPrice=10;
                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
}
