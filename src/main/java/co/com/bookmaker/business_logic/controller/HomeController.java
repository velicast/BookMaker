/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.controller.security.AuthenticationController;
import co.com.bookmaker.business_logic.service.parlay.ParlayService;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Status;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "HomeController", urlPatterns = {"/"+HomeController.URL})
public class HomeController extends GenericController {

    public static final String URL = "home";
    
    public static final String TRACK_PARLAY = "track_parlay";
    
    private ParlayService parlayService;
    
    @Override
    public void init() {
        
        parlayService = new ParlayService();
    }
    
    public static String getJSP(String resource) {
        return "/WEB-INF/home/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        
        switch (resource) {
            case INDEX:
                toIndex(); break;
            case TRACK_PARLAY:
                toTrackParlay(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    protected void toIndex() {
        
        Object roleSelection = request.getSession().getAttribute(Attribute.ROLE_SELECTION);
        if (roleSelection != null) {
            forward(AuthenticationController.getJSP(AuthenticationController.ROLE_SELECTION));
        } else {
            forward(getJSP(INDEX));
        }
    }
    
    private void toTrackParlay() {
        
        String strParlayId = request.getParameter(Parameter.PARLAY);
        
            // INICIO VALIDACION
        if (strParlayId == null || strParlayId.length() == 0) {
            forward(getJSP(INDEX));
            return;
        }
        
        Long parlayId;
        try {
            parlayId = Long.parseLong(strParlayId);
        } catch (Exception ex) {
            request.setAttribute(Information.PARLAY, "Invalid id "+strParlayId);
            forward(getJSP(INDEX));
            return;
        }
        parlayId -= ParlayService.OFFSET_PARLAY_ID;
        
        Parlay parlay = parlayService.getParlay(parlayId);
        parlayService.update(parlay);
        if (parlay == null || parlay.getStatus().equals(Status.IN_QUEUE)) {
            request.setAttribute(Information.PARLAY, "Parlay '"+strParlayId+"' not found.");
            forward(getJSP(INDEX));
            return;
        }
        request.setAttribute(Attribute.PARLAY, parlay);
        request.setAttribute(Attribute.PARLAY_ODDS, parlay.getOdds());
        request.setAttribute(Attribute.TRACK_PARLAY, Boolean.TRUE);
        forward(getJSP(INDEX));
    }
    
}
