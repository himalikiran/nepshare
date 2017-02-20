package com.himalikiran.nepshare.models;

/**
 * Created by himalikiran on 7/31/2016.
 */
public class Stocks {
    /**
     * Name of the Compaly listed in Nepal Stock Exchange.
     */
    private String company;
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
     * @param percent
     */
    public Stocks(String company, double price, double diff, double percent) {
        this.company = company;
        this.price = price;
        this.diff = diff;
        this.percent = percent;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
