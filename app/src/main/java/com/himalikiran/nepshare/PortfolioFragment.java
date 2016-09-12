package com.himalikiran.nepshare;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PortfolioFragment extends Fragment {

    private ArrayAdapter<String> mPortfolioAdapter;

    public PortfolioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(com.himalikiran.nepshare.R.layout.fragment_portfolio, container, false);

        String[] portfolioArray = {
                "EBL 3300.00 -70 500 1650000",
                "EBL 3300.00 -70 500 1650000",
                "EBL 3300.00 -70 500 1650000",
                "EBL 3300.00 -70 500 1650000",
                "EBL 3300.00 -70 500 1650000",
                "EBL 3300.00 -70 500 1650000",
                "EBL 3300.00 -70 500 1650000",
                "EBL 3300.00 -70 500 1650000",
                "EBL 3300.00 -70 500 1650000",
        };


        List<String> myPortfolio = new ArrayList<String>(Arrays.asList(portfolioArray));

        mPortfolioAdapter = new ArrayAdapter<String>(
                //The current context (this fragment's parent activity)
                getActivity(),
                //ID of list item layout
                R.layout.portfolio_list_item,
                //ID of views
                R.id.myStocks,
                //data
                myPortfolio);

        ListView listview = (ListView) rootView.findViewById(R.id.myStockList);
        listview.setAdapter(mPortfolioAdapter);



        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        //when the fragment is stopped, release the memory if there.
    }
}
