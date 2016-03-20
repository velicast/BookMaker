/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import java.io.IOException;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author eduarc
 */
public abstract class GenericController extends HttpServlet {
    
    public static final String INDEX = "index";
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected TreeMap<String, Long> toResource = new TreeMap<>();
    protected TreeMap<String, Long> doResource = new TreeMap<>();
    
    private AuthenticationService auth;
    
    public void allowTO(String resource, Long role) {
        toResource.put(resource, role);
    }
    
    public void allowDO(String resource, Long role) {
        doResource.put(resource, role);
    }

    protected HttpServletRequest getRequest() {
        return request;
    }

    protected HttpServletResponse getResponse() {
        return response;
    }
    
    protected HttpSession getSession() {
        return session;
    }
    
    protected void forward(String url) {
        
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher(url);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException ex) {
            log(ex.getMessage());
        }
    }
    
    protected void redirect(String url) {
        
        try {
            response.sendRedirect(url);
        } catch (IOException ex) {
            log(ex.getMessage());
        }
    }
    
    protected void redirectError(int code) {
        
        try {
            response.sendError(code);
        } catch (IOException ex) {
            log(ex.getMessage());
        }
    }
    
    protected void redirectError(int code, String msg) {
        
        try {
            response.sendError(code, msg);
        } catch (IOException ex) {
            log(ex.getMessage());
        }
    }
    
    protected boolean canTo(String resource) {
        
        if (resource == null) {
            return false;
        }
        Long role = toResource.get(resource);
        if (role == null) {
            return true;
        }
        auth = new AuthenticationService();
        Long sessionRole = auth.sessionRole(request);
        return sessionRole != null && ((role&sessionRole) != 0);
    }
    
    protected boolean canDo(String resource) {
        
        if (resource == null) {
            return false;
        }
        Long role = doResource.get(resource);
        if (role == null) {
            return true;
        }
        auth = new AuthenticationService();
        Long sessionRole = auth.sessionRole(request);
        return sessionRole != null && ((role&sessionRole) != 0);
    }
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        
        String pDo = request.getParameter("do");
        String pTo = request.getParameter("to");
        
        this.request = request;
        this.response = response;
        this.session = request.getSession();
      
        if (pTo == null && pDo == null && canTo(INDEX)) {
            processTO(INDEX);
        }
        else if (canTo(pTo)) {
            processTO(pTo);
        }
        else if (canDo(pDo)) {
            processDO(pDo);
        }
        else {
            redirect("/");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    public abstract void init();
    protected abstract void processTO(String resource);
    protected abstract void processDO(String resource);
}
