package com.himalikiran.nepshare.models;

/**
 * Created by himalikiran on 7/31/2016.
 */
public class Stocks {
    /**
     * Name of the Compaly listed in Nepal Stock Exchange.
     */
    private String mCompany;
    /**
     * Last Price of the stock.
     */
    private String mPrice;

    private float mDiff;

    /**
     * Constructor
     * @param Company
     * @param Price
     */
    public Stocks(String Company, String Price, float Diff){
        mCompany = Company;
        mPrice = Price;
        mDiff = Diff;
    }

    /**
    * Get the company name
    */
    public String getCompany(){
        return mCompany;
    }

     /**
     * Get the price
     */
    public String getPrice(){
        return mPrice;
    }

    /**
     * Get the difference
     */
    public float getDiff(){ return mDiff;}

    public float getPercent(){
        float price=0;
        try
        {
            price = Float.valueOf(mPrice.replaceAll(",",""));
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("NumberFormatException: " + nfe.getMessage());
        }

        float percent = mDiff / (price - mDiff) ;
        return percent;
    }
}
