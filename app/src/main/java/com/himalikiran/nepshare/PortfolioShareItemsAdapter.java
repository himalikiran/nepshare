package com.himalikiran.nepshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by himalikiran on 9/16/2016.
 */
public class PortfolioShareItemsAdapter extends ArrayAdapter<PortfolioItems>{

    public PortfolioShareItemsAdapter(Activity context, ArrayList<PortfolioItems> portfolioitems) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0,portfolioitems);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.portfolio_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        PortfolioItems currentItem = getItem(position);

        // Find the TextView in the NEPSE_list_itemt_item.xml layout with the ID version_name
        TextView symbolTextView = (TextView) listItemView.findViewById(R.id.symbolText);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        symbolTextView.setText(currentItem.getSymbol());

        // Find the TextView in the NEPSE_list_itemt_item.xml layout with the ID version_number
        TextView quantityTextView = (TextView) listItemView.findViewById(R.id.quantityText);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        quantityTextView.setText(Integer.toString(currentItem.getQuantity()));

        TextView priceTextView = (TextView) listItemView.findViewById(R.id.priceText);
        priceTextView.setText(String.format("%.2f",(currentItem.getPrice())));


        TextView netGainTextView = (TextView) listItemView.findViewById(R.id.netGainText);
        netGainTextView.setText(String.format("%.2f",(currentItem.getNetGain())));

//        // Find the ImageView in the NEPSE_list_itemt_item.xml layout with the ID list_item_icon
//        ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_item_icon);
//        // Get the image resource ID from the current AndroidFlavor object and
//        // set the image to iconView
//        iconView.setImageResource(currentAndroidFlavor.getImageResourceId());

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
