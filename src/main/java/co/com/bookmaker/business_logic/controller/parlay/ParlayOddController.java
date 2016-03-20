/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller.parlay;

import co.com.bookmaker.business_logic.controller.GenericController;
import co.com.bookmaker.business_logic.controller.HomeController;
import co.com.bookmaker.business_logic.service.parlay.ParlayOddService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.parlay.ParlayOdd;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "ParlayOddController", urlPatterns = {"/"+ParlayOddController.URL})
public class ParlayOddController extends GenericController {

    public static final String URL = "odds";
    public static final String TICKET = "ticket";
    public static final String TABLE_ODDS = "table_odds";
    
    private AuthenticationService auth;
    private ParlayOddService parlayOddService;
    
    @Override
    public void init() {
        
        auth = new AuthenticationService();
        parlayOddService = new ParlayOddService();
        
        allowTO(TICKET, Role.CLIENT);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/parlayodd/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        
        FinalUser user = auth.sessionUser(request);
        Agency agency = user.getAgency();
        if (agency == null) {
            request.setAttribute(Information.ERROR, "Access denied");
            forward(HomeController.getJSP(HomeController.INDEX));
            return;
        }
        switch (resource) {
            case TICKET:
                toTicket(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void toTicket() {
        
        String strId = request.getParameter(Parameter.ODD_ID);
        Long oddId = Long.parseLong(strId);
        
        ParlayOdd odd = parlayOddService.getOdd(oddId);
        if (odd == null) {
            return;
        }
        request.setAttribute(Attribute.PARLAY_ODD, odd);
        forward(getJSP(TICKET));
    }
}
