package com.himalikiran.nepshare;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himalikiran.nepshare.models.PortfolioItems;
import android.database.DatabaseUtils;
import com.himalikiran.nepshare.models.CurrentPrice;
import com.himalikiran.nepshare.models.Stocks;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PortfolioFragment extends Fragment {



    //DatabaseReference myRef = database.getReference("Users");

    private TextView mTotalInvestmentView;
    private TextView mTotalWorthView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<PortfolioItems, portfolioHolder> mRecyclerViewAdapter;

    private double mTotalInvestment;
    private double mTotalWorth;
    private double mCurrentPrice;
    private FirebaseDatabase mDatabase;
    public PortfolioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(com.himalikiran.nepshare.R.layout.fragment_portfolio, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.portfolioList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //mTotalInvestment=0;

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mTotalInvestmentView = (TextView) rootView.findViewById(R.id.investment);
        mTotalWorthView = (TextView) rootView.findViewById(R.id.netWorth);

        mDatabase = DatabaseUtil.getDatabase();

       // mShareListView =(ListView)rootView.findViewById(R.id.myStockList);

       // LayoutInflater headerInflater = getActivity().getLayoutInflater();
       // View header = (ViewGroup) inflater.inflate(R.layout.portfolio_header, mRecyclerView, false);

//       mRecyclerView.addHeaderView(header);




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
            populatePortfolio();
            populateTotalInvestment();
            //calculateTotalWorth();
        }

        //populateTotalInvestment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUser != null){
            populatePortfolio();
            populateTotalInvestment();
            //calculateTotalWorth();
        }
    }


    //Calculation for Total Investment goes here
    public void populateTotalInvestment(){

        DatabaseReference portfolioRef = mDatabase.getReference("Portfolio").child(mUser.getUid());
        portfolioRef.keepSynced(true);


        portfolioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTotalInvestment=0;

                mTotalWorth =0;
                for (DataSnapshot qtySnapshot: dataSnapshot.getChildren()) {

                    String symb = qtySnapshot.child("symbol").getValue(String.class);
                    double currentPrice = getCurrentPrice(symb);

                    mTotalInvestment = mTotalInvestment + (qtySnapshot.child("quantity").getValue(double.class) * qtySnapshot.child("buyPrice").getValue(double.class));
                    mTotalWorth = mTotalWorth + (qtySnapshot.child("quantity").getValue(double.class) * currentPrice);
                }
                DecimalFormat fm = new DecimalFormat("#,###,###.00");
                mTotalInvestmentView.setText("Rs. " + fm.format(mTotalInvestment));
                mTotalWorthView.setText("Rs. "+ fm.format(mTotalWorth));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //calculateTotalWorth();
    }

    public void calculateTotalWorth(){

        DatabaseReference portFolioRef = mDatabase.getReference("Portfolio").child(mUser.getUid());

        portFolioRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTotalWorth =0;
                for (DataSnapshot qtySnapshot: dataSnapshot.getChildren()){
                    String Symb = qtySnapshot.child("symbol").getValue(String.class);
                    double qty = qtySnapshot.child("quantity").getValue(double.class);

                   double currentPrice = getCurrentPrice(Symb);

                    mTotalWorth = mTotalWorth + (qty * mCurrentPrice);
                     }
                DecimalFormat fm = new DecimalFormat("#,###,###.00");
                mTotalWorthView.setText("Rs. "+ fm.format(mTotalWorth));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public double getCurrentPrice(String symbol){

        DatabaseReference stockRef = mDatabase.getReference("Stocks").child(symbol);
        stockRef.keepSynced(true);

        stockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot priceSnapshot) {
                Stocks cp  = priceSnapshot.getValue(Stocks.class);
                mCurrentPrice = cp.getPrice();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return mCurrentPrice;
    }

    public void populatePortfolio(){

    final String userId = mUser.getUid();

    DatabaseReference portfolioCompaniesRef = mDatabase.getReference("Portfolio").child(userId);
    portfolioCompaniesRef.keepSynced(true);

    final DatabaseReference stocksRef = mDatabase.getReference("Stocks"); //need to be modify
    stocksRef.keepSynced(true);

    mTotalWorth=0;

    mRecyclerViewAdapter =
            new FirebaseRecyclerAdapter<PortfolioItems, portfolioHolder>(PortfolioItems.class, R.layout.portfolio_item, portfolioHolder.class, portfolioCompaniesRef) {
                @Override
                protected void populateViewHolder(final portfolioHolder portfolioViewHolder, final PortfolioItems model, int position) {
                    String key = this.getRef(position).getKey();

                    stocksRef.child(model.getSymbol()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double currentPrice = dataSnapshot.child("price").getValue(double.class);
                            double diffAmount = dataSnapshot.child("diff").getValue(double.class);
                            double diffPercent = dataSnapshot.child("percent").getValue(double.class);

                            double netGainAmount = currentPrice * model.getQuantity();
                            //mTotalWorth = mTotalWorth +netGainAmount;
                            //DecimalFormat fm = new DecimalFormat("#,###,###.00");
                            //mTotalWorthView.setText("Rs. "+ fm.format(mTotalWorth));

                            ((TextView) portfolioViewHolder.currentPrice).setText(String.format("%.2f",currentPrice));
                            ((TextView) portfolioViewHolder.diffText).setText(Double.toString(diffAmount));
                            ((TextView) portfolioViewHolder.percentText).setText("("+String.format("%.2f",diffPercent)+"%)");
                            ((TextView) portfolioViewHolder.netGainText).setText(Double.toString(netGainAmount));


                            if (diffAmount < 0) {
                                ((TextView) portfolioViewHolder.currentPrice).setTextColor(getResources().getColor(R.color.priceDecrease));
                                ((TextView) portfolioViewHolder.diffText).setTextColor(getResources().getColor(R.color.priceDecrease));
                                ((TextView) portfolioViewHolder.percentText).setTextColor(getResources().getColor(R.color.priceDecrease));
                                ((ImageView) portfolioViewHolder.imgViewPortfolio).setBackgroundResource(R.drawable.arrow_down);
                            } else if (diffAmount > 0) {
                                ((TextView) portfolioViewHolder.currentPrice).setTextColor(getResources().getColor(R.color.priceIncrease));
                                ((TextView) portfolioViewHolder.diffText).setTextColor(getResources().getColor(R.color.priceIncrease));
                                ((TextView) portfolioViewHolder.percentText).setTextColor(getResources().getColor(R.color.priceIncrease));
                                ((ImageView) portfolioViewHolder.imgViewPortfolio).setBackgroundResource(R.drawable.arrow_up);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

//                    watchlistRef.child(model.getSymbol()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            double sharePrice = 0;
//                            double shareDiff = 0;
//                            double sharePercentage = 0;
//
//                            if (dataSnapshot.child("price").exists()) {
//                                sharePrice = dataSnapshot.child("price").getValue(Double.class);
//                            }
//                            if (dataSnapshot.child("diff").exists()) {
//                                shareDiff = dataSnapshot.child("diff").getValue(Double.class);
//                            }
//                            if (dataSnapshot.child("percent").exists()) {
//                                sharePercentage = dataSnapshot.child("percent").getValue(Double.class);
//                            }
//
//
//                            if (shareDiff < 0) {
//                                ((TextView) viewHolder.priceText).setTextColor(getResources().getColor(R.color.priceDecrease));
//                                ((TextView) viewHolder.diffText).setTextColor(getResources().getColor(R.color.priceDecrease));
//                                ((TextView) viewHolder.percentText).setTextColor(getResources().getColor(R.color.priceDecrease));
//                                ((ImageView) viewHolder.imgView).setBackgroundResource(R.drawable.arrow_down);
//                            } else if (shareDiff > 0) {
//                                ((TextView) viewHolder.priceText).setTextColor(getResources().getColor(R.color.priceIncrease));
//                                ((TextView) viewHolder.diffText).setTextColor(getResources().getColor(R.color.priceIncrease));
//                                ((TextView) viewHolder.percentText).setTextColor(getResources().getColor(R.color.priceIncrease));
//                                ((ImageView) viewHolder.imgView).setBackgroundResource(R.drawable.arrow_up);
//                            }
//
//                            ((TextView) viewHolder.priceText).setText(String.format("%.2f", sharePrice));
//                            ((TextView) viewHolder.diffText).setText(String.format("%.2f", shareDiff));
//                            ((TextView) viewHolder.percentText).setText("("+String.format("%.2f", sharePercentage)+"%)");
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });

                    //viewHolder.symbolText.setText("test");

                    if (position % 2 == 0) {
                        portfolioViewHolder.stripView.setBackgroundResource(R.color.stripColor);
                    } else {
                        portfolioViewHolder.stripView.setBackgroundResource(R.color.white);
                    }

                    portfolioViewHolder.symbolText.setText(model.getSymbol());
                    portfolioViewHolder.quantityText.setText(Integer.toString(model.getQuantity()));
                    portfolioViewHolder.priceText.setText(String.format("%.2f",model.getBuyPrice()));
                    portfolioViewHolder.portfolioShareType.setText(model.getShareType());

                    //double buyPriceTotal = model.getBuyPrice()
                    //mTotalInvestment = mTotalInvestment + (model.getBuyPrice() * model.getQuantity());
                    //DecimalFormat fm = new DecimalFormat("#,###,###.00");
                   // mSnapshot.setText("");
                    //mTotalInvestmentView.setText("Rs. "+ fm.format(mTotalInvestment));


                }
            };
    mRecyclerViewAdapter.notifyDataSetChanged();
    mRecyclerView.setAdapter(mRecyclerViewAdapter);
}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.cleanup();
        }
    }

    public static class portfolioHolder extends RecyclerView.ViewHolder {
        LinearLayout stripView;
        TextView symbolText;
        TextView quantityText;
        TextView priceText;
        TextView currentPrice;
        TextView diffText;
        TextView percentText;
        TextView netGainText;
        ImageView imgViewPortfolio;
    TextView portfolioShareType;

        public portfolioHolder(View v) {
            super(v);
            stripView = (LinearLayout) v.findViewById(R.id.portfolioStrip);
            symbolText = (TextView) v.findViewById(R.id.symbolText);
            portfolioShareType = (TextView) v.findViewById(R.id.portfolioShareType);
            quantityText = (TextView) v.findViewById(R.id.quantityText);
            priceText = (TextView) v.findViewById(R.id.buyPriceText);
            currentPrice = (TextView) v.findViewById(R.id.pText);
            diffText = (TextView) v.findViewById(R.id.totalDiff);
            percentText = (TextView) v.findViewById(R.id.percentChange);
            netGainText = (TextView) v.findViewById(R.id.netGainText);
            imgViewPortfolio = (ImageView) v.findViewById(R.id.arrowViewPortfolio);

//            companyNameText = (TextView) v.findViewById(R.id.companyNameText); //to display full name of the company
//
//
//            percentText = (TextView) v.findViewById(R.id.percentChangeView);
        }
    }
}

