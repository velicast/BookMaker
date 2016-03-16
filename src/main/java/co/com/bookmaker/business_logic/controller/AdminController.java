/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.ParameterValidator;
import co.com.bookmaker.business_logic.service.AgencyService;
import co.com.bookmaker.business_logic.service.FinalUserService;
import co.com.bookmaker.business_logic.service.event.MatchEventService;
import co.com.bookmaker.business_logic.service.parlay.ParlayService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.EJB;
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
import javax.ejb.EJBException;

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
    public static final String DASHBOARD = "dashboard";
    public static final String NEW_AGENCY = "new_agency";
    public static final String EDIT_AGENCY = "edit_agency";
    public static final String SEARCH_AGENCY = "search_agency";
    public static final String AGENCY_SUMMARY = "agency_summary";
    public static final String LIST_AGENCIES = "list_agencies";
    public static final String SEARCH_USER_RESULT = "search_user_result";
    public static final String SEARCH_AGENCY_EMPLOYEE = "search_agency_employee";
    public static final String AGENCY_BALANCE = "agency_balance";
    public static final String EMPLOYEE_BALANCE = "employee_balance";
    
    public static final String SIDEBAR = "sidebar";
    
    @EJB
    private FinalUserService finalUserService;
    @EJB
    private AgencyService agencyService;
    @EJB
    private ParameterValidator validator;
    @EJB
    private ParlayService parlayService;
    @EJB
    private MatchEventService matchEventService;
    
    @Override
    public void init() {
        
        allowTO(INDEX, Role.ADMIN);
        allowTO(SEARCH_USER, Role.ADMIN);
        allowTO(NEW_USER, Role.ADMIN);
        allowTO(EDIT_USER, Role.ADMIN);
        allowTO(USER_SUMMARY, Role.ADMIN);
        allowTO(DASHBOARD, Role.ADMIN);
        allowTO(NEW_AGENCY, Role.ADMIN);
        allowTO(SEARCH_AGENCY, Role.ADMIN);
        allowTO(EDIT_AGENCY, Role.ADMIN);
        allowTO(SEARCH_AGENCY_EMPLOYEE, Role.ADMIN);
        allowTO(LIST_AGENCIES, Role.ADMIN);
        allowTO(AGENCY_SUMMARY, Role.ADMIN);
        allowTO(AGENCY_BALANCE, Role.ADMIN);
        allowTO(EMPLOYEE_BALANCE, Role.ADMIN);
        
        allowDO(SEARCH_USER, Role.ADMIN);
        allowDO(SEARCH_AGENCY, Role.ADMIN);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/admin/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        
        switch (resource) {
            case INDEX:
            case DASHBOARD:
                toDashboard(); break;
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
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void toDashboard() {
        forward(getJSP(INDEX));
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
            request.setAttribute(Attribute.ROLE, Role.ADMIN);
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
            } catch(NumberFormatException ex) {
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
            request.setAttribute(Attribute.ROLE, Role.ADMIN);
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
        
        List<Agency> list = agencyService.getAllAgencies();
        request.setAttribute(Attribute.LIST_AGENCIES, list);
        forward(getJSP(LIST_AGENCIES));
    }

    private void toAgencyBalance() {
        
        Agency agency = getAgency();
        if (agency == null) {
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
        request.setAttribute(Attribute.ROLE, Role.ADMIN);
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
        request.setAttribute(Attribute.ROLE, Role.ADMIN);
        forward(getJSP(EMPLOYEE_BALANCE));
    }
}
