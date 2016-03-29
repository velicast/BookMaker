/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.util.form.bean;

import co.com.bookmaker.data_access.entity.event.Tournament;
import java.util.List;

/**
 *
 * @author eduarc
 */
public class MatchEventBean {
    
    Long id;
    Integer nPeriods;
    Integer nTeams;
    Integer sportId;
    Integer tournamentId;
    String name;
    String startDate;
    Integer status;
    
    String drawLine[];
    String totalPoints[];
    String lineUnder[];
    String lineOver[];
    String moneyLine[][];
    String teamName[];
    String spreadPoints[];
    String spreadLineTeam0[];
    String spreadLineTeam1[];
    
    List<String> periodNames;

    Tournament tournament;
    
    public MatchEventBean() {
        
    }
    
    public MatchEventBean(int periods, int teams) {
        
        drawLine = new String[periods];
        totalPoints = new String[periods];
        lineUnder = new String[periods];
        lineOver = new String[periods];
        moneyLine  = new String[periods][teams];
        teamName = new String[teams];
        spreadPoints  = new String[periods];
        spreadLineTeam0 = new String[periods];
        spreadLineTeam1 = new String[periods];
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getnPeriods() {
        return nPeriods;
    }

    public void setnPeriods(Integer nPeriods) {
        this.nPeriods = nPeriods;
    }

    public Integer getnTeams() {
        return nTeams;
    }

    public void setnTeams(Integer nTeams) {
        this.nTeams = nTeams;
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    
    public Integer getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public void setTeam(int t, String v) {
        teamName[t] = v;
    }
    
    public void setTotalPoints(int p, String v) {
        totalPoints[p] = v;
    }
    
    public void setLineUnder(int p, String v) {
        lineUnder[p] = v;
    }
    
    public void setLineOver(int p, String v) {
        lineOver[p] = v;
    }
    
    public void setSpreadPoints(int p, String v) {
        spreadPoints[p] = v;
    }
    
    public void setMoneyLine(int p, int t, String v) {
        moneyLine[p][t] = v;
    }
    
    public void setLineTeam0(int p, String v) {
        spreadLineTeam0[p] = v;
    }
    
    public void setLineTeam1(int p, String v) {
        spreadLineTeam1[p] = v;
    }
    
    public void setDrawLine(int p, String v) {
        drawLine[p] = v;
    }
    
    public String getTeam(int t) {
        return teamName[t] == null ? "Team"+t : teamName[t];
    }
    
    public String getTotalPoints(int p) {
        return totalPoints[p];
    }
    
    public String getLineUnder(int p) {
        return lineUnder[p];
    }
    
    public String getLineOver(int p) {
        return lineOver[p];
    }
    
    public String getSpreadPoints(int p) {
        return spreadPoints[p];
    }
    
    public String getMoneyLine(int p, int t) {
        return moneyLine[p][t];
    }
    
    public String getLineTeam0(int p) {
        return spreadLineTeam0[p];
    }
    
    public String getLineTeam1(int p) {
        return spreadLineTeam1[p];
    }

    public String getDrawLine(int p) {
        return drawLine[p];
    }
    
    public void setPeriodNames(List<String> periodNames) {
        this.periodNames = periodNames;
    }
    
    public String getPeriodName(int p) {
        return periodNames.get(p);
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Tournament getTournament() {
        return tournament;
    }   
}