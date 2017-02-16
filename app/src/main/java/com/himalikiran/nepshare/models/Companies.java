package com.himalikiran.nepshare.models;

/**
 * Created by himalikiran on 9/29/2016.
 */

public class Companies {
    /**
     * Name of the Companies listed in Nepal Stock Exchange.
     */
    private String companySymbol;
    private String companyName;
    private String companySector;

    public Companies() {

    }

    public Companies(String companySymbol, String companyName, String companySector) {
        this.companySymbol = companySymbol;
        this.companyName = companyName;
        this.companySector = companySector;
    }

    public void setCompanySymbol(String companySymbol) {
        this.companySymbol = companySymbol;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanySector(String companySector) {
        this.companySector = companySector;
    }

    public String getCompanySymbol() {
        return companySymbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanySector() {
        return companySector;
    }


}


