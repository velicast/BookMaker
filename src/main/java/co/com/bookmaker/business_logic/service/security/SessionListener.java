/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.security;

import co.com.bookmaker.data_access.entity.FinalUser;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import co.com.bookmaker.util.type.Attribute;

/**
 *
 * @author eduarc
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        
        HttpSession session = se.getSession();
        FinalUser user = (FinalUser) session.getAttribute(Attribute.SESSION_USER);
        if (user != null) {
            Map<Long, List<HttpSession>> activeSessions = 
                (Map<Long, List<HttpSession>>) session.getServletContext().getAttribute(Attribute.ACTIVE_SESSIONS);
            List<HttpSession> userSessions = activeSessions.get(user.getId());
            if (userSessions != null) {
                userSessions.remove(session);
            }
        }
    }
}
