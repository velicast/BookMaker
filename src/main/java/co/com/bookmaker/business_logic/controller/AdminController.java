/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.AgencyService;
import co.com.bookmaker.business_logic.service.FinalUserService;
import co.com.bookmaker.business_logic.service.ParameterValidator;
import co.com.bookmaker.business_logic.service.event.MatchEventService;
import co.com.bookmaker.business_logic.service.parlay.ParlayService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.form.bean.AgencyBean;
import co.com.bookmaker.util.form.bean.FinalUserBean;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.Status;
import java.text.ParseException;
import java.util.Calendar;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "AdminController", urlPatterns = {"/"+AdminController.URL})
public class AdminController extends GenericController {

    public static final String URL = "admin";
    
    public static final String SEARCH_USER = "search_user";
    public static final String USER_SUMMARY = "user_summary";
    public static final String NEW_USER = "new_user";
    public static final String EDIT_USER = "edit_user";
    public static final String NEW_AGENCY = "new_agency";
    public static final String EDIT_AGENCY = "edit_agency";
    public static final String SEARCH_AGENCY = "search_agency";
    public static final String AGENCY_SUMMARY = "agency_summary";
    public static final String LIST_AGENCIES = "list_agencies";
    public static final String SEARCH_USER_RESULT = "search_user_result";
    public static final String SEARCH_AGENCY_EMPLOYEE = "search_agency_employee";
    public static final String AGENCY_BALANCE = "agency_balance";
    public static final String EMPLOYEE_BALANCE = "employee_balance";
    public static final String MONEY_BALANCE = "money_balance";
    public static final String MATCHES_BALANCE = "matches_balance";
    public static final String ACCOUNTS_BALANCE = "accounts_balance";
    
    
    public static final String SIDEBAR = "sidebar";
    
        // jsp's in ./balance folder
    public static final String ACCOUNTS = "accounts";
    public static final String MONEY = "money";
    public static final String MATCHES = "matches";
    
    private AuthenticationService auth;
    private FinalUserService finalUserService;
    private AgencyService agencyService;
    private ParameterValidator validator;
    private ParlayService parlayService;
    private MatchEventService matchEventService;
    
    @Override
    public void init() {
        
        auth = new AuthenticationService();
        finalUserService = new FinalUserService();
        agencyService = new AgencyService();
        validator = new ParameterValidator();
        parlayService = new ParlayService();
        matchEventService = new MatchEventService();
        
        allowTO(INDEX, Role.ADMIN);
        allowTO(SEARCH_USER, Role.ADMIN);
        allowTO(NEW_USER, Role.ADMIN);
        allowTO(EDIT_USER, Role.ADMIN);
        allowTO(USER_SUMMARY, Role.ADMIN);
        allowTO(NEW_AGENCY, Role.ADMIN);
        allowTO(SEARCH_AGENCY, Role.ADMIN);
        allowTO(EDIT_AGENCY, Role.ADMIN);
        allowTO(SEARCH_AGENCY_EMPLOYEE, Role.ADMIN);
        allowTO(LIST_AGENCIES, Role.ADMIN);
        allowTO(AGENCY_SUMMARY, Role.ADMIN);
        allowTO(AGENCY_BALANCE, Role.ADMIN);
        allowTO(EMPLOYEE_BALANCE, Role.ADMIN);
        allowTO(MONEY_BALANCE, Role.ADMIN);
        allowTO(MATCHES_BALANCE, Role.ADMIN);
        allowTO(ACCOUNTS_BALANCE, Role.ADMIN);
        
        allowDO(SEARCH_USER, Role.ADMIN);
        allowDO(SEARCH_AGENCY, Role.ADMIN);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/admin/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        
        switch (resource) {
            case NEW_USER:
                toNewUser(); break;
            case SEARCH_USER:
                toSearchUser(); break;
            case USER_SUMMARY:
                toUserSummary(); break;
            case EDIT_USER:
                toEditUser(); break;
            case NEW_AGENCY:
                toNewAgency(); break;
            case SEARCH_AGENCY:
                toSearchAgency(); break;
            case AGENCY_SUMMARY:
                toAgencySummary(); break;
            case EDIT_AGENCY:
                toEditAgency(); break;
            case SEARCH_AGENCY_EMPLOYEE:
                toSearchAgencyEmployee(); break;
            case LIST_AGENCIES:
                toListAgencies(); break;
            case AGENCY_BALANCE: 
                toAgencyBalance(); break;
            case EMPLOYEE_BALANCE:
                toEmployeeBalance(); break;
            case INDEX:
            case MONEY_BALANCE:
                toMoneyBalance(); break;
            case MATCHES_BALANCE:
                toMatchesBalance(); break;
            case ACCOUNTS_BALANCE:
                toAccountsBalance(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    private void toSearchUser() {
        forward(getJSP(SEARCH_USER));
    }

    private void toNewUser() {
        forward(getJSP(NEW_USER));
    }

    private void toEditUser() {
        
        String username = request.getParameter(Parameter.USERNAME);
        
        if (username == null) {
            forward(getJSP(SEARCH_USER));
            return;
        }
        FinalUser user = finalUserService.getUser(username);
        if (user != null) {
            FinalUserBean u = new FinalUserBean();
            u.setTargetUsername(user.getUsername());
            u.setUsername(user.getUsername());
            u.setrAdmin(user.inRole(Role.ADMIN));
            u.setrManager(user.inRole(Role.MANAGER));
            u.setrAnalyst(user.inRole(Role.ANALYST));
            u.setrSeller(user.inRole(Role.SELLER));
            u.setrClient(user.inRole(Role.CLIENT));
            u.setEmail(user.getEmail());
            u.setFirstName(user.getFirstName());
            u.setLastName(user.getLastName());
            u.setCity(user.getCity());
            u.setTelephone(user.getTelephone());
            u.setAddress(user.getAddress());
            u.setStatus(user.getStatus());
            if (user.getBirthDate() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                u.setBirthDate(dateFormat.format(user.getBirthDate().getTime()));
            }
            request.setAttribute(Attribute.FINAL_USER, u);
            forward(getJSP(EDIT_USER));
        } else {
            request.setAttribute(Attribute.USERNAME, username);
            request.setAttribute(Information.ERROR, "User "+username+" not found");
            forward(getJSP(SEARCH_USER));
        }
    }

    private void toUserSummary() {
        
        String username = request.getParameter(Parameter.USERNAME);
        
        if (username == null) {
            forward(getJSP(SEARCH_USER));
            return;
        }
        FinalUser result = finalUserService.getUser(username);
        if (result != null) {
            request.setAttribute(Attribute.FINAL_USER, result);
            request.setAttribute(Attribute.ONLINE, auth.isOnline(result, request));
            forward(getJSP(USER_SUMMARY));
        } else {
            forward(getJSP(SEARCH_USER));
        }
    }
    
    private void toNewAgency() {
        
        forward(getJSP(NEW_AGENCY));
    }

    private void toSearchAgency() {
        
        forward(getJSP(SEARCH_AGENCY));
    }
        // used in the next three methods
    private Agency getAgency() {
        
        String strAgencyId = request.getParameter(Parameter.AGENCY);
        Long agencyId = null;
        if (strAgencyId != null) {
            try {
                agencyId = Long.parseLong(strAgencyId);
            } catch(Exception ex) {
                forward(getJSP(SEARCH_AGENCY));
                return null;
            }
        }
        Agency agency = agencyService.getAgency(agencyId);
        if (agency == null) {
            forward(getJSP(SEARCH_AGENCY));
            return null;
        }
        return agency;
    }
    
    private void toAgencySummary() {
        
        Agency agency = getAgency();
        if (agency != null) {
            request.setAttribute(Attribute.AGENCY, agency);
            request.setAttribute(Attribute.EMPLOYEES, agencyService.getEmployees(agency));
            request.setAttribute(Attribute.AUTHENTICATION_SERVICE, auth);
            forward(getJSP(AGENCY_SUMMARY));
        }
    }
    
    private void toEditAgency() {
        
        Agency agency = getAgency();
        if (agency != null) {
            AgencyBean a = new AgencyBean();
            a.setId(agency.getId());
            a.setName(agency.getName());
            a.setEmail(agency.getEmail());
            a.setCity(agency.getCity());
            a.setAddress(agency.getAddress());
            a.setTelephone(agency.getTelephone());
            a.setStatus(agency.getStatus());
            a.setMinOdds(agency.getMinOddsParlay()+"");
            a.setMaxOdds(agency.getMaxOddsParlay()+"");
            a.setMaxProfit(agency.getMaxProfit()+"");
            a.setAcceptGlobalOdds(agency.getAcceptGlobalOdds());
            request.setAttribute(Attribute.AGENCY, a);
            forward(getJSP(EDIT_AGENCY));
        } else {
            forward(getJSP(SEARCH_AGENCY));
        }
    }
    
    private void toSearchAgencyEmployee() {
        
        Agency agency = getAgency();
        if (agency != null) {
            request.setAttribute(Attribute.AGENCY, agency);
            String add = request.getParameter(Parameter.ADD_EMPLOYEE);
            String rem = request.getParameter(Parameter.REM_EMPLOYEE);
            if (add != null) {
                request.setAttribute(Attribute.ADD_EMPLOYEE, "");
            }
            if (rem != null) {
                request.setAttribute(Attribute.REM_EMPLOYEE, "");
            }
            forward(getJSP(SEARCH_AGENCY_EMPLOYEE));
        }
    }
    
    private void toListAgencies() {
        
        List<Agency> list = agencyService.findAll();
        request.setAttribute(Attribute.LIST_AGENCIES, list);
        forward(getJSP(LIST_AGENCIES));
    }

    private void toAgencyBalance() {
        
        Agency agency = getAgency();
        if (agency == null) {
            return;
        }
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
        
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        request.setAttribute(Attribute.AGENCY, agency);
        forward(getJSP(AGENCY_BALANCE));
    }

    private void toEmployeeBalance() {
        
        String username = request.getParameter(Parameter.USERNAME);
        
        if (username == null) {
            forward(getJSP(SEARCH_USER));
            return;
        }
        FinalUser user = finalUserService.getUser(username);
        if (user == null) {
            forward(getJSP(SEARCH_USER));
            return;
        }
        
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
        forward(getJSP(EMPLOYEE_BALANCE));
    }

    private void toMoneyBalance() {
        
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
        }

        try {
            validator.checkDateRange(from, to);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.STATUS, ex.getMessage());
        }
        if (validated) {
            List<Parlay> parlays = parlayService.searchBy(null, null, null, from, to, null);
            
            Integer soldParlays = 0;
            Integer inQueue = 0;
            Integer cancelled = 0;
            Integer win = 0;
            Integer lose = 0;
            Double revenue = 0D;
            Double cost = 0D;
            for (Parlay p : parlays) {
                switch (p.getStatus()) {
                    case Status.CANCELLED:
                        cancelled++;
                        break;
                    case Status.IN_QUEUE:
                        inQueue++;
                        break;
                    case Status.WIN:
                        soldParlays++;
                        win++;
                        revenue += p.getRisk();
                        cost += p.getProfit();
                        break;
                    case Status.LOSE:
                        revenue += p.getRisk();
                        soldParlays++;
                        lose++;
                        break;
                }
            }
            Double profit = revenue-cost;
            request.setAttribute(Attribute.WIN, win);
            request.setAttribute(Attribute.LOSE, lose);
            request.setAttribute(Attribute.CANCELLED, cancelled);
            request.setAttribute(Attribute.IN_QUEUE, inQueue);
            request.setAttribute(Attribute.PARLAYS, soldParlays);
            request.setAttribute(Attribute.REVENUE, revenue);
            request.setAttribute(Attribute.COST, cost);
            request.setAttribute(Attribute.PROFIT, profit);
        }
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        forward(getJSP(MONEY_BALANCE));
    }

    private void toAccountsBalance() {
        
        List<Agency> agencies = agencyService.findAll();
        Integer totalAgencies = agencies.size();
        Integer activeAgencies = 0;
        Integer inactiveAgencies = 0;
        for (Agency agency : agencies) {
            if (agency.getStatus().equals(Status.ACTIVE)) {
                activeAgencies++;
            }
            else if (agency.getStatus().equals(Status.INACTIVE)) {
                inactiveAgencies++;
            }
        }
        
        List<FinalUser> users = finalUserService.findAll();
        Integer totalUsers = users.size();
        Integer activeUsers = 0;
        Integer inactiveUsers = 0;
        for (FinalUser user : users) {
            if (user.getStatus().equals(Status.ACTIVE)) {
                activeUsers++;
            }
            else if (user.getStatus().equals(Status.INACTIVE)) {
                inactiveUsers++;
            }
        }
        
        request.setAttribute(Attribute.ONLINE, auth.countOnlineUsers(request));
        request.setAttribute(Attribute.AGENCIES, totalAgencies);
        request.setAttribute(Attribute.ACTIVE_AGENCIES, activeAgencies);
        request.setAttribute(Attribute.INACTIVE_AGENCIES, inactiveAgencies);
        request.setAttribute(Attribute.USERS, totalUsers);
        request.setAttribute(Attribute.ACTIVE_USERS, activeUsers);
        request.setAttribute(Attribute.INACTIVE_USERS, inactiveUsers);
        forward(getJSP(ACCOUNTS_BALANCE));
    }

    private void toMatchesBalance() {
        
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
        }

        try {
            validator.checkDateRange(from, to);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.STATUS, ex.getMessage());
        }
        
        if (validated) {
            List<MatchEvent> matches = matchEventService.searchAllBy(null, null, null, from, to, null);
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
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        forward(getJSP(MATCHES_BALANCE));
    }
}
