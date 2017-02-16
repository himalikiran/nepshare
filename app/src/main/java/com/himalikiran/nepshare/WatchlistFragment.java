package com.himalikiran.nepshare;


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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himalikiran.nepshare.models.FavItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class WatchlistFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Intent mServiceIntent;
    private FirebaseRecyclerAdapter<FavItems, watchlistHolder> mRecyclerViewAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    public WatchlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(com.himalikiran.nepshare.R.layout.fragment_watchlist, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshWatchLList);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.watchStockList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareData();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        mServiceIntent = new Intent(getActivity(), FetchStockDataService.class);
//        getActivity().startService(mServiceIntent);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUser != null){
            getWatchlist();
        }

    }

    public void getWatchlist() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String userId = mUser.getUid();

        DatabaseReference watchListCompaniesRef = database.getReference("Favorites").child(userId);
        watchListCompaniesRef.keepSynced(true);

        final DatabaseReference watchlistRef = database.getReference("Stocks"); //need to be modify
        watchlistRef.keepSynced(true);

        final DatabaseReference companyRef = database.getReference("Companies"); //Reference to company list
        companyRef.keepSynced(true);

        mRecyclerViewAdapter =
                new FirebaseRecyclerAdapter<FavItems, watchlistHolder>(FavItems.class, R.layout.nepse_list_item, watchlistHolder.class, watchListCompaniesRef) {
                    @Override
                    protected void populateViewHolder(final watchlistHolder viewHolder, FavItems model, int position) {
                        String key = this.getRef(position).getKey();
                        companyRef.child(model.getSymbol()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String companyName = dataSnapshot.child("Company").getValue(String.class);
                                ((TextView) viewHolder.companyNameText).setText(companyName);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        watchlistRef.child(model.getSymbol()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                double sharePrice = 0;
                                double shareDiff = 0;
                                double sharePercentage = 0;

                                if (dataSnapshot.child("price").exists()) {
                                    sharePrice = dataSnapshot.child("price").getValue(Double.class);
                                }
                                if (dataSnapshot.child("diff").exists()) {
                                    shareDiff = dataSnapshot.child("diff").getValue(Double.class);
                                }
                                if (dataSnapshot.child("percent").exists()) {
                                    sharePercentage = dataSnapshot.child("percent").getValue(Double.class);
                                }


                                if (shareDiff < 0) {
                                    ((TextView) viewHolder.priceText).setTextColor(getResources().getColor(R.color.priceDecrease));
                                    ((TextView) viewHolder.diffText).setTextColor(getResources().getColor(R.color.priceDecrease));
                                    ((TextView) viewHolder.percentText).setTextColor(getResources().getColor(R.color.priceDecrease));
                                    ((ImageView) viewHolder.imgView).setBackgroundResource(R.drawable.arrow_down);
                                } else if (shareDiff > 0) {
                                    ((TextView) viewHolder.priceText).setTextColor(getResources().getColor(R.color.priceIncrease));
                                    ((TextView) viewHolder.diffText).setTextColor(getResources().getColor(R.color.priceIncrease));
                                    ((TextView) viewHolder.percentText).setTextColor(getResources().getColor(R.color.priceIncrease));
                                    ((ImageView) viewHolder.imgView).setBackgroundResource(R.drawable.arrow_up);
                                }

                                ((TextView) viewHolder.priceText).setText(String.format("%.2f", sharePrice));
                                ((TextView) viewHolder.diffText).setText(String.format("%.2f", shareDiff));
                                ((TextView) viewHolder.percentText).setText("("+String.format("%.2f", sharePercentage)+"%)");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        if (position % 2 == 0) {
                            viewHolder.stripView.setBackgroundResource(R.color.stripColor);
                        } else {
                            viewHolder.stripView.setBackgroundResource(R.color.white);
                        }
                        viewHolder.symbolText.setText(model.getSymbol());

                    }
                };
        mRecyclerViewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);


    }

    public void prepareData() {
        mServiceIntent = new Intent(getActivity(), FetchStockDataService.class);
        getActivity().startService(mServiceIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }

    public static class watchlistHolder extends RecyclerView.ViewHolder {
        LinearLayout stripView;
        TextView symbolText;
        TextView companyNameText;
        TextView priceText;
        TextView diffText;
        TextView percentText;
        ImageView imgView;

        public watchlistHolder(View v) {
            super(v);
            stripView = (LinearLayout) v.findViewById(R.id.liveStockListStrip);
            symbolText = (TextView) v.findViewById(R.id.symbolTextView);
            companyNameText = (TextView) v.findViewById(R.id.companyNameText); //to display full name of the company
            priceText = (TextView) v.findViewById(R.id.priceTextView);
            diffText = (TextView) v.findViewById(R.id.totalDiffView);
            percentText = (TextView) v.findViewById(R.id.percentChangeView);
            imgView = (ImageView) v.findViewById(R.id.arrowView);
        }
    }
}
