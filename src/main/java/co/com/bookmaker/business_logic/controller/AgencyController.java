/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.ParameterValidator;
import co.com.bookmaker.business_logic.service.AgencyService;
import co.com.bookmaker.business_logic.service.FinalUserService;
import co.com.bookmaker.business_logic.service.parlay.ParlayService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.form.bean.AgencyBean;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.Status;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "AgencyController", urlPatterns = {"/"+AgencyController.URL})
public class AgencyController extends GenericController {

    public static final String URL = "agency";
    
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String SEARCH = "search";
    public static final String SUMMARY = "summary";
    public static final String MANAGER_SUMMARY = "manager_summary";
    public static final String ADD_EMPLOYEE = "add_employee";
    public static final String REM_EMPLOYEE = "rem_employee";
    public static final String LIST = "list";
    public static final String BALANCE = "balance";
    
    private FinalUserService finalUserService;
    private AgencyService agencyService;
    private AuthenticationService auth;
    private ParameterValidator validator;
    private ParlayService parlayService;
    
    @Override
    public void init() {
        
        finalUserService = new FinalUserService();
        auth = new AuthenticationService();
        agencyService = new AgencyService();
        validator = new ParameterValidator();
        parlayService = new ParlayService();
        
        allowDO(NEW, Role.ADMIN);
        allowDO(EDIT, Role.ADMIN);
        allowDO(SEARCH, Role.ADMIN);
        allowDO(ADD_EMPLOYEE, Role.ADMIN);
        allowDO(REM_EMPLOYEE, Role.ADMIN);
        allowDO(BALANCE, Role.ADMIN|Role.MANAGER);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/agency/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void processDO(String resource) {
        
        switch (resource) {
            case NEW:
                doNew(); break;
            case EDIT:
                doEdit(); break;
            case SEARCH:
                doSearch(); break;
            case ADD_EMPLOYEE:
                doAddEmployee(); break;
            case REM_EMPLOYEE:
                doRemEmployee(); break;
            case BALANCE:
                doBalance(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void doNew() {
        
        String managerUsername = request.getParameter(Parameter.MANAGER);
        String name = request.getParameter(Parameter.NAME);
        String email = request.getParameter(Parameter.EMAIL);
        String telephone = request.getParameter(Parameter.TELEPHONE);
        String city = request.getParameter(Parameter.CITY);
        String address = request.getParameter(Parameter.ADDRESS);
        String strStatus = request.getParameter(Parameter.STATUS);
        String strMinOdds = request.getParameter(Parameter.MIN_ODDS);
        String strMaxOdds = request.getParameter(Parameter.MAX_ODDS);
        String strMaxProfit = request.getParameter(Parameter.MAX_PROFIT);
        String acceptGlobalOdds = request.getParameter(Parameter.ACCEPT_GLOBAL_ODDS);
        
        // INICIO Validacion
        boolean validated = true;
        
        try {
            validator.checkUsername(managerUsername);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.USERNAME, ex.getMessage());
        }
        
        try {
            validator.checkAgencyName(name);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.NAME, ex.getMessage());
        }
        
        try {
            validator.checkCity(city);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.CITY, ex.getMessage());
        }
        
        try {
            validator.checkAddress(address);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.ADDRESS, ex.getMessage());
        }
        
        try {
            validator.checkTelephone(telephone);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.TELEPHONE, ex.getMessage());
        }
        
        Integer status = null;
        try {
            status = Integer.parseInt(strStatus);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.STATUS, "Invalid status value");
        }
        
        Integer minOdds = null;
        try {
            minOdds = Integer.parseInt(strMinOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MIN_ODDS, "Invalid min odds value");
        }
        
        Integer maxOdds = null;
        try {
            maxOdds = Integer.parseInt(strMaxOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_ODDS, "Invalid max odds value");
        }
        
        try {
            validator.checkMinOdds(minOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MIN_ODDS, ex.getMessage());
        }

        try {
            validator.checkMinOdds(maxOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_ODDS, ex.getMessage());
        }

        try {
            validator.checkOddsRange(minOdds, maxOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MIN_ODDS, ex.getMessage());
        }
        
        Double maxProfit = null;
        try {
            maxProfit = Double.parseDouble(strMaxProfit);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_PROFIT, "Invalid max profit value");
        }
        
        try {
            validator.checkMaxProfit(maxProfit);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_PROFIT, ex.getMessage());
        }
        
        FinalUser manager = finalUserService.getUser(managerUsername);
        if (manager == null) {
            validated = false;
            request.setAttribute(Information.MANAGER, "User "+managerUsername+" not found");
        }
        else if (!manager.inRole(Role.MANAGER)) {
            validated = false;
            request.setAttribute(Information.MANAGER, "The user "+managerUsername+" has not the manager role");
        }
        else if (manager.getAgency() != null) {
            validated = false;
            request.setAttribute(Information.MANAGER, "The manager "+managerUsername+" is already asigned to an agency");
        }
        // FIN Validacion
        
        AgencyBean a = new AgencyBean();
        a.setManagerUsername(managerUsername);
        a.setName(name);
        a.setEmail(email);
        a.setCity(city);
        a.setAddress(address);
        a.setTelephone(telephone);
        a.setStatus(status);
        a.setMinOdds(strMinOdds);
        a.setMaxOdds(strMaxOdds);
        a.setMaxProfit(strMaxProfit);
        a.setAcceptGlobalOdds(acceptGlobalOdds != null);
        
        if (!validated) {
            request.setAttribute(Attribute.AGENCY, a);
            forward(AdminController.getJSP(AdminController.NEW_AGENCY));
            return;
        }
        
        Agency newAgency = new Agency();
        agencyService.setAttributes(newAgency, name, email, telephone,
                city, address, minOdds, maxOdds, maxProfit, acceptGlobalOdds != null, status);
        
        try {
            agencyService.create(newAgency, manager);
        } catch (Exception ex) {
            request.setAttribute(Attribute.AGENCY, a);
            request.setAttribute(Information.ERROR, "Opss! Something went wrong. Please try again");
            forward(AdminController.getJSP(AdminController.NEW_AGENCY));
            return;
        }
        auth.logout(request, manager);
        if (auth.sessionUser(request) == null) {
            request.setAttribute(Information.INFO, "Please login again to see the changes.");
            forward(HomeController.getJSP(HomeController.INDEX));
            return;
        }
        request.setAttribute(Attribute.AGENCY, newAgency);
        request.setAttribute(Attribute.EMPLOYEES, agencyService.getEmployees(newAgency));
        forward(AdminController.getJSP(AdminController.AGENCY_SUMMARY));
    }

    // used in the next three methods
    private Agency getAgency() {
        
        String strAgencyId = request.getParameter(Parameter.AGENCY);
        Long agencyId = null;
        if (strAgencyId != null) {
            try {
                agencyId = Long.parseLong(strAgencyId);
            } catch(Exception ex) {
                forward(AdminController.getJSP(AdminController.SEARCH_AGENCY));
                return null;
            }
        }
        Agency agency = agencyService.getAgency(agencyId);
        if (agency == null) {
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY));
        }
        return agency;
    }
    
    private void doEdit() {
        
        String name = request.getParameter(Parameter.NAME);
        String email = request.getParameter(Parameter.EMAIL);
        String telephone = request.getParameter(Parameter.TELEPHONE);
        String city = request.getParameter(Parameter.CITY);
        String address = request.getParameter(Parameter.ADDRESS);
        String strStatus = request.getParameter(Parameter.STATUS);
        String strMinOdds = request.getParameter(Parameter.MIN_ODDS);
        String strMaxOdds = request.getParameter(Parameter.MAX_ODDS);
        String strMaxProfit = request.getParameter(Parameter.MAX_PROFIT);
        String acceptGlobalOdds = request.getParameter(Parameter.ACCEPT_GLOBAL_ODDS);
        
        Agency agency = getAgency();
        if (agency == null) {
            System.out.println("");
            return;
        }
        
        // INICIO Validacion
        boolean validated = true;
        
        try {
            validator.checkAgencyName(name);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.NAME, ex.getMessage());
        }
        
        try {
            validator.checkCity(city);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.CITY, ex.getMessage());
        }
        
        try {
            validator.checkAddress(address);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.ADDRESS, ex.getMessage());
        }
        
        try {
            validator.checkTelephone(telephone);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.TELEPHONE, ex.getMessage());
        }
        
        Integer status = null;
        try {
            status = Integer.parseInt(strStatus);
        } catch (NumberFormatException ex) {
            validated = false;
            request.setAttribute(Information.STATUS, "Invalid status value: "+strStatus);
        }
        
        Integer minOdds = null;
        try {
            minOdds = Integer.parseInt(strMinOdds);
        } catch (NumberFormatException ex) {
            validated = false;
            request.setAttribute(Information.STATUS, "Invalid min odds value");
        }
        
        Integer maxOdds = null;
        try {
            maxOdds = Integer.parseInt(strMaxOdds);
        } catch (NumberFormatException ex) {
            validated = false;
            request.setAttribute(Information.STATUS, "Invalid max odds value");
        }
        
        Double maxProfit = null;
        try {
            maxProfit = Double.parseDouble(strMaxProfit);
        } catch (NumberFormatException ex) {
            validated = false;
            request.setAttribute(Information.STATUS, "Invalid max profit value");
        }
        
        try {
            validator.checkMinOdds(minOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MIN_ODDS, ex.getMessage());
        }

        try {
            validator.checkMinOdds(maxOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_ODDS, ex.getMessage());
        }

        try {
            validator.checkOddsRange(minOdds, maxOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MIN_ODDS, ex.getMessage());
        }
        
        try {
            validator.checkMaxProfit(maxProfit);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_PROFIT, ex.getMessage());
        }
        
        // FIN Validacion
        
        AgencyBean a = new AgencyBean();
        a.setName(name);
        a.setEmail(email);
        a.setCity(city);
        a.setAddress(address);
        a.setTelephone(telephone);
        a.setStatus(status);
        a.setMinOdds(strMinOdds);
        a.setMaxOdds(strMaxOdds);
        a.setMaxProfit(strMaxProfit);
        a.setAcceptGlobalOdds(acceptGlobalOdds != null);
        
        if (!validated) {
            request.setAttribute(Attribute.AGENCY, a);
            forward(AdminController.getJSP(AdminController.EDIT_AGENCY));
            return;
        }
        
        agencyService.setAttributes(agency, name, email, telephone,
                city, address, minOdds, maxOdds, maxProfit, acceptGlobalOdds != null, status);
        request.setAttribute(Attribute.AGENCY, agency);
        try {
            agencyService.edit(agency);
        } catch (Exception ex) {
            request.setAttribute(Information.ERROR, "Opss! Something went wrong. Please try again.");
            request.setAttribute(Attribute.AGENCY, a);
            forward(AdminController.getJSP(AdminController.EDIT_AGENCY));
            return;
        }
        List<FinalUser> employees = agencyService.getEmployees(agency);
        for (FinalUser e : employees) {
            e.setStatus(status);
            finalUserService.edit(e);
        }
        request.setAttribute(Attribute.AGENCY, agency);
        request.setAttribute(Attribute.EMPLOYEES, employees);
        forward(AdminController.getJSP(AdminController.AGENCY_SUMMARY));
    }

    private void doAddEmployee() {
        
        String username = request.getParameter(Parameter.EMPLOYEE);
        
        Agency agency = getAgency();
        if (agency == null) {
            return;
        }
        
        request.setAttribute(Attribute.ADD_EMPLOYEE, "");
        request.setAttribute(Attribute.AGENCY, agency);
        
        try {
            validator.checkUsername(username);
        } catch (Exception ex) {
            request.setAttribute(Information.USERNAME, "Invalid username "+username);
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
            return;
        }
        
        FinalUser employee = finalUserService.getUser(username);
        if (employee == null) {
            request.setAttribute(Information.USERNAME, "User "+username+" not found");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
        }
        else if (employee.getAgency() != null) {
            request.setAttribute(Information.USERNAME, "User "+username+" is already employeed by an agency");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
        }
        else {
            employee.setAgency(agency);
            try {
                finalUserService.edit(employee);
            } catch (Exception ex) {
                employee.setAgency(null);
                request.setAttribute(Attribute.FINAL_USER, employee);
                request.setAttribute(Information.ERROR, "Opss! Something went wrong. Please try again.");
                forward(AdminController.getJSP(AdminController.USER_SUMMARY));
                return;
            }
            auth.logout(request, employee);
            if (auth.sessionUser(request) == null) {
                request.setAttribute(Information.INFO, "Please login again to see the changes.");
                forward(HomeController.getJSP(HomeController.INDEX));
                return;
            }
            request.setAttribute(Attribute.EMPLOYEES, agencyService.getEmployees(agency));
            request.setAttribute(Information.INFO, "User "+username+" successfully added");
            forward(AdminController.getJSP(AdminController.AGENCY_SUMMARY));
        }
    }

    private void doRemEmployee() {
        
        String username = request.getParameter(Parameter.EMPLOYEE);
        
        Agency agency = getAgency();
        if (agency == null) {
            return;
        }
        request.setAttribute(Attribute.REM_EMPLOYEE, "");
        request.setAttribute(Attribute.AGENCY, agency);
        
        try {
            validator.checkUsername(username);
        } catch (Exception ex) {
            request.setAttribute(Information.USERNAME, "Invalid username "+username);
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
            return;
        }
        
        FinalUser employee = finalUserService.getUser(username, agency);
        
        if (employee == null) {
            request.setAttribute(Information.USERNAME, "User "+username+" is not an employee in the agency");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
            return;
        }
        int countManager = 0;
        List<FinalUser> employees = agencyService.getEmployees(agency);
        for (FinalUser user : employees) {
            if (user.inRole(Role.MANAGER)) {
                countManager++;
            }
        }
        if (employee.inRole(Role.MANAGER) && countManager == 1) {
            request.setAttribute(Information.USERNAME, "User "+username+" is the only manager in the agency");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
            return;
        }

        employee.setAgency(null);
        try {
            finalUserService.edit(employee);
        } catch (Exception ex) {
            employee.setAgency(agency);
            request.setAttribute(Attribute.FINAL_USER, employee);
            request.setAttribute(Information.ERROR, "Opss! Something went wrong. Please try again.");
            forward(AdminController.getJSP(AdminController.USER_SUMMARY));
            return;
        }
        employees.remove(employee);
        auth.logout(request, employee);
        if (auth.sessionUser(request) == null) {
            request.setAttribute(Information.INFO, "Please login again to see the changes.");
            forward(HomeController.getJSP(HomeController.INDEX));
            return;
        }
        request.setAttribute(Attribute.EMPLOYEES, employees);
        request.setAttribute(Information.INFO, "User "+username+" successfully removed");
        forward(AdminController.getJSP(AdminController.AGENCY_SUMMARY));
    }

    private void doSearch() {
        
        String username = request.getParameter(Parameter.USERNAME);
        
        try {
            validator.checkUsername(username);
        } catch (Exception ex) {
            request.setAttribute(Information.USERNAME, ex.getMessage());
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY));
            return;
        }
        
        FinalUser employee = finalUserService.getUser(username);
        if (employee == null) {
            request.setAttribute(Information.USERNAME, "User "+username+" not found");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY));
        }
        else if (employee.getAgency() == null) {
            request.setAttribute(Information.USERNAME, "The user "+username+" is not employed by an agency");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY));
        }
        else {
            Agency agency = employee.getAgency();
            request.setAttribute(Attribute.EMPLOYEES, agencyService.getEmployees(agency));
            request.setAttribute(Attribute.AGENCY, agency);
            forward(AdminController.getJSP(AdminController.AGENCY_SUMMARY));
        }
    }

    private void doBalance() {
        
        String strRoleRequester = request.getParameter(Parameter.ROLE);
        Long roleRequester = null;
        try {
            roleRequester = Long.parseLong(strRoleRequester);
        } catch (Exception ex) {}
        if (roleRequester == null || !(new Role().someRole(auth.sessionRole(request), roleRequester))) {
            redirect(HomeController.URL);
            return;
        }
        
        String strAgencyId = request.getParameter(Parameter.AGENCY);
        Long agencyId = null;
        try {
            agencyId = Long.parseLong(strAgencyId);
        } catch(Exception ex) {}
        Agency agency = agencyService.getAgency(agencyId);
        if (agency == null) {
            redirect(HomeController.URL);
            return;
        }
        
        String strFrom = request.getParameter(Parameter.TIME_FROM);
        String strTo = request.getParameter(Parameter.TIME_TO);
        
        boolean validated = true;
        
        Calendar from = null;
        Calendar to = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
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
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.STATUS, ex.getMessage());
        }
        if (validated) {
            List<Parlay> parlays = parlayService.searchBy(agency.getId(), null, null, from, to, null);
            
            Integer soldParlays = 0;
            Integer inQueue = 0;
            Double revenue = 0D;
            Double cost = 0D;
            for (Parlay p : parlays) {
                Integer st = p.getStatus();
                if (!st.equals(Status.CANCELLED)) {
                    if (st.equals(Status.IN_QUEUE)) {
                        inQueue++;
                    } else {
                        soldParlays++;
                        revenue += p.getRisk();
                        if (p.getStatus().equals(Status.WIN)) {
                            cost += p.getProfit();
                        }
                    }
                }
            }
            Double profit = revenue-cost;
            request.setAttribute(Attribute.IN_QUEUE, inQueue);
            request.setAttribute(Attribute.PARLAYS, soldParlays);
            request.setAttribute(Attribute.REVENUE, revenue);
            request.setAttribute(Attribute.COST, cost);
            request.setAttribute(Attribute.PROFIT, profit);
        }
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        request.setAttribute(Attribute.AGENCY, agency);
        if (roleRequester == Role.ADMIN) {
            forward(AdminController.getJSP(AdminController.AGENCY_BALANCE));
        }
        else if (roleRequester == Role.MANAGER) {
            forward(ManagerController.getJSP(ManagerController.AGENCY_BALANCE));
        }
    }
}
