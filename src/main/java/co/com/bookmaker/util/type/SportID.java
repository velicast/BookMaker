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
        
        sportName[SOCCER] = "Fútbol";
        periodsName[SOCCER] = new ArrayList();
        periodsName[SOCCER].add("Juego");
        periodsName[SOCCER].add("1er Tiempo");
        periodsName[SOCCER].add("2do Tiempo");
        nPeriods[SOCCER] = 3;
        nTeams[SOCCER] = 2;
        
        sportName[BASKETBALL] = "Básquetbol";
        periodsName[BASKETBALL] = new ArrayList();
        periodsName[BASKETBALL].add("Juego");
        periodsName[BASKETBALL].add("1er Tiempo");
        periodsName[BASKETBALL].add("2do Tiempo");
        periodsName[BASKETBALL].add("1er Cuarto");
        periodsName[BASKETBALL].add("2do Cuarto");
        periodsName[BASKETBALL].add("3er Cuarto");
        periodsName[BASKETBALL].add("4to Cuarto");
        nPeriods[BASKETBALL] = 7;
        nTeams[BASKETBALL] = 2;
        
        sportName[BASEBALL] = "Béisbol";
        periodsName[BASEBALL] = new ArrayList();
        periodsName[BASEBALL].add("Juego");
        periodsName[BASEBALL].add("1er Tiempo");
        periodsName[BASEBALL].add("2do Tiempo");
        nPeriods[BASEBALL] = 3;
        nTeams[BASEBALL] = 2;
        
        sportName[FOOTBALL] = "Fútbol Americano";
        periodsName[FOOTBALL] = new ArrayList();
        periodsName[FOOTBALL].add("Juego");
        periodsName[FOOTBALL].add("1er Tiempo");
        periodsName[FOOTBALL].add("2do Tiempo");
        periodsName[FOOTBALL].add("1er Cuarto");
        periodsName[FOOTBALL].add("2do Cuarto");
        periodsName[FOOTBALL].add("3er Cuarto");
        periodsName[FOOTBALL].add("4to Cuarto");
        nPeriods[FOOTBALL] = 7;
        nTeams[FOOTBALL] = 2;
        
        sportName[TENNIS] = "Tenis";
        periodsName[TENNIS] = new ArrayList();
        periodsName[TENNIS].add("Juego");
        periodsName[TENNIS].add("1er Set");
        periodsName[TENNIS].add("2do Set");
        periodsName[TENNIS].add("3er Set");
        periodsName[TENNIS].add("4to Set");
        periodsName[TENNIS].add("5to Set");
        nPeriods[TENNIS] = 6;
        nTeams[TENNIS] = 2;
        
        sportName[WOMEN_TENNIS] = "Tenis Femenino";
        periodsName[WOMEN_TENNIS] = new ArrayList();
        periodsName[WOMEN_TENNIS].add("Juego");
        periodsName[WOMEN_TENNIS].add("1er Set");
        periodsName[WOMEN_TENNIS].add("2do Set");
        periodsName[WOMEN_TENNIS].add("3er Set");
        nPeriods[WOMEN_TENNIS] = 4;
        nTeams[WOMEN_TENNIS] = 2;
        
        sportName[HOCKEY] = "Hockey";
        periodsName[HOCKEY] = new ArrayList();
        periodsName[HOCKEY].add("Juego");
        periodsName[HOCKEY].add("1er Periodo");
        periodsName[HOCKEY].add("2do Periodo");
        periodsName[HOCKEY].add("3er Periodo");
        nPeriods[HOCKEY] = 4;
        nTeams[HOCKEY] = 2;
        
        sportName[FUTSAL] = "Fútsal";
        periodsName[FUTSAL] = new ArrayList();
        periodsName[FUTSAL].add("Juego");
        periodsName[FUTSAL].add("1er Tiempo");
        periodsName[FUTSAL].add("2do Tiempo");
        nPeriods[FUTSAL] = 3;
        nTeams[FUTSAL] = 2;
        
        sportName[RUGBY_LEAGUE] = "Rugby League";
        periodsName[RUGBY_LEAGUE] = new ArrayList();
        periodsName[RUGBY_LEAGUE].add("Juego");
        periodsName[RUGBY_LEAGUE].add("1er Tiempo");
        periodsName[RUGBY_LEAGUE].add("2do Tiempo");
        nPeriods[RUGBY_LEAGUE] = 3;
        nTeams[RUGBY_LEAGUE] = 2;
        
        
        sportName[RUGBY_UNION] = "Rugby Union";
        periodsName[RUGBY_UNION] = new ArrayList();
        periodsName[RUGBY_UNION].add("Juego");
        periodsName[RUGBY_UNION].add("1er Tiempo");
        periodsName[RUGBY_UNION].add("2do Tiempo");
        nPeriods[RUGBY_UNION] = 3;
        nTeams[RUGBY_UNION] = 2;
        
        sportName[BOXING] = "Boxeo";
        periodsName[BOXING] = new ArrayList();
        periodsName[BOXING].add("Pelea");
        nPeriods[BOXING] = 1;
        nTeams[BOXING] = 2;
     
        sportName[MIXED_MARTIAL_ARTS] = "Artes Marciales Mixtas";
        periodsName[MIXED_MARTIAL_ARTS] = new ArrayList();
        periodsName[MIXED_MARTIAL_ARTS].add("Pelea");
        periodsName[MIXED_MARTIAL_ARTS].add("Round 1");
        periodsName[MIXED_MARTIAL_ARTS].add("Round 2");
        periodsName[MIXED_MARTIAL_ARTS].add("Round 3");
        periodsName[MIXED_MARTIAL_ARTS].add("Round 4");
        periodsName[MIXED_MARTIAL_ARTS].add("Round 5");
        nPeriods[MIXED_MARTIAL_ARTS] = 6;
        nTeams[MIXED_MARTIAL_ARTS] = 2;
        
        sportName[VOLLEYBALL] = "Voleibol";
        periodsName[VOLLEYBALL] = new ArrayList();
        periodsName[VOLLEYBALL].add("Juego");
        periodsName[VOLLEYBALL].add("1er Set");
        periodsName[VOLLEYBALL].add("2do Set");
        periodsName[VOLLEYBALL].add("3er Set");
        periodsName[VOLLEYBALL].add("4to Set");
        periodsName[VOLLEYBALL].add("5to Set");
        nPeriods[VOLLEYBALL] = 6;
        nTeams[VOLLEYBALL] = 2;
        
        sportName[TABLE_TENNIS] = "Tenis de Mesa";
        periodsName[TABLE_TENNIS] = new ArrayList();
        periodsName[TABLE_TENNIS].add("Juego");
        periodsName[TABLE_TENNIS].add("1er Game");
        periodsName[TABLE_TENNIS].add("2do Game");
        periodsName[TABLE_TENNIS].add("3er Game");
        periodsName[TABLE_TENNIS].add("4to Game");
        periodsName[TABLE_TENNIS].add("5to Game");
        periodsName[TABLE_TENNIS].add("6to Game");
        nPeriods[TABLE_TENNIS] = 7;
        nTeams[TABLE_TENNIS] = 2;
        
    }
    
    public String str(int id) {
        return sportName[id];
    }

    public List<String> periodsName(int id) {
        return periodsName[id];
    }
    
    public String periodName(int sportID, int period) {
        return periodsName[sportID].get(period);
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
