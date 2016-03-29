/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller.event;

import co.com.bookmaker.business_logic.controller.GenericController;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "MatchPeriodController", urlPatterns = {"/"+MatchPeriodController.URL})
public class MatchPeriodController extends GenericController {

    public static final String URL = "period";

    @Override
    public void init() {
        
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/match_period/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }
}
