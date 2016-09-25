package com.himalikiran.nepshare;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PortfolioFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    private ValueEventListener mPortfolioListener;
    private TextView mSnapshot;
    private ListView mShareListView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    public PortfolioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(com.himalikiran.nepshare.R.layout.fragment_portfolio, container, false);

       // mPortfolioShareList = (RecyclerView) rootView.findViewById(R.id.portfolioShareList);

       // mPortfolioShareList.setHasFixedSize(true);
      //  mPortfolioShareList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mSnapshot = (TextView) rootView.findViewById(R.id.investment);

        mShareListView =(ListView)rootView.findViewById(R.id.myStockList);

        LayoutInflater headerInflater = getActivity().getLayoutInflater();
        View header = (ViewGroup) inflater.inflate(R.layout.portfolio_header, mShareListView, false);

        mShareListView.addHeaderView(header);
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();


        final ArrayList<PortfolioItems> mShareItems = new ArrayList<PortfolioItems>();
        final PortfolioShareItemsAdapter itemsAdapter = new PortfolioShareItemsAdapter(getActivity(), mShareItems);

        mShareListView.setAdapter(itemsAdapter);
        ValueEventListener itemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PortfolioItems pItem = dataSnapshot.getValue(PortfolioItems.class);
                mShareItems.add(pItem);
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        String uid = mUser.getUid();
        //Query queryRef = myRef.child(uid).child("Portfolio").orderByChild(uid).equalTo(uid);
        myRef.child(uid).child("Portfolio").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PortfolioItems pItem = dataSnapshot.getValue(PortfolioItems.class);
                mShareItems.add(pItem);
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                PortfolioItems pItem = dataSnapshot.getValue(PortfolioItems.class);
                mShareItems.add(pItem);
                itemsAdapter.notifyDataSetChanged();
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

