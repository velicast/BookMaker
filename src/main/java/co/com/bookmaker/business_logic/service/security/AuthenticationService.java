/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.security;

import co.com.bookmaker.data_access.entity.FinalUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Role;

/**
 *
 * @author eduarc
 */
public class AuthenticationService {
    
    public FinalUser sessionUser(HttpServletRequest request) {
        return (FinalUser) request.getSession().getAttribute(Attribute.SESSION_USER);
    }
    
    public Long sessionRole(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute(Attribute.SESSION_ROLE);
    }
    
    public FinalUser login(HttpServletRequest request, FinalUser user) {
        return login(request, user, user.getRol(), Role.NONE);
    }
    
    public FinalUser singleLogin(HttpServletRequest request, FinalUser user) {
        return login(request, user, user.getRol(), user.getRol());
    }
    
    public FinalUser login(HttpServletRequest request, FinalUser user, Long loginRoles, Long logoutRoles) {
        
        HttpSession session = request.getSession();
        session.setAttribute(Attribute.SESSION_USER, user);
        session.setAttribute(Attribute.SESSION_ROLE, loginRoles);
        addUserSession(user, session, logoutRoles);
        return user;
    }
    
    public FinalUser logout(HttpServletRequest request) {
        
        HttpSession toLogout = request.getSession(false);
        if (toLogout != null) {
            FinalUser user = sessionUser(request);
            toLogout.invalidate();
            return user;
        }
        return null;
    }
    
    public FinalUser logout(HttpServletRequest request, FinalUser user) {
        
        Map<Long, List<HttpSession>> activeSessions = 
                (Map<Long, List<HttpSession>>) request.getServletContext().getAttribute(Attribute.ACTIVE_SESSIONS);
        if (activeSessions != null) {
            Long userId = user.getId();
            List<HttpSession> userSessions = activeSessions.get(userId);
            if (userSessions != null) {
                List<HttpSession> toInvalidate = new ArrayList();
                synchronized (userSessions) {
                    for (HttpSession s : userSessions) {
                        toInvalidate.add(s);
                    }
                }
                for (HttpSession s : toInvalidate) {
                    s.invalidate();
                }
                userSessions.clear();
                return user;
            }
        }
        return null;
    }
    
    private void addUserSession(FinalUser user, HttpSession session, Long logoutRoles) {
        
        Map<Long, List<HttpSession>> activeSessions = 
                (Map<Long, List<HttpSession>>) session.getServletContext().getAttribute(Attribute.ACTIVE_SESSIONS);
        Long userId = user.getId();
        List<HttpSession> userSessions = activeSessions.get(userId);
        if (userSessions == null) {
            userSessions = new ArrayList();
            activeSessions.put(userId, userSessions);
        }
        List<HttpSession> toInvalidate = new ArrayList();
        
        synchronized (userSessions) {
            for (HttpSession s : userSessions) {
                Long role = null;
                try {
                    role = (Long)s.getAttribute(Attribute.SESSION_ROLE);
                } catch (Exception ex) {
                    toInvalidate.add(s);
                }
                if (role != null && (logoutRoles&role) != 0) {
                    toInvalidate.add(s);
                }
            }
            for (HttpSession s : toInvalidate) {
                s.invalidate();
            }
        }
        userSessions.add(session);
    }
}
