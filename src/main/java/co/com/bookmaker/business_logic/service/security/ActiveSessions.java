/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.security;

/**
 *
 * @author eduarc
 */
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import co.com.bookmaker.util.type.Attribute;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

@WebListener
public class ActiveSessions implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        try {
            String reportFile = getClass().getClassLoader().getResource("bookmakerReport.jrxml").getPath();
            JasperReport report = JasperCompileManager.compileReport(reportFile);
            sce.getServletContext().setAttribute(Attribute.COMPILED_TICKED_REPORT, report);
        } catch (JRException ex) {
            Logger.getLogger(ActiveSessions.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Map<Long, List<HttpSession>> activeSessions = new TreeMap();
        sce.getServletContext().setAttribute(Attribute.ACTIVE_SESSIONS, activeSessions);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
        Map<Long, List<HttpSession>> activeSessions = 
                (Map<Long, List<HttpSession>>) sce.getServletContext().getAttribute(Attribute.ACTIVE_SESSIONS);
        for (List<HttpSession> ls : activeSessions.values()) {
            for (HttpSession s : ls) {
                s.invalidate();
            }
        }
    }
}
