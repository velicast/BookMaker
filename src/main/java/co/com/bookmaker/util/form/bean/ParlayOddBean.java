package co.com.bookmaker.util.form.bean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author eduarc
 */
public class ParlayOddBean {
    
    String odd;
    String team;
    String match;
    String startDate;

    public String getOdd() {
        return odd;
    }

    public void setOdd(String odd) {
        this.odd = odd;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }   
}
