package com.himalikiran.nepshare;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himalikiran.nepshare.models.Stocks;
import com.himalikiran.nepshare.models.WatchListItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class WatchlistFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Stocks");
    private ListView mWatchlistView;

    public WatchlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View rootView = inflater.inflate(com.himalikiran.nepshare.R.layout.fragment_watchlist, container, false);

        mWatchlistView =(ListView)rootView.findViewById(R.id.watchList);


        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onStart() {
        super.onStart();

        final ArrayList<Stocks> watchListItems = new ArrayList<Stocks>();
        final StocksAdapter watchlistItemsAdapter = new StocksAdapter(getActivity(), watchListItems);


        mWatchlistView.setAdapter(watchlistItemsAdapter);
        ValueEventListener watchlistItemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Stocks wItems = dataSnapshot.getValue(Stocks.class);
                watchListItems.add(wItems);
                watchlistItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Stocks wItems = dataSnapshot.getValue(Stocks.class);
                watchListItems.add(wItems);
                watchlistItemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Stocks wItems = dataSnapshot.getValue(Stocks.class);
                watchListItems.add(wItems);
                watchlistItemsAdapter.notifyDataSetChanged();
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


    }
}
