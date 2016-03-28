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
    public static final String MEDIT = "medit";
    
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
        allowDO(MEDIT, Role.MANAGER);
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
            case MEDIT:
                doMEdit(); break;
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
            request.setAttribute(Information.STATUS, "Estado inválido");
        }
        
        Integer minOdds = null;
        try {
            minOdds = Integer.parseInt(strMinOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MIN_ODDS, "Valor inválido");
        }
        
        Integer maxOdds = null;
        try {
            maxOdds = Integer.parseInt(strMaxOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_ODDS, "Valor inválido");
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
            request.setAttribute(Information.MAX_PROFIT, "Valor inválido");
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
            request.setAttribute(Information.MANAGER, "Usuario "+managerUsername+" no econtrado");
        }
        else if (!manager.inRole(Role.MANAGER)) {
            validated = false;
            request.setAttribute(Information.MANAGER, "El usuario "+managerUsername+" no está en el role de Gerente");
        }
        else if (manager.getAgency() != null) {
            validated = false;
            request.setAttribute(Information.MANAGER, "El gerente "+managerUsername+" ya se encuentra asignado a una agencia");
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
        
        FinalUser author = auth.sessionUser(request);
            
        Agency newAgency = new Agency();
        agencyService.setAttributes(author, newAgency, name, email, telephone,
                city, address, minOdds, maxOdds, maxProfit, acceptGlobalOdds != null, status);
        
        try {
            agencyService.create(newAgency, manager);
        } catch (Exception ex) {
            request.setAttribute(Attribute.AGENCY, a);
            request.setAttribute(Information.ERROR, "Opss! Algo estuvo mal. Por favor intente de nuevo.");
            forward(AdminController.getJSP(AdminController.NEW_AGENCY));
            return;
        }
        request.setAttribute(Attribute.AGENCY, newAgency);
        request.setAttribute(Attribute.EMPLOYEES, agencyService.getEmployees(newAgency));
        request.setAttribute(Attribute.AUTHENTICATION_SERVICE, auth);
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
            return;
        }
        
        // INICIO Validacion
        boolean validated = true;
        
        // Solo el author puede modificarlo
        /*if (!agency.getAuthor().equals(auth.sessionUser(request))) {
            request.setAttribute(Information.ERROR, "Restricted operation");
            forward(HomeController.getJSP(HomeController.URL));
            return;
        }*/
        
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
            request.setAttribute(Information.STATUS, "Estado inválido");
        }
        
        Integer minOdds = null;
        try {
            minOdds = Integer.parseInt(strMinOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MIN_ODDS, "Valor inválido");
        }
        
        Integer maxOdds = null;
        try {
            maxOdds = Integer.parseInt(strMaxOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_ODDS, "Valor inválido");
        }
        
        Double maxProfit = null;
        try {
            maxProfit = Double.parseDouble(strMaxProfit);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_PROFIT, "Valor inválido");
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
        
        agencyService.setAttributes(agency.getAuthor(), agency, name, email, telephone,
                city, address, minOdds, maxOdds, maxProfit, acceptGlobalOdds != null, status);
        request.setAttribute(Attribute.AGENCY, agency);
        try {
            agencyService.edit(agency);
        } catch (Exception ex) {
            request.setAttribute(Information.ERROR, "Opss! Algo estuvo mal. Por favor intente de nuevo.");
            request.setAttribute(Attribute.AGENCY, a);
            forward(AdminController.getJSP(AdminController.EDIT_AGENCY));
            return;
        }
        List<FinalUser> employees = agencyService.getEmployees(agency);
        for (FinalUser e : employees) {
            e.setStatus(status);
            finalUserService.edit(e);
            if (status.equals(Status.INACTIVE)) {
                auth.logout(request, e);
            }
        }
        request.setAttribute(Attribute.AGENCY, agency);
        request.setAttribute(Attribute.EMPLOYEES, employees);
        request.setAttribute(Attribute.AUTHENTICATION_SERVICE, auth);
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
            request.setAttribute(Information.USERNAME, "Nombre de usuario inválido: "+username);
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
            return;
        }
        
        FinalUser employee = finalUserService.getUser(username);
        if (employee == null) {
            request.setAttribute(Information.USERNAME, "Usuario "+username+" no encontrado");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
        }
        else if (employee.getAgency() != null) {
            request.setAttribute(Information.USERNAME, "Usuario "+username+" ya se encuentra empleado en una agencia");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
        }
        else {
            employee.setAgency(agency);
            try {
                finalUserService.edit(employee);
            } catch (Exception ex) {
                employee.setAgency(null);
                request.setAttribute(Attribute.FINAL_USER, employee);
                request.setAttribute(Information.ERROR, "Opss! Algo estuvo mal. Por favor intente de nuevo.");
                forward(AdminController.getJSP(AdminController.USER_SUMMARY));
                return;
            }
            request.setAttribute(Attribute.EMPLOYEES, agencyService.getEmployees(agency));
            request.setAttribute(Attribute.AUTHENTICATION_SERVICE, auth);
            request.setAttribute(Information.INFO, "Usuario "+username+" agregado satisfactoriamente");
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
            request.setAttribute(Information.USERNAME, "Usuario inválido: "+username);
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
            return;
        }
        
        FinalUser employee = finalUserService.getUser(username, agency);
        
        if (employee == null) {
            request.setAttribute(Information.USERNAME, "Usuario "+username+" no es un empleado de la agencia");
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
            request.setAttribute(Information.USERNAME, "Usuario "+username+" es el único gerente en la agencia");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
            return;
        }

        employee.setAgency(null);
        try {
            finalUserService.edit(employee);
        } catch (Exception ex) {
            employee.setAgency(agency);
            request.setAttribute(Attribute.FINAL_USER, employee);
            request.setAttribute(Information.ERROR, "Opss! Algo estuvo mal. Por favor intente de nuevo.");
            forward(AdminController.getJSP(AdminController.USER_SUMMARY));
            return;
        }
        employees.remove(employee);
        request.setAttribute(Attribute.EMPLOYEES, employees);
        request.setAttribute(Attribute.AUTHENTICATION_SERVICE, auth);
        request.setAttribute(Information.INFO, "Usuario "+username+" removido satisfactoriamente");
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
            request.setAttribute(Information.USERNAME, "Usuario "+username+" no encontrado");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY));
        }
        else if (employee.getAgency() == null) {
            request.setAttribute(Information.USERNAME, "Usuario "+username+" no está empleado por una agencia");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY));
        }
        else {
            Agency agency = employee.getAgency();
            request.setAttribute(Attribute.EMPLOYEES, agencyService.getEmployees(agency));
            request.setAttribute(Attribute.AGENCY, agency);
            request.setAttribute(Attribute.AUTHENTICATION_SERVICE, auth);
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
                request.setAttribute(Information.STATUS, "Fecha inválida: "+strFrom);
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
                request.setAttribute(Information.STATUS, "Fecha Inválida: "+strTo);
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
        request.setAttribute(Attribute.AGENCY, agency);
        if (roleRequester == Role.ADMIN) {
            forward(AdminController.getJSP(AdminController.AGENCY_BALANCE));
        }
        else if (roleRequester == Role.MANAGER) {
            forward(ManagerController.getJSP(ManagerController.AGENCY_BALANCE));
        }
    }

    private void doMEdit() {
        
        Agency agency = getAgency();
        if (agency == null) {
            return;
        }
        String strMinOdds = request.getParameter(Parameter.MIN_ODDS);
        String strMaxOdds = request.getParameter(Parameter.MAX_ODDS);
        String strMaxProfit = request.getParameter(Parameter.MAX_PROFIT);
        
        boolean validated = true;
        
        Integer minOdds = null;
        try {
            minOdds = Integer.parseInt(strMinOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MIN_ODDS, "Valor inválido");
        }
        
        Integer maxOdds = null;
        try {
            maxOdds = Integer.parseInt(strMaxOdds);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_ODDS, "Valor inválido");
        }
        
        Double maxProfit = null;
        try {
            maxProfit = Double.parseDouble(strMaxProfit);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.MAX_PROFIT, "Valor inválido");
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
        
        AgencyBean a = new AgencyBean();
        a.setId(agency.getId());
        a.setMinOdds(strMinOdds);
        a.setMaxOdds(strMaxOdds);
        a.setMaxProfit(strMaxProfit);
        
        if (!validated) {
            request.setAttribute(Attribute.AGENCY, a);
            forward(ManagerController.getJSP(ManagerController.EDIT_AGENCY));
            return;
        }
        
        agency.setMinOddsParlay(minOdds);
        agency.setMaxOddsParlay(maxOdds);
        agency.setMaxProfit(maxProfit);
        try {
            agencyService.edit(agency);
        } catch (Exception ex) {
            request.setAttribute(Information.ERROR, "Opss! Algo estuvo mal. Por favor intente de nuevo.");
            request.setAttribute(Attribute.AGENCY, a);
            forward(ManagerController.getJSP(ManagerController.EDIT_AGENCY));
            return;
        }

        List<FinalUser> employees = agencyService.getEmployees(agency);
        request.setAttribute(Attribute.AGENCY, agency);
        request.setAttribute(Attribute.EMPLOYEES, employees);
        request.setAttribute(Attribute.AUTHENTICATION_SERVICE, auth);
        forward(ManagerController.getJSP(ManagerController.AGENCY_SUMMARY));
    }
}
