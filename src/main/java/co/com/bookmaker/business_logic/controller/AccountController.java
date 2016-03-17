/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Role;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "AccountController", urlPatterns = {"/"+AccountController.URL})
public class AccountController extends GenericController {

    public static final String URL = "account";
    
    public static final String SUMMARY = "summary";
    public static final String CHANGE_PASSWORD = "change_password";
    public static final String BALANCE = "balance";
    
    public static final String SIDEBAR = "sidebar";
    
    @EJB
    private AuthenticationService auth;
    
    @Override
    public void init() {
        
        Long role = Role.ALL&(~Role.CLIENT);
        allowTO(INDEX, role);
        allowTO(SUMMARY, role);
        allowTO(CHANGE_PASSWORD, role);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/account/"+resource+".jsp";
    }
    
    @Override
    protected void processTO(String resource) {
        
        switch (resource) {
            case INDEX:
            case SUMMARY:
                toAccountSummary(); break;
            case CHANGE_PASSWORD:
                toChangePassword(); break;
            case BALANCE:
                toBalance(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void toAccountSummary() {
        
        request.setAttribute(Attribute.FINAL_USER, auth.sessionUser(request));
        forward(getJSP(SUMMARY));
    }
    
    private void toChangePassword() {
        forward(getJSP(CHANGE_PASSWORD));
    }

    private void toBalance() {
        
        FinalUser user = auth.sessionUser(request);
        
        Calendar from = Calendar.getInstance();
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        
        Calendar to = Calendar.getInstance();
        to.set(Calendar.HOUR_OF_DAY, 23);
        to.set(Calendar.MINUTE, 59);
        to.set(Calendar.SECOND, 59);
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        String strFrom = formatter.format(from.getTime());
        String strTo = formatter.format(to.getTime());
        
        request.setAttribute(Attribute.FINAL_USER, user);
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        forward(getJSP(BALANCE));
    }

}
