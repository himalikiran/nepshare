package com.himalikiran.nepshare;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.himalikiran.nepshare.models.Stocks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by himalikiran on 8/4/2016.
 */
public class FetchStockDataService extends IntentService {
    //URL for fetching stock data from Nepal Stock Limited.
    String FETCH_URL = "http://nepalstock.com.np/events";
    private List<String> mFetchData;
    public static Context mContext;
    private DatabaseReference mDatabase;


    public FetchStockDataService() {

        super("FetchStockDataService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent){
        RequestQueue queue = Volley.newRequestQueue(this);

        // [START initialize_auth]
        //mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mDatabase = FirebaseDatabase.getInstance().getReference();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FETCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Elements data  = Jsoup.parse(response).select("marquee");  // response is HTML
                        Element txt = data.first();
                        //String allData = txt.text();

                        //ArrayList<Stocks> stocks = new ArrayList<Stocks>();
                        //StocksAdapter itemsAdapter = new StocksAdapter(,stocks);
                        double lastPrice=0;
                        List<String> rows = Arrays.asList(txt.text().split("\\u00a0\\u00a0\\s"));

                        for (int x = 0; x < rows.size(); x++) {
                            List<String> stockData = Arrays.asList(rows.get(x).split("\\s"));

                            float diff = (Float.parseFloat(stockData.get(6)));
                            try {
                                NumberFormat ukFormat = NumberFormat.getNumberInstance(Locale.UK);
                                lastPrice = ukFormat.parse(stockData.get(1)).doubleValue();

                            }
                            catch (java.text.ParseException e)
                            {
                                System.out.println("NumberFormatException: " + e.getMessage());
                            }
                            double per= 0;
                            try {
                                per = diff / lastPrice * 100;
                            } catch (Exception e){
                                per = 0;
                            }
                            Stocks stock = new Stocks(stockData.get(0), lastPrice, diff, per);
                            //stocks.add(new Stocks(stockData.get(0), lastPrice, change));
                            mDatabase.child("Stocks").child(stockData.get(0)).setValue(stock);

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
     * Constructor
     * @param
     */
//    public FetchStockData (){
//
//    }

    /**
     * Getter method to get the stock string
     */
    //public String getStockData(){
    //return mFetchData;



}
