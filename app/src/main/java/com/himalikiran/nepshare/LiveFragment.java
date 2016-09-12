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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    String fetch_url = "http://nepalstock.com.np/events";
    private static Context mContext;
   // private Activity mActivity;

    public LiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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


                       List<String> rows = Arrays.asList(allData.split("\\u00a0\\u00a0\\s"));

                       for (int x = 0; x <= 50; x++) {
                           List<String> stockData = Arrays.asList(rows.get(x).split("\\s"));
                           float change = (Float.valueOf(stockData.get(6))).floatValue();
                           stocks.add(new Stocks(stockData.get(0), stockData.get(1), change));

                       }
                       ListView companyList = (ListView) getActivity().findViewById(R.id.stockList);
                       companyList.setAdapter(itemsAdapter);
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
