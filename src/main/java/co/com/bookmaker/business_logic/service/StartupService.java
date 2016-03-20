/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service;

import co.com.bookmaker.business_logic.service.security.ActiveSessions;
import co.com.bookmaker.data_access.dao.event.SportDAO;
import co.com.bookmaker.data_access.dao.FinalUserDAO;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.Sport;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.SportID;
import co.com.bookmaker.util.type.Status;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author eduarc
 */
@WebListener
public class StartupService implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        SportDAO sportDAO = new SportDAO();
        FinalUserDAO finalUserDAO = new FinalUserDAO();
        
        Logger.getLogger(StartupService.class.getName()).log(Level.INFO, "Stating App...");
        
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-5:00"));
        Locale.setDefault(Locale.US);
        
        try {
            String reportFile = getClass().getClassLoader().getResource("bookmakerReport.jrxml").getPath();
            JasperReport report = JasperCompileManager.compileReport(reportFile);
            sce.getServletContext().setAttribute(Attribute.COMPILED_TICKED_REPORT, report);
        } catch (JRException ex) {
            Logger.getLogger(ActiveSessions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Sport test = sportDAO.find(SportID.SOCCER);
        if (test != null) {
            Logger.getLogger(StartupService.class.getName()).log(Level.INFO, "Data Exists");
            return;
        }
        Logger.getLogger(StartupService.class.getName()).log(Level.INFO, "Creating Initial Data...");
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

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
