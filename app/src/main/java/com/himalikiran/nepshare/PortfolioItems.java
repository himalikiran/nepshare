package com.himalikiran.nepshare;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.himalikiran.nepshare.models.Stocks;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.google.android.gms.analytics.internal.zzy.m;
import static com.google.android.gms.analytics.internal.zzy.s;

/**
 * Created by himalikiran on 9/16/2016.
 */
public class PortfolioItems {
    String mSymbol;
    int mQuantity;
    double mPrice;
    double mLastPrice;
    double mNetGain;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Stocks");

    double lastPrice;

    public PortfolioItems() {
    }

    public PortfolioItems(String Symbol, int Quantity, double Price, double LastPrice, double NetGain) {
        mSymbol = Symbol;
        mQuantity = Quantity;
        mPrice = Price;
        mLastPrice = LastPrice;
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

    public double getLastPrice() {



        Query myQueryRef =myRef.orderByChild("company").equalTo(mSymbol);

        myQueryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Stocks stk = dataSnapshot.getValue(Stocks.class);
                lastPrice = stk.getPrice();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myQueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Stocks stk = dataSnapshot.getValue(Stocks.class);
                lastPrice = stk.getPrice();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Stocks stk = dataSnapshot.getValue(Stocks.class);
                lastPrice = stk.getPrice();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return lastPrice;
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

    public void setLastPrice(double LastPrice){
        mLastPrice = LastPrice;
    }

    public void setNetGain (double NetGain){
        mNetGain = NetGain;
    }
}

