package com.himalikiran.nepshare;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.himalikiran.nepshare.models.TotalInvestment;
import com.himalikiran.nepshare.models.TotalWorth;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class PortfolioFragment extends Fragment {



    //DatabaseReference myRef = database.getReference("Users");

    private TextView mTotalInvestmentView;
    private TextView mTotalWorthView;
    private TextView mNetGainView;
    private TextView mDaysGainView;
    private ImageView mArrowDaysGain;
    private TextView mDaysGainPercentView;
    private TextView mNetGainPercentView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<PortfolioItems, portfolioHolder> mRecyclerViewAdapter;

    private double mTotalInvestment;
    private double mTotalWorth;

    private ImageView mArrowNetGain;

    private double mDaysGain;
    private double mLastGain;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

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

        mDatabase = DatabaseUtil.getDatabase();
        mRef = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mTotalInvestmentView = (TextView) rootView.findViewById(R.id.investment);
        mTotalWorthView = (TextView) rootView.findViewById(R.id.netWorth);
        mNetGainView = (TextView) rootView.findViewById(R.id.netGain);
        mArrowNetGain = (ImageView) rootView.findViewById(R.id.arrowNetGain);
        mDaysGainView = (TextView) rootView.findViewById(R.id.daysGain);
        mDaysGainPercentView = (TextView) rootView.findViewById(R.id.daysGainPercent);
        mNetGainPercentView = (TextView) rootView.findViewById(R.id.netGainPercent);
        mArrowDaysGain = (ImageView) rootView.findViewById(R.id.arrowDaysGain);

        if (mUser != null) {
                populatePortfolio();
                populateTotalInvestment();
                calculateTotalWorth();
                calculateNetGain();
        }
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
            //populatePortfolio();
            //populateTotalInvestment();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUser != null){
            populatePortfolio();
           //populateTotalInvestment();
        }
    }


    //Calculation for Total Investment
    public void populateTotalInvestment(){

        DatabaseReference portfolioRef = mDatabase.getReference("Portfolio").child(mUser.getUid());
        portfolioRef.keepSynced(true);



        portfolioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTotalInvestment=0;

                for (DataSnapshot qtySnapshot: dataSnapshot.getChildren()) {
                    int qty = qtySnapshot.child("quantity").getValue(int.class);
                    mTotalInvestment = mTotalInvestment + qty * qtySnapshot.child("buyPrice").getValue(double.class);
                }

                DecimalFormat fm = new DecimalFormat("#,###,###.00");
                mTotalInvestmentView.setText("Rs. " + fm.format(mTotalInvestment));

                TotalInvestment tInv = new TotalInvestment(mTotalInvestment);
                mRef.child("TotalInvestment").child(mUser.getUid()).setValue(tInv);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    //Calculation for Total Worth
    public void calculateTotalWorth(){

        final DatabaseReference portfolioRef = mDatabase.getReference("Portfolio").child(mUser.getUid());
        portfolioRef.keepSynced(true);

        final DatabaseReference stocksRef = mDatabase.getReference("Stocks");
        stocksRef.keepSynced(true);

        portfolioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String sm = ds.child("symbol").getValue().toString();
                    final int qt = ds.child("quantity").getValue(int.class);


                    stocksRef.child(sm).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            final double pr = dataSnapshot.child("price").getValue(double.class);
                            final double df = dataSnapshot.child("diff").getValue(double.class);

                            mTotalWorth = mTotalWorth + (qt * pr); //Total Worth = Sum of (Quantity * Current Price)
                            mDaysGain = mDaysGain + (qt * df); // Day's Gain = Sum of (Quantity * Difference)

                            mLastGain = mLastGain + (qt *(pr-df)); // Last Gain = Sum of (Quantity * (Current Price - Difference)

                            double daysgainpercentage = mDaysGain/mLastGain; // Day's Gain Percentage = Last Gain / Today's Gain

                            DecimalFormat fm = new DecimalFormat("#,###,###.00");
                            mTotalWorthView.setText("Rs. "+ fm.format(mTotalWorth));
                            mDaysGainView.setText("Rs. "+ fm.format(mDaysGain));
                            mDaysGainPercentView.setText("("+String.format("%.2f",daysgainpercentage)+"%)");

                            if (mDaysGain < 0 ){
                                mDaysGainView.setTextColor(Color.RED);
                                mArrowDaysGain.setBackgroundResource(R.drawable.arrow_down);
                                mDaysGainPercentView.setTextColor(Color.RED);
                            }
                            else if (mDaysGain > 0){
                                mDaysGainView.setTextColor(Color.parseColor("#20bd90"));
                                mArrowDaysGain.setBackgroundResource(R.drawable.arrow_up);
                                mDaysGainPercentView.setTextColor(Color.parseColor("#20bd90"));
                            }

                            TotalWorth tWorth = new TotalWorth(mTotalWorth);
                            mRef.child("NetWorth").child(mUser.getUid()).setValue(tWorth);

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


//    Calculate Net gain
    public void calculateNetGain(){

        DatabaseReference nwRef = mDatabase.getReference("NetWorth").child(mUser.getUid());
        final DatabaseReference invRef = mDatabase.getReference("TotalInvestment").child(mUser.getUid());
        nwRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 final double netWorth = dataSnapshot.child("totalWorth").getValue(double.class);

                invRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       double netInv = dataSnapshot.child("totalInvestment").getValue(double.class);

                        DecimalFormat fm = new DecimalFormat("#,###,###.00");
                        double netGain = netWorth - netInv;

                        mNetGainView.setText("Rs. "+fm.format(netGain));

                        double netGainPercentage =0;
                        try {
                            netGainPercentage = netGain / netInv * 100;

                        }catch (Exception e){
                            netGainPercentage = 0;
                        }

                        mNetGainPercentView.setText("("+String.format("%.2f",netGainPercentage)+"%)");

                            if (netGain < 0) {
                                mNetGainView.setTextColor(Color.RED);
                                mArrowNetGain.setBackgroundResource(R.drawable.arrow_down);
                                mNetGainPercentView.setTextColor(Color.RED);
                            } else if (netGain > 0) {
                                mNetGainView.setTextColor(Color.parseColor("#20bd90"));
                                mArrowNetGain.setBackgroundResource(R.drawable.arrow_up);
                                mNetGainPercentView.setTextColor(Color.parseColor("#20bd90"));
                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void populatePortfolio(){

    //final FirebaseDatabase database = FirebaseDatabase.getInstance();
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

                    stocksRef.child(model.getSymbol()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double currentPrice = dataSnapshot.child("price").getValue(double.class);
                            double diffAmount = dataSnapshot.child("diff").getValue(double.class);
                            double diffPercent = dataSnapshot.child("percent").getValue(double.class);

                            double netGainAmount = currentPrice * model.getQuantity();

                            ((TextView) portfolioViewHolder.currentPrice).setText(String.format("%.2f",currentPrice));
                            ((TextView) portfolioViewHolder.diffText).setText(String.format("%.2f",diffAmount));
                            ((TextView) portfolioViewHolder.percentText).setText("("+String.format("%.2f",diffPercent)+"%)");
                            ((TextView) portfolioViewHolder.netGainText).setText(String.format("%.2f",netGainAmount));


                            if (diffAmount < 0) {
                                ((TextView) portfolioViewHolder.currentPrice).setTextColor(Color.RED);
                                ((TextView) portfolioViewHolder.diffText).setTextColor(Color.RED);
                                ((TextView) portfolioViewHolder.percentText).setTextColor(Color.RED);
                                ((ImageView) portfolioViewHolder.imgViewPortfolio).setBackgroundResource(R.drawable.arrow_down);
                            } else if (diffAmount > 0) {
                                ((TextView) portfolioViewHolder.currentPrice).setTextColor(Color.parseColor("#20bd90"));
                                ((TextView) portfolioViewHolder.diffText).setTextColor(Color.parseColor("#20bd90"));
                                ((TextView) portfolioViewHolder.percentText).setTextColor(Color.parseColor("#20bd90"));
                                ((ImageView) portfolioViewHolder.imgViewPortfolio).setBackgroundResource(R.drawable.arrow_up);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    if (position % 2 == 0) {
                        portfolioViewHolder.stripView.setBackgroundResource(R.color.stripColor);
                    } else {
                        portfolioViewHolder.stripView.setBackgroundResource(R.color.white);
                    }

                    portfolioViewHolder.symbolText.setText(model.getSymbol());
                    portfolioViewHolder.quantityText.setText(Integer.toString(model.getQuantity()));
                    portfolioViewHolder.priceText.setText(String.format("%.2f",model.getBuyPrice()));
                    portfolioViewHolder.portfolioShareType.setText(model.getShareType());

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
        }
    }
}

