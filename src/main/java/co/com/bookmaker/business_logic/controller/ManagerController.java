/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.AgencyService;
import co.com.bookmaker.business_logic.service.FinalUserService;
import co.com.bookmaker.business_logic.service.event.MatchEventPeriodService;
import co.com.bookmaker.business_logic.service.event.MatchEventService;
import co.com.bookmaker.business_logic.service.event.SportService;
import co.com.bookmaker.business_logic.service.event.TeamService;
import co.com.bookmaker.business_logic.service.event.TournamentService;
import co.com.bookmaker.business_logic.service.parlay.ParlayOddService;
import co.com.bookmaker.business_logic.service.parlay.ParlayService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.Sport;
import co.com.bookmaker.data_access.entity.event.Tournament;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import co.com.bookmaker.util.form.bean.AgencyBean;
import co.com.bookmaker.util.form.bean.SearchParlayBean;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.Status;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "ManagerController", urlPatterns = {"/"+ManagerController.URL})
public class ManagerController extends GenericController {

    public static final String URL = "manager";
    
    public static final String SIDEBAR = "sidebar";
    
    public static final String AGENCY_SUMMARY = "agency_summary";
    public static final String AGENCY_BALANCE = "agency_balance";
    public static final String EMPLOYEE_SUMMARY = "employee_summary";
    public static final String EMPLOYEE_BALANCE = "employee_balance";
    public static final String SEARCH_PARLAY = "search_parlay";
    public static final String SEARCH_MATCH = "search_match";
    
    public static final String PARLAY_SEARCH_RESULT = "parlay_search_result";
    public static final String MATCH_SEARCH_RESULT = "match_search_result";
    
    public static final String PARLAY_SUMMARY = "parlay_summary";
    public static final String MATCH_SUMMARY = "match_summary";
    public static final String TOURNAMENT_SUMMARY = "tournament_summary";
    
    public static final String EDIT_AGENCY = "edit_agency";
    
    private AuthenticationService auth;
    private AgencyService agencyService;
    private ParlayService parlayService;
    private FinalUserService finalUserService;
    private MatchEventService matchEventService;
    private SportService sportService;
    private TeamService teamService;
    private ParlayOddService parlayOddService;
    private MatchEventPeriodService matchPeriodService;
    private TournamentService tournamentService;
    
    @Override
    public void init() {
        
        finalUserService = new FinalUserService();
        auth = new AuthenticationService();
        agencyService = new AgencyService();
        parlayService = new ParlayService();
        matchEventService = new MatchEventService();
        sportService = new SportService();
        teamService = new TeamService();
        parlayOddService = new ParlayOddService();
        matchPeriodService = new MatchEventPeriodService();
        tournamentService = new TournamentService();
        
        allowTO(INDEX, Role.MANAGER);
        allowTO(AGENCY_SUMMARY, Role.MANAGER);
        allowTO(AGENCY_BALANCE, Role.MANAGER);
        allowTO(SEARCH_PARLAY, Role.MANAGER);
        allowTO(EMPLOYEE_BALANCE, Role.MANAGER);
        allowTO(SEARCH_MATCH, Role.MANAGER);
        allowTO(PARLAY_SUMMARY, Role.MANAGER);
        allowTO(MATCH_SUMMARY, Role.MANAGER);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/manager/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        
        switch (resource) {
            case INDEX:
            case AGENCY_SUMMARY:
                toAgencySummary(); break;
            case AGENCY_BALANCE:
                toAgencyBalance(); break;
            case SEARCH_PARLAY:
                toSearchParlay(); break;
            case EMPLOYEE_SUMMARY:
                toEmployeeSummary(); break;
            case EMPLOYEE_BALANCE:
                toEmployeeBalance(); break;
            case SEARCH_MATCH:
                toSearchMatch(); break;
            case PARLAY_SUMMARY:
                toParlaySummary(); break;
            case MATCH_SUMMARY:
                toMatchSummary(); break;
            case TOURNAMENT_SUMMARY:
                toTournamentSummary(); break;
            case EDIT_AGENCY:
                toEditAgency(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void toAgencySummary() {
        
        Agency agency = auth.sessionUser(request).getAgency();
        request.setAttribute(Attribute.AGENCY, agency);
        request.setAttribute(Attribute.EMPLOYEES, agencyService.getEmployees(agency));
        request.setAttribute(Attribute.AUTHENTICATION_SERVICE, auth);
        forward(getJSP(AGENCY_SUMMARY));
    }

    private void toAgencyBalance() {
        
        Agency agency = auth.sessionUser(request).getAgency();
        
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
        
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        request.setAttribute(Attribute.AGENCY, agency);
        forward(getJSP(AGENCY_BALANCE));
    }

    private void toEmployeeSummary() {
        
        String username = request.getParameter(Parameter.USERNAME);
        
        if (username == null) {
            redirect(HomeController.URL);
            return;
        }
        FinalUser result = finalUserService.getUser(username);
        if (result != null) {
            request.setAttribute(Attribute.FINAL_USER, result);
            request.setAttribute(Attribute.ONLINE, auth.isOnline(result, request));
            forward(getJSP(EMPLOYEE_SUMMARY));
        } else {
            redirect(HomeController.URL);
        }
    }
    
    private void toEmployeeBalance() {
        
        String username = request.getParameter(Parameter.USERNAME);
        
        if (username == null) {
            redirect(HomeController.URL);
            return;
        }
        FinalUser user = finalUserService.getUser(username);
        if (user == null) {
            redirect(HomeController.URL);
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
        forward(getJSP(EMPLOYEE_BALANCE));
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

    private void toSearchMatch() {
        
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
        
        List<Sport> sports = sportService.getSports(Status.ACTIVE);
        
        request.setAttribute(Attribute.TIME_FROM, strFrom);
        request.setAttribute(Attribute.TIME_TO, strTo);
        request.setAttribute(Attribute.SPORTS, sports);
        forward(getJSP(SEARCH_MATCH));
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

    private void toMatchSummary() {
       
        String strMatchId = request.getParameter(Parameter.MATCH_EVENT);
        
        Long matchId;
        try {
            matchId = Long.parseLong(strMatchId);
        } catch(Exception ex) {
            request.setAttribute(Information.SEARCH_RESULT, "Juego "+strMatchId+" no encontrado");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        MatchEvent match = matchEventService.getMatchEvent(matchId);
        matchEventService.update(match);
        
        if (match == null) {
            request.setAttribute(Information.SEARCH_RESULT, "Juego "+strMatchId+" no encontrado");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        request.setAttribute(Attribute.MATCH_EVENT, match);
        request.setAttribute(Attribute.TEAM_SERVICE, teamService);
        request.setAttribute(Attribute.PARLAYODD_SERVICE, parlayOddService);
        request.setAttribute(Attribute.MATCH_PERIOD_SERVICE, matchPeriodService);
        forward(getJSP(MATCH_SUMMARY));
    }

    private void toTournamentSummary() {
        
        String strTournamentId = request.getParameter(Parameter.TOURNAMENT);
        Long tournamentId;
        try {
            tournamentId = Long.parseLong(strTournamentId);
        } catch(Exception ex) {
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        Tournament tournament = tournamentService.getTournament(tournamentId);
        if (tournament == null) {
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        Agency agency = tournament.getAuthor().getAgency();
        Agency sAgency = auth.sessionUser(request).getAgency();
        if ((sAgency != null && agency != null && !sAgency.equals(agency))
            || (agency == null && sAgency != null && !sAgency.getAcceptGlobalOdds())) {
            request.setAttribute(Information.ERROR, "Restricted operation");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        request.setAttribute(Attribute.TOURNAMENT, tournament);
        forward(getJSP(TOURNAMENT_SUMMARY));
    }

    private void toEditAgency() {
        
        String strAgencyId = request.getParameter(Parameter.AGENCY);
        Long agencyId = null;
        if (strAgencyId != null) {
            try {
                agencyId = Long.parseLong(strAgencyId);
            } catch(Exception ex) {
                redirect(HomeController.URL);
                return;
            }
        }
        Agency agency = agencyService.getAgency(agencyId);
        if (agency == null) {
            redirect(HomeController.URL);
            return;
        }
        AgencyBean a = new AgencyBean();
        a.setId(agency.getId());
        a.setMinOdds(agency.getMinOddsParlay()+"");
        a.setMaxOdds(agency.getMaxOddsParlay()+"");
        a.setMaxProfit(agency.getMaxProfit()+"");

        request.setAttribute(Attribute.AGENCY, a);
        forward(getJSP(EDIT_AGENCY));
    }
}
