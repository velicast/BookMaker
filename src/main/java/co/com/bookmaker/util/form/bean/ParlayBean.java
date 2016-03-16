package co.com.bookmaker.util.form.bean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;

/**
 *
 * @author eduarc
 */
public class ParlayBean {
    
    String id;
    String risk;
    String profit;
    String date;
    String agency;
    List<ParlayOddBean> odds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public List<ParlayOddBean> getOdds() {
        return odds;
    }

    public void setOdds(List<ParlayOddBean> odds) {
        this.odds = odds;
    }
}
