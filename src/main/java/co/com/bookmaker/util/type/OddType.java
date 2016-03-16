/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.util.type;

/**
 *
 * @author eduarc
 */
public class OddType {
 
    public static final int MONEY_LINE = 0;
    public static final int SPREAD_TEAM0 = 1;
    public static final int SPREAD_TEAM1 = 2;
    public static final int TOTAL_OVER = 3;
    public static final int TOTAL_UNDER = 4;
    public static final int DRAW_LINE = 5;

    public static final String[] typeName;
    
    static {
        typeName = new String[6];
        typeName[MONEY_LINE] = "Moneyline";
        typeName[SPREAD_TEAM0] = "Spread";
        typeName[SPREAD_TEAM1] = "Spread";
        typeName[TOTAL_OVER] = "Total";
        typeName[TOTAL_UNDER] = "Total";
        typeName[DRAW_LINE] = "Draw";
    }
    
    public String str(int type) {
        return typeName[type];
    }
    
    public int getMONEY_LINE() {
        return MONEY_LINE;
    }

    public int getSPREAD_TEAM0() {
        return SPREAD_TEAM0;
    }

    public int getSPREAD_TEAM1() {
        return SPREAD_TEAM1;
    }

    public int getTOTAL_OVER() {
        return TOTAL_OVER;
    }

    public int getTOTAL_UNDER() {
        return TOTAL_UNDER;
    }

    public int getDRAW_LINE() {
        return DRAW_LINE;
    }
}
