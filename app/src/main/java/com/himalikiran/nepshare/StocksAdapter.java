package com.himalikiran.nepshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by himalikiran on 7/31/2016.
 */
public class StocksAdapter extends ArrayAdapter<Stocks> {
    public StocksAdapter(Activity context, ArrayList<Stocks> stocks) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0,stocks);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    com.himalikiran.nepshare.R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Stocks currentStock = getItem(position);


        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView titleTextView = (TextView) listItemView.findViewById(com.himalikiran.nepshare.R.id.titleText);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        titleTextView.setText(currentStock.getCompany());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView subTitleTextView = (TextView) listItemView.findViewById(com.himalikiran.nepshare.R.id.subTitleText);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        subTitleTextView.setText(currentStock.getPrice());

        // Find the ImageView in the list_item.xml layout with the ID list_item_icon
        //ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_item_icon);
        // Get the image resource ID from the current AndroidFlavor object and
        // set the image to iconView
        //iconView.setImageResource(currentAndroidFlavor.getImageResourceId());

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView


        if (currentStock.getDiff() < 0){
            subTitleTextView.setTextColor(listItemView.getResources().getColor(com.himalikiran.nepshare.R.color.priceDecrease));
        }
        else if(currentStock.getDiff() > 0){
           // String diff = "+" + Float.toString(currentStock.getDiff());
            subTitleTextView.setTextColor(listItemView.getResources().getColor(com.himalikiran.nepshare.R.color.priceIncrease));
        }

        TextView totalDiff = (TextView) listItemView.findViewById(com.himalikiran.nepshare.R.id.totalDiff);
        totalDiff.setText(Float.toString(currentStock.getDiff()));


        TextView percentChange = (TextView) listItemView.findViewById(com.himalikiran.nepshare.R.id.percentChange);
        percentChange.setText("( " + String.format("%.2f",currentStock.getPercent())+"% )");
//        percentChange.setText("( " + Float.toString(percent)+"% )");

        return listItemView;
    }
}
