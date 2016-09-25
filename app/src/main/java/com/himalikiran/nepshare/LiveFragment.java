package com.himalikiran.nepshare;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himalikiran.nepshare.models.Stocks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.N;
import static com.google.android.gms.analytics.internal.zzy.m;
import static com.google.android.gms.analytics.internal.zzy.n;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    String fetch_url = "http://nepalstock.com.np/events";
    private static Context mContext;



    private DatabaseReference mDatabase;
    //private myRef = DatabaseReference.get

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


   // private Activity mActivity;

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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prepareData();
                //Toast.makeText(getActivity(), "Latest NEPSE data updated!",Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

       // prepareData();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        prepareData();
    }

    public void prepareData( ){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fetch_url,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {

                       Document doc = Jsoup.parse(response);  // response is HTML
                       Elements data = doc.select("marquee");
                       Element txt = data.first();
                       String allData = txt.text();

                       ArrayList<Stocks> stocks = new ArrayList<Stocks>();

                       StocksAdapter itemsAdapter = new StocksAdapter(getActivity(), stocks);

                       double lastPrice=0;
                       List<String> rows = Arrays.asList(allData.split("\\u00a0\\u00a0\\s"));

                       for (int x = 0; x <= 50; x++) {
                           List<String> stockData = Arrays.asList(rows.get(x).split("\\s"));

                           float change = (Float.valueOf(stockData.get(6))).floatValue();
                           try {
                               NumberFormat ukFormat = NumberFormat.getNumberInstance(Locale.UK);
                               lastPrice = ukFormat.parse(stockData.get(1)).doubleValue();

                           }
                           catch (java.text.ParseException e)
                           {
                               System.out.println("NumberFormatException: " + e.getMessage());
                           }

                           stocks.add(new Stocks(stockData.get(0), lastPrice, change));

                       }
                       ListView companyList = (ListView) getActivity().findViewById(R.id.stockList);
                       companyList.setAdapter(itemsAdapter);

                       mDatabase.child("Stocks").setValue(stocks);
                   }
                    }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {

                           Toast.makeText(getActivity(), "Connection error! Try again.",Toast.LENGTH_LONG).show();
                       }
                   });
                   queue.add(stringRequest);
    }

}
