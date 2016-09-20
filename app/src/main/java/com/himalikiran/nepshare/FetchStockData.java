package com.himalikiran.nepshare;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by himalikiran on 8/4/2016.
 */
public class FetchStockData {
    //URL for fetching stock data from Nepal Stock Limited.
    String FETCH_URL = "http://nepalstock.com.np/events";
    private List<String> mFetchData;
    private static Context mContext;
    /**
     * Constructor
     * @param
     */
    public FetchStockData (){
        RequestQueue queue = Volley.newRequestQueue(mContext.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FETCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Elements data  = Jsoup.parse(response).select("marquee");  // response is HTML
                        //Elements data = doc.select("marquee");
                        Element txt = data.first();
                        List<String> rows = Arrays.asList(txt.text().split("\\u00a0\\u00a0\\s"));
                        ArrayList<Stocks> stocks = new ArrayList<Stocks>();

                        for (int x = 0; x <= 50; x++) {
                            List<String> stockData = Arrays.asList(rows.get(x).split("\\s"));
                            float change = (Float.valueOf(stockData.get(6))).floatValue();
                            stocks.add(new Stocks(stockData.get(0), stockData.get(1), change));

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(mContext.getApplicationContext(), "Connection error! Try again.",Toast.LENGTH_LONG).show();

            }
        });
        queue.add(stringRequest);
    }

    /**
     * Getter method to get the stock string
     */
    //public String getStockData(){
        //return mFetchData;



}
