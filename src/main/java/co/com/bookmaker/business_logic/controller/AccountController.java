/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.ParameterValidator;
import co.com.bookmaker.business_logic.service.event.MatchEventService;
import co.com.bookmaker.business_logic.service.parlay.ParlayService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.Status;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
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
    @EJB
    private ParameterValidator validator;
    @EJB
    private MatchEventService matchEventService;
    @EJB
    private ParlayService parlayService;
    
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
        request.setAttribute(Attribute.ROLE, Role.ALL);
        forward(getJSP(SUMMARY));
    }
    
    private void toChangePassword() {
        forward(getJSP(CHANGE_PASSWORD));
    }

    private void toBalance() {
        
        String strFrom = request.getParameter(Parameter.TIME_FROM);
        String strTo = request.getParameter(Parameter.TIME_TO);
        
        boolean validated = true;
        Calendar from = null;
        Calendar to = null;
        
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        if (strFrom == null && strTo == null) {
            from = Calendar.getInstance();
            from.set(Calendar.HOUR_OF_DAY, 0);
            from.set(Calendar.MINUTE, 0);
            from.set(Calendar.SECOND, 0);
            to = Calendar.getInstance();
            to.set(Calendar.HOUR_OF_DAY, 23);
            to.set(Calendar.MINUTE, 59);
            to.set(Calendar.SECOND, 59);
            
            strFrom = formatter.format(from.getTime());
            strTo = formatter.format(to.getTime());
        } else {
            if (strFrom != null && strFrom.trim().length() > 0) {
                from = Calendar.getInstance();
                try {
                    from.setTime(formatter.parse(strFrom));
                    from.set(Calendar.HOUR_OF_DAY, 0);
                    from.set(Calendar.MINUTE, 0);
                    from.set(Calendar.SECOND, 0);
                } catch (ParseException ex) {
                    request.setAttribute(Information.STATUS, "Invalid value "+strFrom);
                    validated = false;
                }
            }
            if (strTo != null && strTo.trim().length() > 0) {
                to = Calendar.getInstance();
                try {
                    to.setTime(formatter.parse(strTo));
                    to.set(Calendar.HOUR_OF_DAY, 23);
                    to.set(Calendar.MINUTE, 59);
                    to.set(Calendar.SECOND, 59);
                } catch (ParseException ex) {
                    request.setAttribute(Information.STATUS, "Invalid value "+strTo);
                    validated = false;
                }
            }

            try {
                validator.checkDateRange(from, to);
            } catch (EJBException ex) {
                validated = false;
                request.setAttribute(Information.STATUS, ex.getCausedByException().getMessage());
            }
        }
        
        FinalUser sessionUser = auth.sessionUser(request);
        
        if (validated) {
            Agency agency = sessionUser.getAgency();
            if (sessionUser.inRole(Role.SELLER) && agency != null) {
                List<Parlay> parlays = parlayService.searchBy(agency.getId(), null, sessionUser.getUsername(), from, to, null);

                Integer soldParlays = 0;
                Double revenue = 0D;
                Double cost = 0D;
                for (Parlay p : parlays) {
                    Integer st = p.getStatus();
                    if (!st.equals(Status.CANCELLED)) {
                        soldParlays++;
                        revenue += p.getRisk();
                        if (p.getStatus().equals(Status.WIN)) {
                            cost += p.getProfit();
                        }
                    }
                }
                Double profit = revenue-cost;
                request.setAttribute(Attribute.PARLAYS, soldParlays);
                request.setAttribute(Attribute.REVENUE, revenue);
                request.setAttribute(Attribute.COST, cost);
                request.setAttribute(Attribute.PROFIT, profit);
            }
            if (sessionUser.inRole(Role.ANALYST)) {
                Long agencyId = null;
                if (agency != null) {
                    agencyId = agency.getId();
                }
                List<MatchEvent> matches = matchEventService.searchBy(agencyId, null, null, sessionUser.getUsername(), from, to, null);
                Integer totalMatches = matches.size();
                Integer activeMatches = 0;
                Integer inactiveMatches = 0;
                Integer cancelledMatches = 0;
                Integer pendingMatches = 0;
                Integer finishedMatches = 0;
                for (MatchEvent m : matches) {
                    switch (m.getStatus()) {
                        case Status.ACTIVE:
                            activeMatches++; break;
                        case Status.INACTIVE:
                            inactiveMatches++; break;
                        case Status.CANCELLED: 
                            cancelledMatches++; break;
                        case Status.PENDING_RESULT:
                            pendingMatches++; break;
                        case Status.FINISHED: 
                            finishedMatches++; break;
                    }
                }
                request.setAttribute(Attribute.MATCHES, totalMatches);
                request.setAttribute(Attribute.ACTIVE_MATCHES, activeMatches);
                request.setAttribute(Attribute.INACTIVE_MATCHES, inactiveMatches);
                request.setAttribute(Attribute.CANCELLED_MATCHES, cancelledMatches);
                request.setAttribute(Attribute.PENDING_MATCHES, pendingMatches);
                request.setAttribute(Attribute.FINISHED_MATCHES, finishedMatches);
            }
        }
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        request.setAttribute(Attribute.ROLE, Role.ALL);
        forward(getJSP(BALANCE));
    }

}
