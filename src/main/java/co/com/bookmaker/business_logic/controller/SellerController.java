/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.parlay.ParlayService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import co.com.bookmaker.util.form.bean.SearchParlayBean;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "SellerController", urlPatterns = {"/"+SellerController.URL})
public class SellerController extends GenericController {

    public static final String URL = "seller";
    
    public static final String SELLING_QUEUE = "selling_queue";
    public static final String SEARCH_PARLAY = "search_parlay";
    public static final String PARLAY_SUMMARY = "parlay_summary";
    public static final String SIDEBAR = "sidebar";
    public static final String DASHBOARD = "dashboard";
    public static final String PARLAY_SEARCH_RESULT = "parlay_search_result";
    
    private ParlayService parlayService;
    private AuthenticationService auth;
    
    @Override
    public void init() {
        
        auth = new AuthenticationService();
        parlayService = new ParlayService();

        allowTO(INDEX, Role.SELLER);
        allowTO(SELLING_QUEUE, Role.SELLER);
        allowTO(SEARCH_PARLAY, Role.SELLER);
        allowTO(PARLAY_SUMMARY, Role.SELLER);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/seller/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        
        FinalUser user = auth.sessionUser(request);
        Agency agency = user.getAgency();
        if (agency == null) {
            request.setAttribute(Information.INFO, "El usuario "+user.getUsername()+" no está empleado en una agencia");
            forward(HomeController.getJSP(HomeController.INDEX));
            return;
        }
        switch (resource) {
            case INDEX:
            case SELLING_QUEUE:
                toSellingQueue(); break;
            case SEARCH_PARLAY:
                toSearchParlay(); break;
            case PARLAY_SUMMARY:
                toParlaySummary(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void toSellingQueue() {
        
        FinalUser seller = auth.sessionUser(request);
        List<Parlay> parlaysInQueue;
        parlaysInQueue = parlayService.getParlaysInQueue(seller);

        request.setAttribute(Attribute.PARLAYS, parlaysInQueue);
        forward(getJSP(SELLING_QUEUE));
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
        forward(getJSP(SEARCH_PARLAY));
    }

    private void toParlaySummary() {
        
        String strParlayId = request.getParameter(Parameter.PARLAY);
        
        Long parlayId;
        try {
            parlayId = Long.parseLong(strParlayId);
        } catch(NumberFormatException ex) {
            forward(getJSP(SEARCH_PARLAY));
            return;
        }
        Parlay parlay = parlayService.getParlay(parlayId);
        request.setAttribute(Attribute.PARLAY, parlay);
        request.setAttribute(Attribute.PARLAY_ODDS, parlay.getOdds());
        forward(getJSP(PARLAY_SUMMARY));
    }
}
