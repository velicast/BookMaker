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
public final class Status {
    
    public static final int ACTIVE = 0;
    public static final int INACTIVE = 1;
    
    public static final int PENDING = 2;
    public static final int WIN = 3;
    public static final int LOSE = 4;
    public static final int PUSH = 5;
    public static final int CANCELLED = 6;
    public static final int INVALID = 7;
    public static final int DRAW = 8;
    public static final int FINISHED = 9;
    public static final int SELLING = 10;
    
    public static final int LOGED_IN = 11;
    public static final int LOGED_OUT = 12;

    public static final int IN_QUEUE = 13;
    public static final int PENDING_RESULT = 14;
    
    public static final String[] statusName;
    
    static {
        statusName = new String[15];
        statusName[Status.ACTIVE] = "Activo";
        statusName[Status.CANCELLED] = "Cancelado";
        statusName[Status.PUSH] = "Empata";
        statusName[Status.INACTIVE] = "Inactivo";
        statusName[Status.INVALID] = "Invalido";
        statusName[Status.IN_QUEUE] = "En Cola";
        statusName[Status.LOGED_IN] = "Loged In";
        statusName[Status.LOGED_OUT] = "Loged Out";
        statusName[Status.LOSE] = "Pierde";
        statusName[Status.PENDING] = "Pendiente";
        statusName[Status.WIN] = "Gana";
        statusName[Status.DRAW] = "Empata";
        statusName[Status.FINISHED] = "Finalizado";
        statusName[Status.SELLING] = "Pendiente";
        statusName[Status.PENDING_RESULT] = "Pendiente";
    }
    
    public String str(Integer status) {
        if (status == null) {
            return "";
        }
        return statusName[status];
    }
    
    public int getACTIVE() {
        return ACTIVE;
    }

    public int getINACTIVE() {
        return INACTIVE;
    }

    public int getPENDING() {
        return PENDING;
    }

    public int getWIN() {
        return WIN;
    }

    public int getLOSE() {
        return LOSE;
    }

    public int getPUSH() {
        return PUSH;
    }

    public int getCANCELLED() {
        return CANCELLED;
    }

    public int getINVALID() {
        return INVALID;
    }

    public int getLOGED_IN() {
        return LOGED_IN;
    }

    public int getLOGED_OUT() {
        return LOGED_OUT;
    }

    public int getIN_QUEUE() {
        return IN_QUEUE;
    }

    public int getDRAW() {
        return DRAW;
    }

    public int getFINISHED() {
        return FINISHED;
    }

    public int getSELLING() {
        return SELLING;
    }

    public int getPENDING_RESULT() {
        return PENDING_RESULT;
    }

    public String[] getStatusName() {
        return statusName;
    }    
}
