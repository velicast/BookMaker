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
public class Attribute {
    
    public static final String ACTIVE_SESSIONS = "_a_s";
    
    public static final String SESSION_USER = "a-0";
    public static final String SESSION_ROLE = "a-1";
    
    public static final String FINAL_USER = "a0";
    public static final String AGENCY = "a1";
    public static final String MANAGER = "a2";
    public static final String CURRENT_MANAGER = "a3";
    public static final String EMPLOYEE = "a4";
    public static final String ADD_EMPLOYEE = "a5";
    public static final String REM_EMPLOYEE = "a6";
    public static final String LIST_AGENCIES = "a7";
    public static final String ROLE_SELECTION = "a8";
    public static final String USERNAME = "a9";
    
    public static final String SPORT = "a10";
    
    public static final String MIN_ODDS = "a11";
    public static final String MAX_ODDS = "a12";
    public static final String MAX_PROFIT = "a13";
    
    public static final String PARLAY = "a14";
    
    public static final String IN_QUEUE = "a15";
    
    public static final String EMPLOYEES = "a16";
    
    public static final String PARLAY_ODDS = "a17";
    public static final String PARLAY_ODD = "a18";
    public static final String TRACK_PARLAY = "a20";
    public static final String PARLAYS = "a22";
    public static final String USERS = "a21";
    
    public static final String SPORTS = "a23";
    public static final String TOURNAMENT = "a24";
    public static final String ACTIVE_MATCHES = "a25";
    public static final String TOURNAMENTS = "a26";
    public static final String MATCH_EVENT = "a27";

    public static final String HOME_TEAM = "a28";
    public static final String AWAY_TEAM = "a29";
    
    public static final String NTEAMS = "a30";
    public static final String NPERIODS = "a31";
    public static final String PERIODS_NAME = "a32";
    
    public static final String TOTAL_OVER_POINTS = "a33";
    public static final String TOTAL_OVER_VALUE = "a34";
    public static final String TOTAL_UNDER_POINTS = "a35";
    public static final String TOTAL_UNDER_VALUE = "a36";
    public static final String MONEY_LINE = "a37";
    public static final String SPREAD = "a38";
    
    public static final String SPORT_SERVICE = "a39";
    public static final String TOURNAMENT_SERVICE = "a40";
    public static final String MATCH_EVENT_SERVICE = "a41";
    public static final String MATCH_PERIOD_SERVICE = "a42";
    public static final String PARLAYODD_SERVICE = "a43";
    public static final String PARLAY_SERVICE = "a44";
    public static final String TEAM_SERVICE = "a45";
    public static final String AGENCY_SERVICE = "a46";
    public static final String FINALUSER_SERVICE = "a47";
    public static final String SCORE_SERVICE = "a48";
    
    public static final String PASSWORD = "a49";
    public static final String EMAIL = "a50";
    public static final String BIRTH_DATE = "a51";
    public static final String FIRST_NAME = "a52";
    public static final String LAST_NAME = "a53";
    public static final String CITY = "a54";
    public static final String ADDRESS = "a55";
    public static final String TELEPHONE = "a56";
    public static final String STATUS = "a57";
    public static final String ROLE_ADMIN = "a58";
    public static final String ROLE_MANAGER = "a59";
    public static final String ROLE_ANALYST = "a60";
    public static final String ROLE_SELLER = "a61";
    public static final String ROLE_CLIENT = "a62";
    public static final String TARGET_USERNAME = "a63";
    
    public static final String TEAMS = "a64";
    public static final String PERIODS = "a65";
    public static final String MATCH_RESULT = "a66";
    
    public static final String TIME_FROM = "a67";
    public static final String TIME_TO = "a68";
    
    public static final String REVENUE = "a69";
    public static final String COST = "a70";
    public static final String PROFIT = "a71";
    
    public static final String MATCHES = "a72";
    public static final String INACTIVE_MATCHES = "a73";
    public static final String CANCELLED_MATCHES = "a74";
    public static final String PENDING_MATCHES = "a75";
    public static final String FINISHED_MATCHES = "a76";
    
    public static final String AGENCIES = "a77";
    public static final String ACTIVE_AGENCIES = "a78";
    public static final String INACTIVE_AGENCIES = "a79";
    
    public static final String ROLE = "a80";
    
    public static final String ACTIVE_USERS = "a81";
    public static final String INACTIVE_USERS = "a82";
    
    public static final String COMPILED_TICKED_REPORT = "a83";
    
    public String getSESSION_USER() {
        return SESSION_USER;
    }

    public String getSESSION_ROLE() {
        return SESSION_ROLE;
    }
    
    public String getFINAL_USER() {
        return FINAL_USER;
    }
    
    public String getAGENCY() {
        return AGENCY;
    }
    
    public String getMANAGER() {
        return MANAGER;
    }
    
    public String getCURRENT_MANAGER() {
        return CURRENT_MANAGER;
    }
    
    public String getEMPLOYEE() {
        return EMPLOYEE;
    }
    
    public String getADD_EMPLOYEE() {
        return ADD_EMPLOYEE;
    }

    public String getREM_EMPLOYEE() {
        return REM_EMPLOYEE;
    }
    
    public String getLIST_AGENCIES() {
        return LIST_AGENCIES;
    }
    
    public String getUSERNAME() {
        return USERNAME;
    }
    
    public String getROLE_SELECTION() {
        return ROLE_SELECTION;
    }
    
    public String getSPORT() {
        return SPORT;
    }

    public String getMIN_ODDS() {
        return MIN_ODDS;
    }

    public String getMAX_ODDS() {
        return MAX_ODDS;
    }

    public String getMAX_PROFIT() {
        return MAX_PROFIT;
    }

    public String getPARLAY() {
        return PARLAY;
    }

    public String getIN_QUEUE() {
        return IN_QUEUE;
    }
    
    public String getEMPLOYEES() {
        return EMPLOYEES;
    }

    public String getACTIVE_SESSIONS() {
        return ACTIVE_SESSIONS;
    }

    public String getPARLAY_ODDS() {
        return PARLAY_ODDS;
    }

    public String getPARLAY_ODD() {
        return PARLAY_ODD;
    }

    public String getTEAM_SERVICE() {
        return TEAM_SERVICE;
    }

    public String getTRACK_PARLAY() {
        return TRACK_PARLAY;
    }

    public String getUSERS() {
        return USERS;
    }

    public String getPARLAYS() {
        return PARLAYS;
    }

    public String getSPORTS() {
        return SPORTS;
    }

    public String getTOURNAMENT() {
        return TOURNAMENT;
    }

    public String getACTIVE_MATCHES() {
        return ACTIVE_MATCHES;
    }

    public String getTOURNAMENTS() {
        return TOURNAMENTS;
    }

    public String getMATCH_EVENT() {
        return MATCH_EVENT;
    }

    public String getHOME_TEAM() {
        return HOME_TEAM;
    }

    public String getAWAY_TEAM() {
        return AWAY_TEAM;
    }

    public String getNTEAMS() {
        return NTEAMS;
    }

    public String getNPERIODS() {
        return NPERIODS;
    }
    
    public String getPERIODS_NAME() {
        return PERIODS_NAME;
    }
    
    public String getTOTAL_OVER_POINTS() {
        return TOTAL_OVER_POINTS;
    }

    public String getTOTAL_OVER_VALUE() {
        return TOTAL_OVER_VALUE;
    }

    public String getTOTAL_UNDER_POINTS() {
        return TOTAL_UNDER_POINTS;
    }

    public String getTOTAL_UNDER_VALUE() {
        return TOTAL_UNDER_VALUE;
    }

    public String getMONEY_LINE() {
        return MONEY_LINE;
    }

    public String getSPREAD() {
        return SPREAD;
    }

    public String getSPORT_SERVICE() {
        return SPORT_SERVICE;
    }

    public String getTOURNAMENT_SERVICE() {
        return TOURNAMENT_SERVICE;
    }

    public String getMATCH_EVENT_SERVICE() {
        return MATCH_EVENT_SERVICE;
    }

    public String getMATCH_PERIOD_SERVICE() {
        return MATCH_PERIOD_SERVICE;
    }

    public String getPARLAYODD_SERVICE() {
        return PARLAYODD_SERVICE;
    }

    public String getPARLAY_SERVICE() {
        return PARLAY_SERVICE;
    }

    public String getAGENCY_SERVICE() {
        return AGENCY_SERVICE;
    }

    public String getFINALUSER_SERVICE() {
        return FINALUSER_SERVICE;
    }

    public String getSCORE_SERVICE() {
        return SCORE_SERVICE;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public String getBIRTH_DATE() {
        return BIRTH_DATE;
    }

    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public String getCITY() {
        return CITY;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public String getTELEPHONE() {
        return TELEPHONE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public String getROLE_ADMIN() {
        return ROLE_ADMIN;
    }

    public String getROLE_MANAGER() {
        return ROLE_MANAGER;
    }

    public String getROLE_ANALYST() {
        return ROLE_ANALYST;
    }

    public String getROLE_SELLER() {
        return ROLE_SELLER;
    }

    public String getROLE_CLIENT() {
        return ROLE_CLIENT;
    }    

    public String getTARGET_USERNAME() {
        return TARGET_USERNAME;
    }    

    public String getTEAMS() {
        return TEAMS;
    }

    public String getPERIODS() {
        return PERIODS;
    }

    public String getMATCH_RESULT() {
        return MATCH_RESULT;
    }

    public String getTIME_FROM() {
        return TIME_FROM;
    }

    public String getTIME_TO() {
        return TIME_TO;
    }

    public String getREVENUE() {
        return REVENUE;
    }

    public String getCOST() {
        return COST;
    }

    public String getPROFIT() {
        return PROFIT;
    }

    public String getMATCHES() {
        return MATCHES;
    }
    
    public String getINACTIVE_MATCHES() {
        return INACTIVE_MATCHES;
    }

    public String getCANCELLED_MATCHES() {
        return CANCELLED_MATCHES;
    }

    public String getPENDING_MATCHES() {
        return PENDING_MATCHES;
    }

    public String getFINISHED_MATCHES() {
        return FINISHED_MATCHES;
    }

    public String getROLE() {
        return ROLE;
    }

    public String getAGENCIES() {
        return AGENCIES;
    }

    public String getACTIVE_AGENCIES() {
        return ACTIVE_AGENCIES;
    }

    public String getINACTIVE_AGENCIES() {
        return INACTIVE_AGENCIES;
    }

    public String getACTIVE_USERS() {
        return ACTIVE_USERS;
    }

    public String getINACTIVE_USERS() {
        return INACTIVE_USERS;
    }

    public static String getCOMPILED_TICKED_REPORT() {
        return COMPILED_TICKED_REPORT;
    }
}
