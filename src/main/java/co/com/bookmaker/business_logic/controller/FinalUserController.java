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
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.form.bean.FinalUserBean;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.Status;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "FinalUserController", urlPatterns = {"/"+FinalUserController.URL})
public class FinalUserController extends GenericController {

    public static final String URL = "user";

    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String SEARCH = "search";
    public static final String SUMMARY = "summary";
    public static final String CHANGE_PASSWORD = "change_password";
    public static final String BALANCE = "balance";
    public static final String SEARCH_RESULT = "search_result";
    public static final String SEARCH_EMPLOYEE = "search_employee";

    private FinalUserService finalUserService;
    private AuthenticationService auth;
    private AgencyService agencyService;
    private ParameterValidator validator;
    private MatchEventService matchEventService;
    private ParlayService parlayService;
    
    @Override
    public void init() {

        finalUserService = new FinalUserService();
        auth = new AuthenticationService();
        agencyService = new AgencyService();
        validator = new ParameterValidator();
        parlayService = new ParlayService();
        matchEventService = new MatchEventService();
        
        allowDO(NEW, Role.ADMIN);
        allowDO(EDIT, Role.ADMIN);
        allowDO(SEARCH, Role.ADMIN);
        allowDO(SEARCH_EMPLOYEE, Role.ADMIN);
        allowDO(CHANGE_PASSWORD, Role.ALL&(~Role.CLIENT));
        allowDO(BALANCE, Role.ALL&(~Role.CLIENT));
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/finaluser/" + resource + ".jsp";
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
            case SEARCH_EMPLOYEE:
                doSearchEmployee(); break;
            case CHANGE_PASSWORD:
                doChangePassword(); break;
            case BALANCE:
                doBalance(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void doNew() {

        String username = request.getParameter(Parameter.USERNAME);
        String password = request.getParameter(Parameter.PASSWORD);

        String roleAdmin = request.getParameter(Parameter.ROLE_ADMIN);
        String roleManager = request.getParameter(Parameter.ROLE_MANAGER);
        String roleAnalyst = request.getParameter(Parameter.ROLE_ANALYST);
        String roleSeller = request.getParameter(Parameter.ROLE_SELLER);
        String roleClient = request.getParameter(Parameter.ROLE_CLIENT);

        String email = request.getParameter(Parameter.EMAIL);
        String firstName = request.getParameter(Parameter.FIRST_NAME);
        String lastName = request.getParameter(Parameter.LAST_NAME);
        String city = request.getParameter(Parameter.CITY);
        String telephone = request.getParameter(Parameter.TELEPHONE);
        String address = request.getParameter(Parameter.ADDRESS);

        String strStatus = request.getParameter(Parameter.STATUS);
        String strBirthDate = request.getParameter(Parameter.BIRTH_DATE);

        // INICIO Validacion
        boolean validated = true;

        try {
            validator.checkUsername(username);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.USERNAME, ex.getMessage());
        }
        
        try {
            validator.checkPassword(password);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.PASSWORD, ex.getMessage());
        }
        
        try {
            validator.checkFirstName(firstName);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.FIRST_NAME, ex.getMessage());
        }
        
        try {
            validator.checkLastName(lastName);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.LAST_NAME, ex.getMessage());
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
            request.setAttribute(Information.STATUS, "Estado inválido");
            validated = false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date bDate = null;
        try {
            bDate = dateFormat.parse(strBirthDate);
        } catch (Exception ex) {
            request.setAttribute(Information.BIRTH_DATE, "Fecha de Nacimiento inválida");
            validated = false;
        }
        Calendar birthDate = Calendar.getInstance();
        if (bDate != null) {
            birthDate.setTime(bDate);
            try {
                validator.checkBirthDate(birthDate);
            } catch (Exception ex) {
                String reason = ex.getMessage();
                request.setAttribute(Information.BIRTH_DATE, reason);
            }
        }
        
            // FIN Validacion

        FinalUserBean user = new FinalUserBean();
        user.setUsername(username);
        user.setrAdmin(roleAdmin != null);
        user.setrManager(roleManager != null);
        user.setrAnalyst(roleAnalyst != null);
        user.setrSeller(roleSeller != null);
        user.setrClient(roleClient != null);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCity(city);
        user.setTelephone(telephone);
        user.setAddress(address);
        user.setStatus(status);
        user.setBirthDate(strBirthDate);

        if (!validated) {
            request.setAttribute(Attribute.FINAL_USER, user);
            forward(AdminController.getJSP(AdminController.NEW_USER));
            return;
        }

        FinalUser author = auth.sessionUser(request);
        
        FinalUser newUser = new FinalUser();
        finalUserService.setAttributes(author, newUser,
                username, password, email, birthDate, firstName, lastName, city,
                telephone, address, status,
                roleAdmin != null, roleManager != null, roleAnalyst != null,
                roleSeller != null, roleClient != null);
        try {
            finalUserService.create(newUser);
        } catch (Exception ex) {
            if (ex instanceof IllegalArgumentException) {
                request.setAttribute(Information.USERNAME, ex.getMessage());
            } else {
                request.setAttribute(Information.ERROR, "Opss! Algo estuvo mal. Por favor intente de nuevo.");
            }
            request.setAttribute(Attribute.FINAL_USER, user);
            forward(AdminController.getJSP(AdminController.NEW_USER));
            return;
        }
        request.setAttribute(Attribute.FINAL_USER, newUser);
        forward(AdminController.getJSP(AdminController.USER_SUMMARY));
    }

    private void doEdit() {

        String targetUsername = request.getParameter(Parameter.TARGET_USERNAME);
        String username = request.getParameter(Parameter.USERNAME);
        String password = request.getParameter(Parameter.PASSWORD);

        String roleAdmin = request.getParameter(Parameter.ROLE_ADMIN);
        String roleManager = request.getParameter(Parameter.ROLE_MANAGER);
        String roleAnalyst = request.getParameter(Parameter.ROLE_ANALYST);
        String roleSeller = request.getParameter(Parameter.ROLE_SELLER);
        String roleClient = request.getParameter(Parameter.ROLE_CLIENT);

        String email = request.getParameter(Parameter.EMAIL);
        String firstName = request.getParameter(Parameter.FIRST_NAME);
        String lastName = request.getParameter(Parameter.LAST_NAME);
        String city = request.getParameter(Parameter.CITY);
        String telephone = request.getParameter(Parameter.TELEPHONE);
        String address = request.getParameter(Parameter.ADDRESS);

        String strStatus = request.getParameter(Parameter.STATUS);
        String strBirthDate = request.getParameter(Parameter.BIRTH_DATE);

        // INICIO Validacion 
        FinalUser oldUser = finalUserService.getUser(targetUsername);
        if (oldUser == null) {
            request.setAttribute(Information.USERNAME, "Usuario " + username + " no encontrado");
            request.setAttribute(Attribute.USERNAME, username);
            forward(AdminController.getJSP(AdminController.SEARCH_USER));
            return;
        }
        
        boolean validated = true;

        try {
            validator.checkUsername(username);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.USERNAME, ex.getMessage());
        }
        
        if (password != null && password.length() > 0) {
            try {
                validator.checkPassword(password);
            } catch (Exception ex) {
                validated = false;
                request.setAttribute(Information.PASSWORD, ex.getMessage());
            }
        }
        
        try {
            validator.checkFirstName(firstName);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.FIRST_NAME, ex.getMessage());
        }
        
        try {
            validator.checkLastName(lastName);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.LAST_NAME, ex.getMessage());
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
            request.setAttribute(Information.STATUS, "Estado inválido");
            validated = false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date bDate = null;
        try {
            bDate = dateFormat.parse(strBirthDate);
        } catch (ParseException ex) {
            request.setAttribute(Information.BIRTH_DATE, "Fecha de Nacimiento inválida");
            validated = false;
        }
        Calendar birthDate = Calendar.getInstance();
        if (bDate != null) {
            birthDate.setTime(bDate);
            try {
                validator.checkBirthDate(birthDate);
            } catch (Exception ex) {
                String reason = ex.getMessage();
                request.setAttribute(Information.BIRTH_DATE, reason);
            }
        }
            // FIN Validacion
        
        FinalUserBean user = new FinalUserBean();
        user.setTargetUsername(targetUsername);
        user.setUsername(username);
        user.setrAdmin(roleAdmin != null);
        user.setrManager(roleManager != null);
        user.setrAnalyst(roleAnalyst != null);
        user.setrSeller(roleSeller != null);
        user.setrClient(roleClient != null);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCity(city);
        user.setTelephone(telephone);
        user.setAddress(address);
        user.setStatus(status);
        user.setBirthDate(strBirthDate);
        
        if (!validated) {
            request.setAttribute(Attribute.FINAL_USER, user);
            forward(AdminController.getJSP(AdminController.EDIT_USER));
            return;
        }

        if (password == null || password.length() == 0) {
            password = oldUser.getPassword();
        }
        
        finalUserService.setAttributes(oldUser.getAuthor(), oldUser,
                username, password, email, birthDate, firstName, lastName, city,
                telephone, address, status,
                roleAdmin != null, roleManager != null, roleAnalyst != null,
                roleSeller != null, roleClient != null);
        try {
            finalUserService.update(oldUser, targetUsername);
        } catch (Exception ex) {
            if (ex instanceof IllegalArgumentException) {
                oldUser.setUsername(targetUsername);
                request.setAttribute(Information.ERROR, ex.getMessage());
            } else {
                request.setAttribute(Information.ERROR, "Opss! Algo estuvo mal. Por favor intente de nuevo.");
            }
            request.setAttribute(Attribute.FINAL_USER, user);
            forward(AdminController.getJSP(AdminController.EDIT_USER));
            return;
        }
        request.setAttribute(Attribute.FINAL_USER, oldUser);
        forward(AdminController.getJSP(AdminController.USER_SUMMARY));
    }

    private void doSearch() {

        String username = request.getParameter(Parameter.USERNAME);
        String strStatus = request.getParameter(Parameter.STATUS);
        String roleAdmin = request.getParameter(Parameter.ROLE_ADMIN);
        String roleManager = request.getParameter(Parameter.ROLE_MANAGER);
        String roleAnalyst = request.getParameter(Parameter.ROLE_ANALYST);
        String roleSeller = request.getParameter(Parameter.ROLE_SELLER);
        String roleClient = request.getParameter(Parameter.ROLE_CLIENT);

        if (username != null && username.trim().length() > 0) {
            try {
                validator.checkUsername(username);
            } catch (Exception ex) {
                request.setAttribute(Information.SEARCH_RESULT, ex.getMessage());
                forward(AdminController.getJSP(AdminController.SEARCH_USER));
                return;
            }
        } else {
            username = null;
        }
        
        Integer status = null;
        if (strStatus != null && strStatus.length() > 0) {
            try {
                status = Integer.parseInt(strStatus);
            } catch (Exception ex) {
                request.setAttribute(Information.SEARCH_RESULT, "Estado inválido "+strStatus);
                forward(AdminController.getJSP(AdminController.SEARCH_USER));
                return;
            }
        }

        Long role = 0L;
        if (roleAdmin != null) {
            role |= Role.ADMIN;
        }
        if (roleManager != null) {
            role |= Role.MANAGER;
        }
        if (roleAnalyst != null) {
            role |= Role.ANALYST;
        }
        if (roleSeller != null) {
            role |= Role.SELLER;
        }
        if (roleClient != null) {
            role |= Role.CLIENT;
        }

        boolean online = request.getParameter(Parameter.ONLINE) != null;
        
        List<FinalUser> result = finalUserService.searchBy(request, username, role, status, online);
        request.setAttribute(Attribute.USERS, result);
        request.setAttribute(Attribute.AUTHENTICATION_SERVICE, auth);

        forward(AdminController.getJSP(AdminController.SEARCH_USER_RESULT));
    }

    private void doSearchEmployee() {
     
        String username = request.getParameter(Parameter.USERNAME);
        String strAgencyId = request.getParameter(Parameter.AGENCY);
        
        Long agencyId;
        try {
            agencyId = Long.parseLong(strAgencyId);
        } catch(Exception ex) {
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY));
            return;
        }
        
        Agency agency = agencyService.getAgency(agencyId);
        if (agency == null) {
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY));
            return;
        }
        
        request.setAttribute(Attribute.AGENCY, agency);
        if (request.getParameter(Parameter.ADD_EMPLOYEE) != null) {
            request.setAttribute(Attribute.ADD_EMPLOYEE, "");
        }
        else if (request.getParameter(Parameter.REM_EMPLOYEE) != null) {
            request.setAttribute(Attribute.REM_EMPLOYEE, "");
        }
        
        try {
            validator.checkUsername(username);
        } catch (Exception ex) {
            request.setAttribute(Information.USERNAME, ex.getMessage());
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
            return;
        }
        
        FinalUser user = finalUserService.getUser(username);
        if(user == null) {
            request.setAttribute(Information.USERNAME, "Usuario "+username+" no encontrado");
            forward(AdminController.getJSP(AdminController.SEARCH_AGENCY_EMPLOYEE));
            return;
        }
        request.setAttribute(Attribute.FINAL_USER, user);
        forward(AdminController.getJSP(AdminController.USER_SUMMARY));
    }

    private void doChangePassword() {
        
        String oldPassword = request.getParameter(Parameter.PASSWORD);
        String newPassword = request.getParameter(Parameter.NEW_PASSWORD);
        String confirmedPassword = request.getParameter(Parameter.CONFIRMED_NEW_PASSWORD);
        
        boolean validated = true;
        
        try {
            validator.checkPassword(oldPassword);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.PASSWORD, ex.getMessage());
        }
        try {
            validator.checkPassword(newPassword);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.NEW_PASSWORD, ex.getMessage());
        }
        try {
            validator.checkPassword(confirmedPassword);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.CONFIRMED_PASSWORD, ex.getMessage());
        }
        
        FinalUser sessionUser = auth.sessionUser(request);
        
        if (oldPassword != null && !oldPassword.equals(sessionUser.getPassword())) {
            validated = false;
            request.setAttribute(Information.PASSWORD, "Contraseña actual incorrecta");
        }
        
        if (newPassword != null && !newPassword.equals(confirmedPassword)) {
            validated = false;
            request.setAttribute(Information.CONFIRMED_PASSWORD, "La contraseña nueva y la confirmación no coinciden");
        }
        
        if (!validated) {
            forward(AccountController.getJSP(AccountController.CHANGE_PASSWORD));
            return;
        }
        
        sessionUser.setPassword(newPassword);
        try {
            finalUserService.edit(sessionUser);
        } catch (Exception ex) {
            sessionUser.setPassword(oldPassword);
            request.setAttribute(Information.ERROR, "Opss! Algo estuvo mal. Por favor intente de nuevo");
            forward(AccountController.getJSP(AccountController.CHANGE_PASSWORD));
            return;
        }
        request.setAttribute(Information.INFO, "La contraseña ha sido cambiada. No la olvide.");
        request.setAttribute(Attribute.FINAL_USER, sessionUser);
        forward(AccountController.getJSP(AccountController.SUMMARY));
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
        
        String username = request.getParameter(Parameter.USERNAME);
        String strFrom = request.getParameter(Parameter.TIME_FROM);
        String strTo = request.getParameter(Parameter.TIME_TO);
        
        if (username == null) {
            redirect(HomeController.URL);
            return;
        }
        FinalUser user = finalUserService.getUser(username);
        if (user == null) {
            redirect(HomeController.URL);
            return;
        }
        
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
                request.setAttribute(Information.STATUS, "Fecha Inválida "+strFrom);
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
                request.setAttribute(Information.STATUS, "Fecha Inválida "+strTo);
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
            Agency agency = user.getAgency();
            if (user.inRole(Role.SELLER) && agency != null) {
                List<Parlay> parlays = parlayService.searchBy(agency.getId(), null, user.getUsername(), from, to, null);

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
            if (user.inRole(Role.ANALYST)) {
                Long agencyId = null;
                if (agency != null) {
                    agencyId = agency.getId();
                }
                List<MatchEvent> matches = matchEventService.searchBy(agencyId, null, null, user.getUsername(), from, to, null);
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
        request.setAttribute(Attribute.FINAL_USER, user);
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        
        if (roleRequester == Role.ALL) {
            forward(AccountController.getJSP(AccountController.BALANCE));
        }
        else if (roleRequester == Role.ADMIN) {
            forward(AdminController.getJSP(AdminController.EMPLOYEE_BALANCE));
        }
        else if (roleRequester == Role.MANAGER) {
            forward(ManagerController.getJSP(ManagerController.EMPLOYEE_BALANCE));
        }
    }
}
