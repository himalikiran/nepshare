package com.himalikiran.nepshare;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by himalikiran on 8/3/2016.
 */
public class TabAdapter extends FragmentPagerAdapter {
    /** Context of the app */
    private Context mContext;

    public TabAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PortfolioFragment();
        } else if (position == 1) {
            return new WatchlistFragment();
        }
        else {
            return new LiveFragment();
        }
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.tab_portfolio);
        } else if (position == 1) {
            return mContext.getString(R.string.tab_watchlist);
        }
        else {
            return mContext.getString(R.string.tab_live);
        }
    }

}
