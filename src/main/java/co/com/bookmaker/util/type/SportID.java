/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.util.type;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eduarc
 */
public class SportID {
    
    public static final int N_SPORTS = 15;
    
    public static final int SOCCER = 1;
    public static final int BASKETBALL = 2;
    public static final int BASEBALL = 3;
    public static final int FOOTBALL = 4;
    public static final int TENNIS = 5;
    public static final int WOMEN_TENNIS = 6;
    public static final int HOCKEY = 7;
    public static final int FUTSAL = 8;
    public static final int RUGBY_LEAGUE = 9;
    public static final int RUGBY_UNION = 10;
    public static final int BOXING = 11;
    public static final int MIXED_MARTIAL_ARTS = 12;
    
    public static final int VOLLEYBALL = 13;
    public static final int TABLE_TENNIS = 14;
    
    public static final String[] sportName;
    
    public static final List<String>[] periodsName;
    public static final int[] nPeriods;
    public static final int[] nTeams;
    
    static {
        sportName = new String[N_SPORTS];
        periodsName = new ArrayList[N_SPORTS];
        nPeriods = new int[N_SPORTS];
        nTeams = new int[N_SPORTS];
        
        nPeriods[0] = nTeams[0] = 0;
        
        sportName[SOCCER] = "Soccer";
        periodsName[SOCCER] = new ArrayList();
        periodsName[SOCCER].add("Match");
        periodsName[SOCCER].add("1st-Half");
        periodsName[SOCCER].add("2nd-Half");
        nPeriods[SOCCER] = 3;
        nTeams[SOCCER] = 2;
        
        sportName[BASKETBALL] = "Basketball";
        periodsName[BASKETBALL] = new ArrayList();
        periodsName[BASKETBALL].add("Game");
        periodsName[BASKETBALL].add("1st Half");
        periodsName[BASKETBALL].add("2nd Half");
        periodsName[BASKETBALL].add("1st Quarter");
        periodsName[BASKETBALL].add("2nd Quarter");
        periodsName[BASKETBALL].add("3rd Quarter");
        periodsName[BASKETBALL].add("4th Quarter");
        nPeriods[BASKETBALL] = 7;
        nTeams[BASKETBALL] = 2;
        
        sportName[BASEBALL] = "Baseball";
        periodsName[BASEBALL] = new ArrayList();
        periodsName[BASEBALL].add("Game");
        periodsName[BASEBALL].add("1st Half");
        periodsName[BASEBALL].add("2nd Half");
        nPeriods[BASEBALL] = 3;
        nTeams[BASEBALL] = 2;
        
        sportName[FOOTBALL] = "Football";
        periodsName[FOOTBALL] = new ArrayList();
        periodsName[FOOTBALL].add("Game");
        periodsName[FOOTBALL].add("1st Half");
        periodsName[FOOTBALL].add("2nd Half");
        periodsName[FOOTBALL].add("1st Quarter");
        periodsName[FOOTBALL].add("2nd Quarter");
        periodsName[FOOTBALL].add("3rd Quarter");
        periodsName[FOOTBALL].add("4th Quarter");
        nPeriods[FOOTBALL] = 7;
        nTeams[FOOTBALL] = 2;
        
        sportName[TENNIS] = "Tennis";
        periodsName[TENNIS] = new ArrayList();
        periodsName[TENNIS].add("Match");
        periodsName[TENNIS].add("1st Set");
        periodsName[TENNIS].add("2nd Set");
        periodsName[TENNIS].add("3rd Set");
        periodsName[TENNIS].add("4th Set");
        periodsName[TENNIS].add("5th Set");
        nPeriods[TENNIS] = 6;
        nTeams[TENNIS] = 2;
        
        sportName[WOMEN_TENNIS] = "Women's Tennis";
        periodsName[WOMEN_TENNIS] = new ArrayList();
        periodsName[WOMEN_TENNIS].add("Match");
        periodsName[WOMEN_TENNIS].add("1st Set");
        periodsName[WOMEN_TENNIS].add("2nd Set");
        periodsName[WOMEN_TENNIS].add("3rd Set");
        nPeriods[WOMEN_TENNIS] = 4;
        nTeams[WOMEN_TENNIS] = 2;
        
        sportName[HOCKEY] = "Hockey";
        periodsName[HOCKEY] = new ArrayList();
        periodsName[HOCKEY].add("Game");
        periodsName[HOCKEY].add("1st Period");
        periodsName[HOCKEY].add("2nd Period");
        periodsName[HOCKEY].add("3rd Period");
        nPeriods[HOCKEY] = 4;
        nTeams[HOCKEY] = 2;
        
        sportName[FUTSAL] = "Futsal";
        periodsName[FUTSAL] = new ArrayList();
        periodsName[FUTSAL].add("Game");
        periodsName[FUTSAL].add("1st Half");
        periodsName[FUTSAL].add("2nd Half");
        nPeriods[FUTSAL] = 3;
        nTeams[FUTSAL] = 2;
        
        sportName[RUGBY_LEAGUE] = "Rugby League";
        periodsName[RUGBY_LEAGUE] = new ArrayList();
        periodsName[RUGBY_LEAGUE].add("Match");
        periodsName[RUGBY_LEAGUE].add("1st Half");
        periodsName[RUGBY_LEAGUE].add("2nd Half");
        nPeriods[RUGBY_LEAGUE] = 3;
        nTeams[RUGBY_LEAGUE] = 2;
        
        
        sportName[RUGBY_UNION] = "Rugby Union";
        periodsName[RUGBY_UNION] = new ArrayList();
        periodsName[RUGBY_UNION].add("Match");
        periodsName[RUGBY_UNION].add("1st Half");
        periodsName[RUGBY_UNION].add("2nd Half");
        nPeriods[RUGBY_UNION] = 3;
        nTeams[RUGBY_UNION] = 2;
        
        sportName[BOXING] = "Boxing";
        periodsName[BOXING] = new ArrayList();
        periodsName[BOXING].add("Fight");
        nPeriods[BOXING] = 1;
        nTeams[BOXING] = 2;
     
        sportName[MIXED_MARTIAL_ARTS] = "MMA - Mixed Martial Arts";
        periodsName[MIXED_MARTIAL_ARTS] = new ArrayList();
        periodsName[MIXED_MARTIAL_ARTS].add("Fight");
        periodsName[MIXED_MARTIAL_ARTS].add("Round 1");
        periodsName[MIXED_MARTIAL_ARTS].add("Round 2");
        periodsName[MIXED_MARTIAL_ARTS].add("Round 3");
        periodsName[MIXED_MARTIAL_ARTS].add("Round 4");
        periodsName[MIXED_MARTIAL_ARTS].add("Round 5");
        nPeriods[MIXED_MARTIAL_ARTS] = 6;
        nTeams[MIXED_MARTIAL_ARTS] = 2;
        
        sportName[VOLLEYBALL] = "Volleyball";
        periodsName[VOLLEYBALL] = new ArrayList();
        periodsName[VOLLEYBALL].add("Match");
        periodsName[VOLLEYBALL].add("1st Set");
        periodsName[VOLLEYBALL].add("2nd Set");
        periodsName[VOLLEYBALL].add("3rd Set");
        periodsName[VOLLEYBALL].add("4th Set");
        periodsName[VOLLEYBALL].add("5th Set");
        nPeriods[VOLLEYBALL] = 6;
        nTeams[VOLLEYBALL] = 2;
        
        sportName[TABLE_TENNIS] = "Table Tennis";
        periodsName[TABLE_TENNIS] = new ArrayList();
        periodsName[TABLE_TENNIS].add("Match");
        periodsName[TABLE_TENNIS].add("1st Game");
        periodsName[TABLE_TENNIS].add("2nd Game");
        periodsName[TABLE_TENNIS].add("3rd Game");
        periodsName[TABLE_TENNIS].add("4th Game");
        periodsName[TABLE_TENNIS].add("5th Game");
        periodsName[TABLE_TENNIS].add("6th Game");
        nPeriods[TABLE_TENNIS] = 7;
        nTeams[TABLE_TENNIS] = 2;
        
    }
    
    public String str(int id) {
        return sportName[id];
    }

    public List<String> periodsName(int id) {
        return periodsName[id];
    }
    
    public int nPeriods(int id) {
        return nPeriods[id];
    }
    
    public int nTeams(int id) {
        return nTeams[id];
    }
    
    public int getSOCCER() {
        return SOCCER;
    }

    public int getBASKETBALL() {
        return BASKETBALL;
    }

    public int getBASEBALL() {
        return BASEBALL;
    }

    public int getTENNIS() {
        return TENNIS;
    }

    public int getN_SPORTS() {
        return N_SPORTS;
    }

    public int getFOOTBALL() {
        return FOOTBALL;
    }

    public int getWOMEN_TENNIS() {
        return WOMEN_TENNIS;
    }

    public int getHOCKEY() {
        return HOCKEY;
    }

    public int getFUTSAL() {
        return FUTSAL;
    }

    public int getRUGBY_LEAGUE() {
        return RUGBY_LEAGUE;
    }

    public int getRUGBY_UNION() {
        return RUGBY_UNION;
    }

    public int getBOXING() {
        return BOXING;
    }

    public int getMIXED_MARTIAL_ARTS() {
        return MIXED_MARTIAL_ARTS;
    }

    public int getVOLLEYBALL() {
        return VOLLEYBALL;
    }

    public int getTABLE_TENNIS() {
        return TABLE_TENNIS;
    }
}
