/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller.security;

import co.com.bookmaker.business_logic.controller.GenericController;
import co.com.bookmaker.business_logic.controller.HomeController;
import co.com.bookmaker.business_logic.controller.ManagerController;
import co.com.bookmaker.business_logic.controller.SellerController;
import co.com.bookmaker.business_logic.controller.AdminController;
import co.com.bookmaker.business_logic.controller.ClientController;
import co.com.bookmaker.business_logic.controller.AnalystController;
import co.com.bookmaker.business_logic.service.ParameterValidator;
import co.com.bookmaker.business_logic.service.FinalUserService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.Status;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "AuthenticationController", urlPatterns = {"/"+AuthenticationController.URL})
public class AuthenticationController extends GenericController {

    public static final String URL = "auth";

    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String ROLE_SELECTION = "role_selection";
    
    private AuthenticationService auth;
    private FinalUserService finalUserService;
    private ParameterValidator validator;
    
    @Override
    public void init() {
        
        finalUserService = new FinalUserService();
        auth = new AuthenticationService();
        validator = new ParameterValidator();
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/auth/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void processDO(String resource) {
        
        switch (resource) {
            case LOGIN:
                doLogin(); break;
            case LOGOUT:
                doLogout(); break;
            case ROLE_SELECTION:
                doRoleSelection(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    protected void toIndex() {
        
        Long sessionRoles = auth.sessionRole(request);
        Agency agency = auth.sessionUser(request).getAgency();
        
        Role rRole = new Role();
        if (rRole.inRole(sessionRoles, Role.ADMIN)) {
            redirect(AdminController.URL);
        }
        else if (agency != null && rRole.inRole(sessionRoles, Role.MANAGER)) {
            redirect(ManagerController.URL);
        }
        else if (rRole.inRole(sessionRoles, Role.ANALYST)) {
            redirect(AnalystController.URL);
        }
        else if (agency != null && rRole.inRole(sessionRoles, Role.SELLER)) {
            redirect(SellerController.URL);
        }
        else if (agency != null && rRole.inRole(sessionRoles, Role.CLIENT)) {
            redirect(ClientController.URL);
        }
        else {
            redirect(HomeController.URL);
        }
    }
    
    public void doLogin() {
        
        FinalUser user = auth.sessionUser(request);
        if (user == null) {
            String username = request.getParameter(Parameter.USERNAME);
            String password = request.getParameter(Parameter.PASSWORD);
            
            try {
                validator.checkUsername(username);
                validator.checkPassword(password);
            } catch (Exception ex) {
                request.setAttribute(Information.ERROR, "Login failed. Invalid username or password");
                forward(HomeController.getJSP(HomeController.INDEX));
                return;
            }
            
            user = finalUserService.getLoginUser(username, password);
            
            if(user != null) {
                if (user.getStatus() == Status.INACTIVE) {
                    request.setAttribute(Information.INFO, "The user account is <b>inactive</b>. Please contact the administration.");
                    forward(HomeController.getJSP(HomeController.INDEX));
                    return;
                }
                request.getSession().setAttribute(Attribute.ROLE_SELECTION, "");
                request.getSession().setAttribute(Attribute.USERNAME, username);
                request.getSession().setAttribute(Attribute.FINAL_USER, user);
                forward(getJSP(ROLE_SELECTION));
                return;
            } else {
                request.setAttribute(Information.ERROR, "Login failed. Invalid username or password");
                forward(HomeController.getJSP(HomeController.INDEX));
                return;
            }
        }
        toIndex();
    }
    
    public void doLogout() {
        
        auth.logout(request);
        redirect(HomeController.URL);
    }

    private void doRoleSelection() {
        
        String roleAdmin = request.getParameter(Parameter.ROLE_ADMIN);
        String roleManager = request.getParameter(Parameter.ROLE_MANAGER);
        String roleAnalyst = request.getParameter(Parameter.ROLE_ANALYST);
        String roleSeller = request.getParameter(Parameter.ROLE_SELLER);
        String roleClient = request.getParameter(Parameter.ROLE_CLIENT);
        
        String username = (String) request.getSession().getAttribute(Attribute.USERNAME);
        FinalUser user = finalUserService.getUser(username);
        
        if (user == null) {
            doLogout();
            forward(getJSP(HomeController.INDEX));
            return;
        }
        Long loginRoles = 0L;
        if (roleAdmin != null && user.inRole(Role.ADMIN)) {
            loginRoles |= Role.ADMIN;
        }
        if (roleManager != null && user.inRole(Role.MANAGER)) {
            loginRoles |= Role.MANAGER;
        }
        if (roleAnalyst != null && user.inRole(Role.ANALYST)) {
            loginRoles |= Role.ANALYST;
        }
        if (roleSeller != null && user.inRole(Role.SELLER)) {
            loginRoles |= Role.SELLER;
        }
        if (roleClient != null && user.inRole(Role.CLIENT)) {
            loginRoles |= Role.CLIENT;
        }
        Long logoutRoles = loginRoles&(~Role.CLIENT);
        auth.login(request, user, loginRoles, logoutRoles);
        request.getSession().removeAttribute(Attribute.ROLE_SELECTION);
        request.getSession().removeAttribute(Attribute.FINAL_USER);
        request.getSession().removeAttribute(Attribute.USERNAME);
        toIndex();
    }
}
