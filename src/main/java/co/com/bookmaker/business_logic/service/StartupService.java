/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service;

import co.com.bookmaker.data_access.dao.event.SportDAO;
import co.com.bookmaker.data_access.dao.FinalUserDAO;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.Sport;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Startup;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.SportID;
import co.com.bookmaker.util.type.Status;

/**
 *
 * @author eduarc
 */
@Startup
@DependsOn({"SportDAO", "FinalUserDAO"})
public class StartupService {
    
    @EJB
    SportDAO sportDAO;
    @EJB
    FinalUserDAO finalUserDAO;
    
    @PostConstruct
    public void init() {
        
        Sport test = sportDAO.find(SportID.SOCCER);
        if (test != null) {
            return;
        }
        for (int i = 1; i < SportID.N_SPORTS; i++) {
            Sport sport = new Sport(i, SportID.sportName[i]);
            sport.setStatus(Status.ACTIVE);
            sportDAO.create(sport);
        }
        FinalUser eduarc = new FinalUser();
        eduarc.setUsername("eduarc");
        eduarc.setPassword("1234");
        eduarc.setRol(Role.ALL);
        eduarc.setStatus(Status.ACTIVE);
        eduarc.setFirstName("Eduar Moises");    
        eduarc.setLastName("Castrillo Velilla");
        finalUserDAO.create(eduarc);
    }
}
