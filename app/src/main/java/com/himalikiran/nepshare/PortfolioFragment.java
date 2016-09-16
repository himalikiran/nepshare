package com.himalikiran.nepshare;


import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.Firebase;

import com.google.android.gms.nearby.connection.dev.Strategy;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.himalikiran.nepshare.models.Share;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PortfolioFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Portfolio");
    private ValueEventListener mPortfolioListener;


    private TextView mSnapshot;

    private ArrayAdapter<String> mPortfolioAdapter;



    ListView mShareListView;

    RecyclerView mPortfolioShareList;

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

        mSnapshot = (TextView) rootView.findViewById(R.id.inv);

        mShareListView =(ListView)rootView.findViewById(R.id.myStockList);
//
//        String[] portfolioArray = {
//                "EBL 3300.00 -70 500 1650000",
//                "EBL 3300.00 -70 500 1650000",
//                "EBL 3300.00 -70 500 1650000",
//                "EBL 3300.00 -70 500 1650000",
//                "EBL 3300.00 -70 500 1650000",
//                "EBL 3300.00 -70 500 1650000",
//                "EBL 3300.00 -70 500 1650000",
//                "EBL 3300.00 -70 500 1650000",
//                "EBL 3300.00 -70 500 1650000",
//        };
//
//
//        List<String> myPortfolio = new ArrayList<String>(Arrays.asList(portfolioArray));




//        mPortfolioAdapter = new ArrayAdapter<String>(
//                //The current context (this fragment's parent activity)
//                getActivity(),
//                //ID of list item layout
//                R.layout.fragment_portfolio,
//                //ID of views
//                R.id.myStocks,
//                //data
//                myPortfolio);

       // ListView listview = (ListView) rootView.findViewById(R.id.myStockList);
        //listview.setAdapter(mPortfolioAdapter);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        //when the fragment is stopped, release the memory if there.
    }

    @Override
    public void onStart() {
        super.onStart();


        final ArrayList<PortfolioItem> mShareItems = new ArrayList<PortfolioItem>();

        final ProfileShareItemsAdapter itemsAdapter = new ProfileShareItemsAdapter(getActivity(), mShareItems);

        mShareListView.setAdapter(itemsAdapter);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Map<String, String> map = dataSnapshot.getValue(Map.class);
                PortfolioItem pItem = dataSnapshot.getValue(PortfolioItem.class);
                mShareItems.add(pItem);
                itemsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//       myRef.addChildEventListener(new ChildEventListener() {
//           @Override
//           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//              // Map<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
//
//               PortfolioItem pItem = dataSnapshot.getValue(PortfolioItem.class);
//               String portfolio = dataSnapshot.getValue(String.class);
//
//               mShareItems.add(dataSnapshot.getKey().,dataSnapshot.getValue().toString());
//               itemsAdapter.notifyDataSetChanged();
//           }
//
//           @Override
//           public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//               String message = dataSnapshot.getValue(String.class);
//           }
//
//           @Override
//           public void onChildRemoved(DataSnapshot dataSnapshot) {
//              // String message = dataSnapshot.getValue(String.class);
//           }
//
//           @Override
//           public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            //Ignore
//           }
//
//           @Override
//           public void onCancelled(DatabaseError databaseError) {
//            //// TODO: later
//           }
//       });



//        mPortfolioListener = shareListener;



       // List<String> myPortfolio = new ArrayList<String>(Arrays.asList(portfolioArray));

    }


    //    @Override
//    public void onResume() {
//        super.onResume();
//
//            ValueEventListener portfolioListener = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    Share share = dataSnapshot.getValue(Share.class);
//                    mSnapshot.setText(share.symbol);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Toast.makeText(getActivity(), "Failed to load post.",
//                            Toast.LENGTH_SHORT).show();
//                }
//            };
//    }


    //    public class FetchPortfolioDataTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void){
//
//        }
//    }
}
