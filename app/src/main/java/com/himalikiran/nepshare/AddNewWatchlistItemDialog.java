package com.himalikiran.nepshare;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.himalikiran.nepshare.models.Companies;
import com.himalikiran.nepshare.models.FavItems;


/**
 * Created by himalikiran on 9/28/2016.
 */

public class AddNewWatchlistItemDialog extends DialogFragment implements View.OnClickListener {



    private DatabaseReference mRef;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    private FirebaseRecyclerAdapter<Companies, AddNewWatchlistItemDialog.populateCompaniesHolder> mRecyclerViewAdapter;



    private RecyclerView mRecyclerView;
    private Intent mServiceIntent;
    private EditText mQueryText;
    private Query searchListQueryRef;
    public AddNewWatchlistItemDialog(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Add New Watchlist Item"); // Need some work

        View view = inflater.inflate(R.layout.fragment_add_new_watchlist_item, container, false);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mDatabase = DatabaseUtil.getDatabase();

        mRef = FirebaseDatabase.getInstance().getReference();

        mUser = mAuth.getCurrentUser();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.searchStockList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mServiceIntent = new Intent(getActivity(), FetchStockDataService.class);
        getActivity().startService(mServiceIntent);


        mQueryText = (EditText)view.findViewById(R.id.companyList);

        mQueryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                populateCompanies(mQueryText.getText().toString()+" ");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        prepareData();
        populateCompanies(mQueryText.getText().toString());
        return view;
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                this.dismiss();
                break;
            case R.id.favButton:
                //addNewShare();
                Toast.makeText(this.getContext(), "New Share on your watchlist has been added!",Toast.LENGTH_LONG).show();
                //-+++this.dismiss();
                break;
            default:
                break;
        }
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        return dialog;
    }

    public void addNewFavItem(String favSymbolText){
        String userId = mUser.getUid();


          FavItems favItems = new FavItems( userId, favSymbolText );

        mRef.child("Favorites").child(userId).child(favSymbolText).setValue(favItems);
        mRef.keepSynced(true);
        //Toast.makeText(getContext(),favSymbolText + " has been added!", Toast.LENGTH_LONG).show();
    }

    public void removeFavItem(String fabSymbolText){
        String userId = mUser.getUid();
        mRef.child("Favorites").child(userId).child(fabSymbolText).removeValue();
        mRef.keepSynced(true);
        //Toast.makeText(getContext(),fabSymbolText + " has been removed!", Toast.LENGTH_LONG).show();

    }
    public void prepareData( ){
        mServiceIntent = new Intent(getActivity(), FetchStockDataService.class);
        getActivity().startService(mServiceIntent);
    }

    public void populateCompanies(String searchQuery){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //final DatabaseReference watchlistRef = database.getReference("Stocks"); //need to be modify
        final DatabaseReference companyRef = database.getReference("Companies"); //Reference to company list
        companyRef.keepSynced(true);

        final DatabaseReference favoritesRef = database.getReference("Favorites").child(mUser.getUid());
        favoritesRef.keepSynced(true);


        if (searchQuery.isEmpty()) {
            searchListQueryRef = companyRef.orderByChild("Company");
        }
        else{
            searchListQueryRef = companyRef.orderByChild("Company")
                        .startAt(searchQuery);
        }
        searchListQueryRef.keepSynced(true);


        mRecyclerViewAdapter =
                new FirebaseRecyclerAdapter<Companies, AddNewWatchlistItemDialog.populateCompaniesHolder>(Companies.class, R.layout.search_list_item, populateCompaniesHolder.class, searchListQueryRef) {
                    @Override
                    protected void populateViewHolder(final populateCompaniesHolder companiesHolder, Companies model, int position) {

                        final String key = this.getRef(position).getKey();

                        companyRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String companyName = dataSnapshot.child("Company").getValue(String.class);
                                final String symbolText = dataSnapshot.child("Symbol").getValue(String.class);
                                ((TextView)companiesHolder.companyNameText).setText(companyName+" ("+symbolText+")");


                                companiesHolder.mFavButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            addNewFavItem(symbolText);
                                        } else {
                                            removeFavItem(symbolText);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        favoritesRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String smb = dataSnapshot.child("symbol").getValue(String.class);

                                if (key.equals(smb)){
                                    ((ToggleButton)companiesHolder.mFavButton).setChecked(true);
                                }
                                else {
                                    ((ToggleButton)companiesHolder.mFavButton).setChecked(false);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        if(position %2 ==0){
                            companiesHolder.searchStripView.setBackgroundResource(R.color.stripColor);
                        }
                        else {
                            companiesHolder.searchStripView.setBackgroundResource(R.color.white);
                        }

                    }
                };

        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    public static class populateCompaniesHolder extends RecyclerView.ViewHolder{
        LinearLayout searchStripView;
        ToggleButton mFavButton;
        TextView companyNameText;


        public populateCompaniesHolder(View v){
            super(v);
            searchStripView =(LinearLayout)v.findViewById(R.id.searchListStrip);
            mFavButton = (ToggleButton) v.findViewById(R.id.favButton);
            companyNameText = (TextView) v.findViewById(R.id.searchCompanyNameText); //to display full name of the company
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