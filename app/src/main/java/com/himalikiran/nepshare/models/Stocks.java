package com.himalikiran.nepshare.models;

/**
 * Created by himalikiran on 7/31/2016.
 */
public class Stocks {
    /**
     * Name of the Compaly listed in Nepal Stock Exchange.
     */
    private String company;
    private String companyFullName;
    private double price;
    private double diff;
    private double percent;


    public Stocks() {

    }

    /**
     * Constructor
     * @param company
     * @param price
     * @param diff
     */
    public Stocks(String company, double price, double diff) {
        this.company = company;
        this.price = price;
        this.diff = diff;
    }


    /**
    * Get the company name
    */
    public String getCompany(){
        //company = "Agriculture Development Bank Limited";
        return this.company;
    }

    /**
     * Get the companyFullName name
     */
//    public String getCompanyFullName(){
//       // companyFullName = "Agriculture Development Bank Limited";
//        return this.companyFullName;
//    }


    /**
     * Get the price
     */
    public double getPrice(){
        return this.price;
    }

    /**
     * Get the difference
     */
    public double getDiff(){ return this.diff;}

    public double getPercent(){
        try{
            double percent = this.diff / (getPrice() - this.diff) ;

        } catch (Exception e){
            double percent = 0;
        }

        return percent;
    }
}
