/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.ParameterValidator;
import co.com.bookmaker.business_logic.service.AgencyService;
import co.com.bookmaker.business_logic.service.FinalUserService;
import co.com.bookmaker.business_logic.service.event.MatchEventPeriodService;
import co.com.bookmaker.business_logic.service.event.MatchEventService;
import co.com.bookmaker.business_logic.service.event.SportService;
import co.com.bookmaker.business_logic.service.event.TeamService;
import co.com.bookmaker.business_logic.service.parlay.ParlayOddService;
import co.com.bookmaker.business_logic.service.parlay.ParlayService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.Sport;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import co.com.bookmaker.util.form.bean.SearchParlayBean;
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
@WebServlet(name = "ManagerController", urlPatterns = {"/"+ManagerController.URL})
public class ManagerController extends GenericController {

    public static final String URL = "manager";
    
    public static final String SIDEBAR = "sidebar";
    
    public static final String AGENCY_SUMMARY = "agency_summary";
    public static final String AGENCY_BALANCE = "agency_balance";
    public static final String EMPLOYEE_SUMMARY = "employee_summary";
    public static final String EMPLOYEE_BALANCE = "employee_balance";
    public static final String SEARCH_PARLAY = "search_parlay";
    public static final String SEARCH_MATCH = "search_match";
    
    public static final String PARLAY_SEARCH_RESULT = "parlay_search_result";
    public static final String MATCH_SEARCH_RESULT = "match_search_result";
    
    public static final String PARLAY_SUMMARY = "parlay_summary";
    public static final String MATCH_SUMMARY = "match_summary";
    
    @EJB
    private AuthenticationService auth;
    @EJB
    private AgencyService agencyService;
    @EJB
    private ParameterValidator validator;
    @EJB
    private ParlayService parlayService;
    @EJB
    private FinalUserService finalUserService;
    @EJB
    private MatchEventService matchEventService;
    @EJB
    private SportService sportService;
    @EJB
    private TeamService teamService;
    @EJB
    private ParlayOddService parlayOddService;
    @EJB
    private MatchEventPeriodService matchPeriodService;
    
    @Override
    public void init() {
        
        allowTO(AGENCY_SUMMARY, Role.MANAGER);
        allowTO(AGENCY_BALANCE, Role.MANAGER);
        allowTO(SEARCH_PARLAY, Role.MANAGER);
        allowTO(EMPLOYEE_BALANCE, Role.MANAGER);
        allowTO(SEARCH_MATCH, Role.MANAGER);
        allowTO(PARLAY_SUMMARY, Role.MANAGER);
        allowTO(MATCH_SUMMARY, Role.MANAGER);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/manager/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        
        switch (resource) {
            case INDEX:
            case AGENCY_SUMMARY:
                toAgencySummary(); break;
            case AGENCY_BALANCE:
                toAgencyBalance(); break;
            case SEARCH_PARLAY:
                toSearchParlay(); break;
            case EMPLOYEE_SUMMARY:
                toEmployeeSummary(); break;
            case EMPLOYEE_BALANCE:
                toEmployeeBalance(); break;
            case SEARCH_MATCH:
                toSearchMatch(); break;
            case PARLAY_SUMMARY:
                toParlaySummary(); break;
            case MATCH_SUMMARY:
                toMatchSummary(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void toAgencySummary() {
        
        Agency agency = auth.sessionUser(request).getAgency();
        request.setAttribute(Attribute.AGENCY, agency);
        request.setAttribute(Attribute.EMPLOYEES, agencyService.getEmployees(agency));
        request.setAttribute(Attribute.ROLE, Role.MANAGER);
        forward(getJSP(AGENCY_SUMMARY));
    }

    private void toAgencyBalance() {
        
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
        
        if (validated) {
            Agency agency = auth.sessionUser(request).getAgency();
            List<Parlay> parlays = parlayService.searchBy(agency.getId(), null, null, from, to, null);
            
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
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        request.setAttribute(Attribute.ROLE, Role.MANAGER);
        forward(getJSP(AGENCY_BALANCE));
    }

    private void toEmployeeSummary() {
        
        String username = request.getParameter(Parameter.USERNAME);
        
        if (username == null) {
            forward(getJSP(AGENCY_SUMMARY));
            return;
        }
        FinalUser result = finalUserService.getUser(username);
        if (result != null) {
            request.setAttribute(Attribute.FINAL_USER, result);
            request.setAttribute(Attribute.ROLE, Role.MANAGER);
            forward(getJSP(EMPLOYEE_SUMMARY));
        } else {
            forward(getJSP(AGENCY_SUMMARY));
        }
    }
    
    private void toEmployeeBalance() {
        
        String username = request.getParameter(Parameter.USERNAME);
        
        if (username == null) {
            forward(getJSP(AGENCY_SUMMARY));
            return;
        }
        FinalUser employee = finalUserService.getUser(username);
        if (employee == null) {
            forward(getJSP(AGENCY_SUMMARY));
            return;
        }
        
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
        
        if (validated) {
            Agency agency = employee.getAgency();
            if (employee.inRole(Role.SELLER)) {
                List<Parlay> parlays = parlayService.searchBy(agency.getId(), null, username, from, to, null);

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
            if (employee.inRole(Role.ANALYST)) {
                List<MatchEvent> matches = matchEventService.searchBy(agency.getId(), null, null, username, from, to, null);
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
        request.setAttribute(Attribute.EMPLOYEE, employee);
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        request.setAttribute(Attribute.ROLE, Role.MANAGER);
        forward(getJSP(EMPLOYEE_BALANCE));
    }
    
    private void toSearchParlay() {
        
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
        
        SearchParlayBean sp = new SearchParlayBean();
        sp.setFrom(strFrom);
        sp.setTo(strTo);
        
        request.setAttribute(Attribute.PARLAY, sp);
        request.setAttribute(Attribute.ROLE, Role.MANAGER);
        forward(getJSP(SEARCH_PARLAY));
    }

    private void toSearchMatch() {
        
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
        
        List<Sport> sports = sportService.getSports(Status.ACTIVE);
        
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        request.setAttribute(Attribute.SPORTS, sports);
        request.setAttribute(Attribute.ROLE, Role.MANAGER);
        forward(getJSP(SEARCH_MATCH));
    }

    private void toParlaySummary() {
       
        String strParlayId = request.getParameter(Parameter.PARLAY);
        
        Long parlayId;
        try {
            parlayId = Long.parseLong(strParlayId);
        } catch(NumberFormatException ex) {
            request.setAttribute(Attribute.ROLE, Role.MANAGER);
            forward(getJSP(SEARCH_PARLAY));
            return;
        }
        Parlay parlay = parlayService.getParlay(parlayId);
        request.setAttribute(Attribute.PARLAY, parlay);
        request.setAttribute(Attribute.PARLAY_ODDS, parlay.getOdds());
        forward(getJSP(PARLAY_SUMMARY));
    }

    private void toMatchSummary() {
       
        String strMatchId = request.getParameter(Parameter.MATCH_EVENT);
        
        Long matchId;
        try {
            matchId = Long.parseLong(strMatchId);
        } catch(Exception ex) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        MatchEvent match = matchEventService.getMatchEvent(matchId);
        matchEventService.update(match);
        
        if (match == null) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        request.setAttribute(Attribute.MATCH_EVENT, match);
        request.setAttribute(Attribute.TEAM_SERVICE, teamService);
        request.setAttribute(Attribute.PARLAYODD_SERVICE, parlayOddService);
        request.setAttribute(Attribute.MATCH_PERIOD_SERVICE, matchPeriodService);
        forward(getJSP(MATCH_SUMMARY));
    }
}
