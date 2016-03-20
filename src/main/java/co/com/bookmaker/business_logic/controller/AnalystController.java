/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller;

import co.com.bookmaker.business_logic.service.event.MatchEventService;
import co.com.bookmaker.business_logic.service.event.MatchEventPeriodService;
import co.com.bookmaker.business_logic.service.event.ScoreService;
import co.com.bookmaker.business_logic.service.event.SportService;
import co.com.bookmaker.business_logic.service.event.TeamService;
import co.com.bookmaker.business_logic.service.event.TournamentService;
import co.com.bookmaker.business_logic.service.parlay.ParlayOddService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.MatchEventPeriod;
import co.com.bookmaker.data_access.entity.event.Score;
import co.com.bookmaker.data_access.entity.event.Sport;
import co.com.bookmaker.data_access.entity.event.Team;
import co.com.bookmaker.data_access.entity.event.Tournament;
import co.com.bookmaker.data_access.entity.parlay.ParlayOdd;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.form.bean.MatchEventBean;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.form.bean.MatchResultBean;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.SportID;
import co.com.bookmaker.util.type.Status;
import co.com.bookmaker.util.type.OddType;
import java.util.Calendar;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "AnalystController", urlPatterns = {"/"+AnalystController.URL})
public class AnalystController extends GenericController {

    public static final String URL = "analyst";
    
    public static final String SIDEBAR = "sidebar";
    
    public static final String NEW_MATCH_SPORT_SELECTION = "new_match_sport_selection";
    public static final String NEW_MATCH = "new_match";
    public static final String MATCH_SUMMARY = "match_summary";
    public static final String EDIT_MATCH = "edit_match";
    public static final String SEARCH_MATCH = "search_match";
    public static final String MATCH_SEARCH_RESULT = "match_search_result";
    public static final String PENDING_RESULT = "pending_result";
    public static final String MATCH_RESULT = "match_result";
    
    public static final String NEW_TOURNAMENT = "new_tournament";
    public static final String EDIT_TOURNAMENT = "edit_tournament";
    public static final String SEARCH_TOURNAMENT = "search_tournament";
    public static final String TOURNAMENT_SUMMARY = "tournament_summary";
    public static final String TOURNAMENT_SEARCH_RESULT = "tournament_search_result";
    
    private AuthenticationService auth;
    private SportService sportService;
    private TournamentService tournamentService;
    private MatchEventPeriodService matchPeriodService;
    private TeamService teamService;
    private MatchEventService matchEventService;
    private ParlayOddService parlayOddService;
    private ScoreService scoreService;
    
    @Override
    public void init() {
        
        auth = new AuthenticationService();
        matchEventService = new MatchEventService();
        sportService = new SportService();
        teamService = new TeamService();
        parlayOddService = new ParlayOddService();
        matchPeriodService = new MatchEventPeriodService();
        tournamentService = new TournamentService();
        
        allowTO(INDEX, Role.ANALYST);
        
        allowTO(NEW_MATCH_SPORT_SELECTION, Role.ANALYST);
        allowTO(NEW_MATCH, Role.ANALYST);
        allowTO(EDIT_MATCH, Role.ANALYST);
        allowTO(SEARCH_MATCH, Role.ANALYST);
        allowTO(MATCH_SUMMARY, Role.ANALYST);
        allowTO(MATCH_RESULT, Role.ANALYST);
        allowTO(PENDING_RESULT, Role.ANALYST);
        
        allowTO(NEW_TOURNAMENT, Role.ANALYST);
        allowTO(EDIT_TOURNAMENT, Role.ANALYST);
        allowTO(SEARCH_TOURNAMENT, Role.ANALYST);
        allowTO(TOURNAMENT_SUMMARY, Role.ANALYST);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/analyst/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        
        switch(resource) {
            case NEW_MATCH_SPORT_SELECTION:
                toNewMatchSportSelection(); break;
            case NEW_MATCH:
                toNewMatch(); break;
            case EDIT_MATCH:
                toEditMatch(); break;
            case SEARCH_MATCH:
                toSearchMatch(); break;
            case MATCH_SUMMARY:
                toMatchSummary(); break;
            case MATCH_RESULT:
                toMatchResult(); break;
            case INDEX:
            case PENDING_RESULT:
                toPendingResult(); break;
            case NEW_TOURNAMENT:
                toNewTournament(); break;
            case EDIT_TOURNAMENT:
                toEditTournament(); break;
            case SEARCH_TOURNAMENT:
                toSearchTournament(); break;
            case TOURNAMENT_SUMMARY:
                toTournamentSummary(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void processDO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    private void toNewMatchSportSelection() {
        
        List<Sport> sports = sportService.getSports(Status.ACTIVE);
        request.setAttribute(Attribute.SPORTS, sports);
        forward(getJSP(NEW_MATCH_SPORT_SELECTION));
    }

    private void toNewMatch() {
        
        String strSportId = request.getParameter(Parameter.SPORT);
        Integer sportId;
        try {
            sportId = Integer.parseInt(strSportId);
            // validate out of bounds
        } catch (NumberFormatException ex) {
            forward(getJSP(NEW_MATCH_SPORT_SELECTION));
            return;
        }
        
        if (sportId < 0 || sportId >= SportID.N_SPORTS) {
            forward(getJSP(NEW_MATCH_SPORT_SELECTION));
            return;
        }
        
        int nPeriods = SportID.nPeriods[sportId];
        int nTeams = SportID.nTeams[sportId];
        List<String> periodsName = SportID.periodsName[sportId];
        
        MatchEventBean formData = new MatchEventBean(nPeriods, nTeams);
        formData.setnPeriods(nPeriods);
        formData.setnTeams(nTeams);
        formData.setSportId(sportId);
        formData.setPeriodNames(periodsName);
        
        request.setAttribute(Attribute.MATCH_EVENT, formData);
        request.setAttribute(Attribute.TOURNAMENT_SERVICE, tournamentService);
        
        forward(getJSP(NEW_MATCH));
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

    private void toMatchSummary() {
        
        String strMatchId = request.getParameter(Parameter.MATCH_EVENT);
        
        Long matchId;
        try {
            matchId = Long.parseLong(strMatchId);
        } catch(Exception ex) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        MatchEvent match = matchEventService.getMatchEvent(matchId);
        matchEventService.update(match);
        
        if (match == null) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        request.setAttribute(Attribute.MATCH_EVENT, match);
        request.setAttribute(Attribute.TEAM_SERVICE, teamService);
        request.setAttribute(Attribute.PARLAYODD_SERVICE, parlayOddService);
        request.setAttribute(Attribute.MATCH_PERIOD_SERVICE, matchPeriodService);
        forward(getJSP(MATCH_SUMMARY));
    }
    
    private void toMatchResult() {
        
        String strMatchId = request.getParameter(Parameter.MATCH_EVENT);
        
        Long matchId;
        try {
            matchId = Long.parseLong(strMatchId);
        } catch(Exception ex) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        
        MatchEvent match = matchEventService.getMatchEvent(matchId);
        matchEventService.update(match);
        
        if (match == null) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        else if ((match.getStatus() != Status.FINISHED && match.getStatus() != Status.PENDING_RESULT)
                || !match.getAuthor().equals(auth.sessionUser(request))) {
            request.setAttribute(Information.ERROR, "Restricted operation");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        Sport sport = match.getTournament().getSport();
        int nPeriods = SportID.nPeriods[sport.getId()];
        int nTeams = SportID.nTeams[sport.getId()];
        
        MatchResultBean matchResult = new MatchResultBean(nPeriods, nTeams);
        matchResult.setPeriodNames(SportID.periodsName[sport.getId()]);
        matchResult.setMatchId(match.getId());
        matchResult.setSportName(sport.getName());
        matchResult.setMatchName(match.getName());
        matchResult.setTournamentName(match.getTournament().getName());
        
        List<Team> teams = teamService.getTeams(match);
        for (int i = 0; i < teams.size(); i++) {
            matchResult.setTeamName(i, teams.get(i).getName());
        }
        for (Score s : scoreService.getScores(match)) {
            matchResult.setScore(s.getPeriod().getNumber(), s.getTeam().getNumber(), formatResult(s.getVal()));
        }
        request.setAttribute(Attribute.MATCH_RESULT, matchResult);
        forward(getJSP(MATCH_RESULT));
    }
    
    private void toNewTournament() {
        
        List<Sport> sports = sportService.getSports(Status.ACTIVE);
        request.setAttribute(Attribute.SPORTS, sports);
        forward(getJSP(NEW_TOURNAMENT));
    }

    private void toEditMatch() {
        
        String strMatchId = request.getParameter(Parameter.MATCH_EVENT);
        
        Long matchId;
        try {
            matchId = Long.parseLong(strMatchId);
        } catch(Exception ex) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        
        MatchEvent match = matchEventService.getMatchEvent(matchId);
        matchEventService.update(match);
        
        if (match == null) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        else if ((match.getStatus() != Status.ACTIVE && match.getStatus() != Status.INACTIVE)
                || !match.getAuthor().equals(auth.sessionUser(request))) {
            request.setAttribute(Information.ERROR, "Restricted operation");
            forward(getJSP(SEARCH_MATCH));
            return;
        }
        
        List<MatchEventPeriod> periods = matchPeriodService.getPeriods(match);
        List<Team> teams = teamService.getTeams(match);
        
        int nPeriods = periods.size();
        int nTeams = teams.size();
        int sportId = match.getTournament().getSport().getId();
        int status = match.getStatus();
        
        MatchEventBean formData = new MatchEventBean(nPeriods, nTeams);
        formData.setnTeams(nTeams);
        formData.setnPeriods(nPeriods);
        formData.setTournament(match.getTournament());
        formData.setId(matchId);
        formData.setSportId(sportId);
        formData.setStatus(status);
        formData.setName(match.getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        formData.setStartDate(dateFormat.format(match.getStartDate().getTime()));
        formData.setPeriodNames(SportID.periodsName[sportId]);
        
        for (int t = 0; t < nTeams; t++) {
            formData.setTeam(t, teams.get(t).getName());
        }
        for (int p = 0; p < nPeriods; p++) {
            ParlayOdd draw = parlayOddService.getOddByPeriod(periods.get(p), OddType.DRAW_LINE, Status.SELLING);
            if (draw != null) {
                Double line = draw.getLine();
                String strLine = format(line);
                formData.setDrawLine(p, strLine);
            }
            ParlayOdd over = parlayOddService.getOddByPeriod(periods.get(p), OddType.TOTAL_OVER, Status.SELLING);
            if (over != null) {
                ParlayOdd under = parlayOddService.getOddByPeriod(periods.get(p), OddType.TOTAL_UNDER, Status.SELLING);
                
                Double points = over.getPoints();
                String strPoints = format(points);
                formData.setTotalPoints(p, strPoints);
                
                Double lOver = over.getLine();
                String strLineOver = format(lOver);
                formData.setLineOver(p, strLineOver);
                
                Double lUnder = under.getLine();
                String strLineUnder = format(lUnder);
                formData.setLineUnder(p, strLineUnder);
            }
            ParlayOdd spread0 = parlayOddService.getOddByPeriod(periods.get(p), OddType.SPREAD_TEAM0, Status.SELLING);
            if (spread0 != null) {
                ParlayOdd spread1 = parlayOddService.getOddByPeriod(periods.get(p), OddType.SPREAD_TEAM1, Status.SELLING);
                
                Double points = spread0.getPoints();
                String strPoints = format(points);
                formData.setSpreadPoints(p, strPoints);
                
                Double line0 = spread0.getLine();
                String strLine0 = format(line0);
                formData.setLineTeam0(p, strLine0);
                
                Double line1 = spread1.getLine();
                String strLine1 = format(line1);
                formData.setLineTeam1(p, strLine1);
            }
            for (int t = 0; t < nTeams; t++) {
                ParlayOdd ml = parlayOddService.getOdd(teams.get(t), periods.get(p), OddType.MONEY_LINE, Status.SELLING);
                if (ml != null) {
                    Double line = ml.getLine();
                    String strLine = format(line);
                    formData.setMoneyLine(p, t, strLine);
                }
            }
        }
        request.setAttribute(Attribute.MATCH_EVENT, formData);
        request.setAttribute(Attribute.TOURNAMENT_SERVICE, tournamentService);
        forward(getJSP(EDIT_MATCH));
    }
    
    private String formatResult(Double v) {
        
        String s = String.format("%d", v.intValue());
        if (Math.abs(v-v.intValue()) > 0.0) {
            s = String.format("%.1f", v);
        }
        return s;
    }
    
    private String format(Double v) {
        
        String s = String.format("%d", v.intValue());
        if (Math.abs(v-v.intValue()) > 0.0) {
            s = String.format("%.1f", v);
        }
        if (v > 0.0) s = "+"+s;
        return s;
    }
    
    private Tournament getTournament() {
        
        String strTournamentId = request.getParameter(Parameter.TOURNAMENT);
        Long tournamentId;
        try {
            tournamentId = Long.parseLong(strTournamentId);
        } catch(Exception ex) {
            forward(getJSP(SEARCH_TOURNAMENT));
            return null;
        }
        Tournament tournament = tournamentService.getTournament(tournamentId);
        if (tournament == null) {
            forward(getJSP(SEARCH_TOURNAMENT));
            return null;
        }
        Agency agency = tournament.getAuthor().getAgency();
        Agency sAgency = auth.sessionUser(request).getAgency();
        if ((sAgency != null && agency != null && !sAgency.equals(agency))
            || (agency == null && sAgency != null && !sAgency.getAcceptGlobalOdds())) {
            request.setAttribute(Information.ERROR, "Restricted operation");
            forward(getJSP(SEARCH_TOURNAMENT));
            return null;
        }
        return tournament;
    }
    
    private void toEditTournament() {
        
        Tournament tournament = getTournament();
        if (tournament == null) {
            return;
        }
        List<Sport> sports = sportService.getSports(Status.ACTIVE);
        request.setAttribute(Attribute.SPORTS, sports);
        request.setAttribute(Attribute.TOURNAMENT, tournament);
        forward(getJSP(EDIT_TOURNAMENT));
    }
    
    private void toSearchTournament() {
        
        List<Sport> sports = sportService.getSports(Status.ACTIVE);
        request.setAttribute(Attribute.SPORTS, sports);
        forward(getJSP(SEARCH_TOURNAMENT));
    }

    private void toTournamentSummary() {
        
        Tournament tournament = getTournament();
        if (tournament == null) {
            return;
        }
        request.setAttribute(Attribute.TOURNAMENT, tournament);
        forward(getJSP(TOURNAMENT_SUMMARY));
    }

    private void toPendingResult() {
        
        List<MatchEvent> activeMatches = matchEventService.getMatches(auth.sessionUser(request), Status.ACTIVE);
        for (MatchEvent m : activeMatches) {
            matchEventService.update(m);
        }
        List<MatchEvent> matches = matchEventService.getMatches(auth.sessionUser(request), Status.PENDING_RESULT);
        request.setAttribute(Attribute.MATCH_EVENT, matches);
        forward(getJSP(PENDING_RESULT));
    }
}
