/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.util.form.bean;

import java.util.List;

/**
 *
 * @author eduarc
 */
public class MatchResultBean {
    
    Integer nPeriods;
    Integer nTeams;
    
    Long sportId;
    Long matchId;
    String matchName;
    String tournamentName;
    String sportName;
    String teamName[];
    String score[][];
    List<String> periodNames;

    public MatchResultBean(int p, int t) {
        
        nPeriods = p;
        nTeams = t;
        teamName = new String[t];
        score = new String[p][t];
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
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

    public Long getSportId() {
        return sportId;
    }

    public void setSportId(Long sportId) {
        this.sportId = sportId;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getTeamName(int t) {
        return teamName[t];
    }

    public void setTeamName(Integer t, String name) {
        this.teamName[t] = name;
    }

    public String getScore(int p, int t) {
        return score[p][t];
    }

    public void setScore(int p, int t, String v) {
        this.score[p][t] = v;
    }

    public String getPeriodName(int p) {
        return periodNames.get(p);
    }

    public void setPeriodNames(List<String> periodNames) {
        this.periodNames = periodNames;
    }
}
