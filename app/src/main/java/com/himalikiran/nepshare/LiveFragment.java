package com.himalikiran.nepshare;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.himalikiran.nepshare.models.Stocks;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private Intent mServiceIntent;
    private  RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Stocks, LiveStockHolder> mRecyclerViewAdapter;

    private DatabaseReference mDatabase;

    public LiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(com.himalikiran.nepshare.R.layout.fragment_live, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);



        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.liveStockList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

       // mServiceIntent = new Intent(getActivity(), FetchStockDataService.class);
       // getActivity().startService(mServiceIntent);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //prepareData();
        getStocks();
    }

    public void getStocks(){
        final ArrayList<Stocks> stocks = new ArrayList<Stocks>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference stocksRef = database.getReference("Stocks"); //Reference to the Stock lists
        stocksRef.keepSynced(true);
        final DatabaseReference companyRef = database.getReference("Companies"); //Reference to the Company lists
        companyRef.keepSynced(true);

        Query myQueryRef = stocksRef.orderByChild("timestamp");
        myQueryRef.keepSynced(true);
        mRecyclerViewAdapter =
                new FirebaseRecyclerAdapter<Stocks, LiveStockHolder>(Stocks.class, R.layout.nepse_list_item, LiveStockHolder.class, myQueryRef) {
                    @Override
                    protected void populateViewHolder(final LiveStockHolder liveStockHolder, Stocks model, int position) {

                        String key = this.getRef(position).getKey();

                        //referencing company full name.
                        companyRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String companyName = dataSnapshot.child("Company").getValue(String.class);
                                ((TextView)liveStockHolder.companyNameText).setText(companyName);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        if(position %2 ==0){
                            liveStockHolder.stripView.setBackgroundResource(R.color.stripColor);
                        }
                        else {
                            liveStockHolder.stripView.setBackgroundResource(R.color.white);
                        }

                        liveStockHolder.symbolText.setText(model.getCompany());

                        if (model.getDiff() < 0) {
                            liveStockHolder.priceText.setTextColor(getResources().getColor(R.color.priceDecrease));
                            liveStockHolder.diffText.setTextColor(getResources().getColor(R.color.priceDecrease));
                            liveStockHolder.percentText.setTextColor(getResources().getColor(R.color.priceDecrease));
                            liveStockHolder.imgView.setBackgroundResource(R.drawable.arrow_down);
                        }
                        else if(model.getDiff() > 0){

                            liveStockHolder.priceText.setTextColor(getResources().getColor(com.himalikiran.nepshare.R.color.priceIncrease));
                            liveStockHolder.diffText.setTextColor(getResources().getColor(R.color.priceIncrease));
                            liveStockHolder.percentText.setTextColor(getResources().getColor(R.color.priceIncrease));
                            liveStockHolder.imgView.setBackgroundResource(R.drawable.arrow_up);
                        }

                        else{
                            //todo
                        }

                        liveStockHolder.priceText.setText(String.format("%.2f",model.getPrice()));
                        liveStockHolder.diffText.setText(String.format("%.2f",model.getDiff()));
                        liveStockHolder.percentText.setText("("+ String.format("%.2f",model.getPercent())+ "%)");

                    }
                };
        mRecyclerView.setAdapter(mRecyclerViewAdapter);


    }

    public void prepareData( ){
        mServiceIntent = new Intent(getActivity(), FetchStockDataService.class);
        getActivity().startService(mServiceIntent);
    }

    public static class LiveStockHolder extends RecyclerView.ViewHolder{
        LinearLayout stripView;
        TextView symbolText;
        TextView companyNameText;
        TextView priceText;
        TextView diffText;
        TextView percentText;
        ImageView imgView;
        public LiveStockHolder(View v){
            super(v);
            stripView =(LinearLayout)v.findViewById(R.id.liveStockListStrip);
            symbolText = (TextView) v.findViewById(R.id.symbolTextView);
            companyNameText = (TextView) v.findViewById(R.id.companyNameText); //to display full name of the company
            priceText = (TextView) v.findViewById(R.id.priceTextView);
            diffText = (TextView) v.findViewById(R.id.totalDiffView);
            percentText = (TextView) v.findViewById(R.id.percentChangeView);
            imgView = (ImageView) v.findViewById(R.id.arrowView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }
}
