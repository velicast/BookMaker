/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.FinalUserService;
import co.com.bookmaker.business_logic.service.event.MatchEventService;
import co.com.bookmaker.business_logic.service.event.MatchEventPeriodService;
import co.com.bookmaker.business_logic.service.event.TeamService;
import co.com.bookmaker.business_logic.service.event.TournamentService;
import co.com.bookmaker.business_logic.service.parlay.ParlayOddService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.Tournament;
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
@WebServlet(name = "ClientController", urlPatterns = {"/"+ClientController.URL})
public class ClientController extends GenericController {

    public static final String URL = "client";
    
    public static final String MATCHES = "matches";
    
    private TeamService teamService;
    private TournamentService tournamentService;
    private MatchEventService matchEventService;
    private MatchEventPeriodService matchPeriodService;
    private AuthenticationService auth;
    private ParlayOddService parlayOddService;
    private FinalUserService finalUserService;
    
    @Override
    public void init() {
        
        auth = new AuthenticationService();
        matchEventService = new MatchEventService();
        teamService = new TeamService();
        parlayOddService = new ParlayOddService();
        matchPeriodService = new MatchEventPeriodService();
        tournamentService = new TournamentService();
        finalUserService = new FinalUserService();
        
        allowTO(INDEX, Role.CLIENT);
        allowTO(MATCHES, Role.CLIENT);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/client/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        
        FinalUser user = auth.sessionUser(request);
        if (user.getAgency() == null) {
            request.setAttribute(Information.INFO, "El usuario "+user.getUsername()+" no está empleado en una agencia");
            forward(HomeController.getJSP(HomeController.INDEX));
            return;
        }
        
        switch (resource) {
            case INDEX:
                toIndex(); break;
            case MATCHES:
                toMatches(); break;
        }
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    protected void toIndex() {
        
        finalUserService.addClientData(request);
        forward(getJSP(INDEX));
    }

    private void toMatches() {
        
        String strTournamentId = request.getParameter(Parameter.TOURNAMENT);
        Long tournamentId;
        try {
            tournamentId = Long.parseLong(strTournamentId);
        } catch(Exception ex) {
            return;
        }
        Tournament tournament = tournamentService.getTournament(tournamentId);
        if (tournament == null) {
            return;
        }
        request.setAttribute(Attribute.TOURNAMENT, tournament);
        request.setAttribute(Attribute.TEAM_SERVICE, teamService);
        request.setAttribute(Attribute.PARLAYODD_SERVICE, parlayOddService);
        request.setAttribute(Attribute.TOURNAMENT_SERVICE, tournamentService);
        request.setAttribute(Attribute.MATCH_EVENT_SERVICE, matchEventService);
        request.setAttribute(Attribute.MATCH_PERIOD_SERVICE, matchPeriodService);
        forward(getJSP(MATCHES));
    }
}
