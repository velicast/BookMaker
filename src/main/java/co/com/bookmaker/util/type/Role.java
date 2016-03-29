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
public class Role {

    public static final long NONE = 0L;
    public static final long CLIENT = 1L;
    public static final long SELLER = 2L;
    public static final long ANALYST = 4L;
    public static final long MANAGER = 8L;
    public static final long ADMIN = 16L;
    public static final long ALL = CLIENT | SELLER | ANALYST | MANAGER | ADMIN;

    public boolean inRole(Long role, Long r) {
        return role != null && r != null && (role & r) == r;
    }
    
    public boolean someRole(Long role, Long r) {
        return role != null && r != null && (role & r) != NONE;
    }

    public Long getCLIENT() {
        return CLIENT;
    }

    public Long getSELLER() {
        return SELLER;
    }

    public Long getANALYST() {
        return ANALYST;
    }

    public Long getMANAGER() {
        return MANAGER;
    }

    public Long getADMIN() {
        return ADMIN;
    }

    public Long getALL() {
        return ALL;
    }
}
