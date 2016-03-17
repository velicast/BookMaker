/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller.event;

import co.com.bookmaker.business_logic.controller.AnalystController;
import co.com.bookmaker.business_logic.controller.GenericController;
import co.com.bookmaker.business_logic.controller.HomeController;
import co.com.bookmaker.business_logic.controller.ManagerController;
import co.com.bookmaker.business_logic.service.ParameterValidator;
import co.com.bookmaker.business_logic.service.event.MatchEventService;
import co.com.bookmaker.business_logic.service.event.MatchEventPeriodService;
import co.com.bookmaker.business_logic.service.event.TeamService;
import co.com.bookmaker.business_logic.service.event.TournamentService;
import co.com.bookmaker.business_logic.service.parlay.ParlayOddService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.MatchEventPeriod;
import co.com.bookmaker.data_access.entity.event.Sport;
import co.com.bookmaker.data_access.entity.event.Team;
import co.com.bookmaker.data_access.entity.event.Tournament;
import co.com.bookmaker.data_access.entity.parlay.ParlayOdd;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.form.bean.MatchEventBean;
import co.com.bookmaker.util.form.bean.MatchResultBean;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.SportID;
import co.com.bookmaker.util.type.Status;
import co.com.bookmaker.util.type.OddType;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "MatchEventController", urlPatterns = {"/"+MatchEventController.URL})
public class MatchEventController extends GenericController {
    
    public static final String URL = "match";

    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String SUMMARY = "summary";
    public static final String SEARCH = "search";
    public static final String SEARCH_RESULT = "search_result";
    public static final String RESULT = "result";
    public static final String CANCEL = "cancel";
    
    @EJB
    private AuthenticationService auth;
    @EJB
    private TournamentService tournamentService;
    @EJB
    private ParlayOddService parlayOddService;
    @EJB
    private MatchEventPeriodService matchPeriodService;
    @EJB
    private TeamService teamService;
    @EJB
    private MatchEventService matchEventService;
    @EJB
    private ParameterValidator validator;
    
    @Override
    public void init() {
        
        allowDO(NEW, Role.ANALYST);
        allowDO(EDIT, Role.ANALYST);
        allowDO(SEARCH, Role.ANALYST|Role.MANAGER);
        allowDO(RESULT, Role.ANALYST);
        allowDO(CANCEL, Role.ANALYST);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/match_event/"+resource+".jsp";
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
            case CANCEL:
                doCancel(); break;
            case RESULT:
                doResult(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void doNew() {
        
        String strSportId = request.getParameter(Parameter.SPORT);
        String strTournamentId = request.getParameter(Parameter.TOURNAMENT);
        String name = request.getParameter(Parameter.NAME);
        String strStartDate = request.getParameter(Parameter.START_DATE);
        String strStatus = request.getParameter(Parameter.STATUS);
        
        boolean validated = true;
        
        try {
            validator.checkMatchName(name);
        } catch (EJBException ex) {
            validated = false;
            request.setAttribute(Information.NAME, ex.getCausedByException().getMessage());
        }
        
        Integer sportId = null;
        try {
            sportId = Integer.parseInt(strSportId);
        } catch(Exception ex) {
            request.setAttribute(Information.SPORT, "Invalid Sport ID ");
            validated = false;
        }
        if (sportId == null || sportId < 0 || sportId >= SportID.N_SPORTS) {
            request.setAttribute(Information.SPORT, "Invalid Sport ID");
            return;
        }
        
        Long tournamentId = null;
        try {
            tournamentId = Long.parseLong(strTournamentId);
        } catch(Exception ex) {
            request.setAttribute(Information.TOURNAMENT, "Invalid tournament ID");
            validated = false;
        }
        Integer status = null;
        try {
            status = Integer.parseInt(strStatus);
        } catch(Exception ex) {
            request.setAttribute(Information.STATUS, "Invalid status");
            validated = false;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dStartDate = null;
        try {
            dStartDate = dateFormat.parse(strStartDate);
        } catch (ParseException ex) {
            request.setAttribute(Information.START_DATE, "Invalid start date");
            validated = false;
        }
        
        Tournament tournament = null;
        if (tournamentId != null) {
            tournament = tournamentService.getTournament(tournamentId);
        }
        if (tournament == null) {
            request.setAttribute(Information.TOURNAMENT, "Tournament not found");
            validated = false;
        }
        Calendar startDate = Calendar.getInstance();
        if (dStartDate != null) {
            startDate.setTime(dStartDate);
            try {
                validator.checkMatchStartDate(startDate);
            } catch (EJBException ex) {
                validated = false;
                request.setAttribute(Information.START_DATE, ex.getCausedByException().getMessage());
            }
        }
        
        int nPeriods = SportID.nPeriods[sportId];
        int nTeams = SportID.nTeams[sportId];
        List<String> periodsName = SportID.periodsName[sportId];

        MatchEvent match = new MatchEvent();
        
        match.setAuthor(auth.sessionUser(request));
        match.setTournament(tournament);
        match.setName(name);
        match.setStartDate(startDate);
        match.setStatus(status);

        MatchEventPeriod[] periods = new MatchEventPeriod[nPeriods];
        Team[] teams = new Team[nTeams];
        ParlayOdd[] totalsOver = new ParlayOdd[nPeriods];
        ParlayOdd[] totalsUnder = new ParlayOdd[nPeriods];
        ParlayOdd[] spreadsTeam0 = new ParlayOdd[nPeriods];
        ParlayOdd[] spreadsTeam1 = new ParlayOdd[nPeriods];
        ParlayOdd[] drawLines = new ParlayOdd[nPeriods];
        ParlayOdd[][] moneyLines = new ParlayOdd[nPeriods][nTeams];
        
        MatchEventBean formData = new MatchEventBean(nPeriods, nTeams);
        formData.setnPeriods(nPeriods);
        formData.setnTeams(nTeams);
        formData.setSportId(sportId);
        formData.setStatus(status);
        formData.setName(name);
        formData.setStartDate(strStartDate);
        formData.setPeriodNames(periodsName);
        
        for (int t = 0; t < nTeams; t++) {
            String pTeamName = Parameter.NAME+" "+t;
            
            String strTeamName = request.getParameter(pTeamName).trim();
            
            try {
                validator.checkTeamName(strTeamName);
            } catch (EJBException ex) {
                validated = false;
                request.setAttribute(Information.TEAM+" "+t, ex.getCausedByException().getMessage());
            }
            
            Team team = new Team();
            team.setName(strTeamName);
            team.setNumber(t);
            teams[t] = team;
            
            formData.setTeam(t, strTeamName);
        }
        
        boolean[] activePeriod = new boolean[nPeriods];
        
        for (int p = 0; p < nPeriods; p++) { // PERIOD

            MatchEventPeriod period = new MatchEventPeriod();
            period.setCutoff(startDate);
            period.setName(periodsName.get(p));
            period.setNumber(p);
            period.setStatus(status);
            
            if (nTeams == 2) {
                String pTotalPoints = Parameter.TOTAL_POINTS+" "+p;
                String pOverLine = Parameter.TOTAL_OVER_LINE+" "+p;
                String pUnderLine = Parameter.TOTAL_UNDER_LINE+" "+p;

                String strTotalPoints = request.getParameter(pTotalPoints).trim();
                String strOverLine = request.getParameter(pOverLine).trim();
                String strUnderLine = request.getParameter(pUnderLine).trim();

                formData.setTotalPoints(p, strTotalPoints);
                formData.setLineOver(p, strOverLine);
                formData.setLineUnder(p, strUnderLine);
                
                if (strTotalPoints.length() == 0) strTotalPoints = null;
                if (strOverLine.length() == 0) strOverLine = null;
                if (strUnderLine.length() == 0) strUnderLine = null;

                boolean addTotal = strTotalPoints != null && strOverLine != null  && strUnderLine != null;
                if (addTotal) {
                    Double totalPoints = null;
                    Double overValue = null;
                    Double underValue = null;

                    try {
                        totalPoints = Double.parseDouble(strTotalPoints);
                    } catch (Exception ex) {
                        request.setAttribute(Information.TOTAL+" "+p, "Invalid total points");
                        validated = false;
                    }
                    try {
                        overValue = Double.parseDouble(strOverLine);
                    } catch (Exception ex) {
                        request.setAttribute(Information.TOTAL+" "+p, "Invalid total line");
                        validated = false;
                    }
                    try {
                        underValue = Double.parseDouble(strUnderLine);
                    } catch (Exception ex) {
                        request.setAttribute(Information.TOTAL+" "+p, "Invalid total line");
                        validated = false;
                    }
                    if (validated) {
                        ParlayOdd over = new ParlayOdd();
                        over.setType(OddType.TOTAL_OVER);
                        over.setPoints(totalPoints);
                        over.setLine(overValue);
                        over.setStatus(Status.SELLING);
                        totalsOver[p] = over;

                        ParlayOdd under = new ParlayOdd();
                        under.setType(OddType.TOTAL_UNDER);
                        under.setPoints(totalPoints);
                        under.setLine(underValue);
                        under.setStatus(Status.SELLING);
                        totalsUnder[p] = under;
                        
                        activePeriod[p] = true;
                    }
                }
                
                String pSpreadPoints = Parameter.SPREAD+" "+p;
                String pSpreadTeam0Line = Parameter.SPREAD_TEAM0_LINE+" "+p;
                String pSpreadTeam1Line = Parameter.SPREAD_TEAM1_LINE+" "+p;

                String strSpreadPoints = request.getParameter(pSpreadPoints);
                String strSpreadTeam0Line = request.getParameter(pSpreadTeam0Line);
                String strSpreadTeam1Line = request.getParameter(pSpreadTeam1Line);

                formData.setSpreadPoints(p, strSpreadPoints);
                formData.setLineTeam0(p, strSpreadTeam0Line);
                formData.setLineTeam1(p, strSpreadTeam1Line);
                
                if (strSpreadPoints.length() == 0) strSpreadPoints = null;
                if (strSpreadTeam0Line.length() == 0) strSpreadTeam0Line = null;
                if (strSpreadTeam1Line.length() == 0) strSpreadTeam1Line = null;

                boolean addSpread = strSpreadPoints != null || strSpreadTeam0Line != null || strSpreadTeam1Line != null;
                if (addSpread) {
                    Double spreadPoints = null;
                    Double spreadTeam0Line = null;
                    Double spreadTeam1Line = null;

                    try {
                        spreadPoints = Double.parseDouble(strSpreadPoints);
                    } catch(Exception ex) {
                        request.setAttribute(Information.SPREAD+" "+p, "Invalid spread points");
                        validated = false;
                    }
                    try {
                        spreadTeam0Line = Double.parseDouble(strSpreadTeam0Line);
                    } catch(Exception ex) {
                        request.setAttribute(Attribute.SPREAD+" "+p, "Invalid spread line");
                        validated = false;
                    }
                    try {
                        spreadTeam1Line = Double.parseDouble(strSpreadTeam1Line);
                    } catch(Exception ex) {
                        request.setAttribute(Attribute.SPREAD+" "+p, "Invalid spread line");
                        validated = false;
                    }

                    if (validated) {
                        ParlayOdd spreadTeam0 = new ParlayOdd();
                        spreadTeam0.setLine(spreadTeam0Line);
                        spreadTeam0.setPoints(spreadPoints);
                        spreadTeam0.setType(OddType.SPREAD_TEAM0);
                        spreadTeam0.setStatus(Status.SELLING);
                        spreadsTeam0[p] = spreadTeam0;
                        
                        ParlayOdd spreadTeam1 = new ParlayOdd();
                        spreadTeam1.setLine(spreadTeam1Line);
                        spreadTeam1.setPoints(-spreadPoints);
                        spreadTeam1.setType(OddType.SPREAD_TEAM1);
                        spreadTeam1.setStatus(Status.SELLING);
                        spreadsTeam1[p] = spreadTeam1;
                        
                        activePeriod[p] = true;
                    }
                }
            }
            
            String pDrawLine = Parameter.DRAW_LINE+" "+p;
            String strDrawLine = request.getParameter(pDrawLine);
            
            formData.setDrawLine(p, strDrawLine);
            
            if (strDrawLine.trim().length() == 0) strDrawLine = null;
            
            if (strDrawLine != null) { 
                Double drawLine = null;
                try {
                    drawLine = Double.parseDouble(strDrawLine);
                } catch(Exception ex) {
                    request.setAttribute(Information.DRAW_LINE+" "+p, "Invalid draw line");
                    validated = false;
                }

                if (validated) {
                    ParlayOdd draw = new ParlayOdd();
                    draw.setType(OddType.DRAW_LINE);
                    draw.setLine(drawLine);
                    draw.setStatus(Status.SELLING);
                    drawLines[p] = draw;
                }
            }
            
            int cntMoneyLine = 0;
            for (int t = 0; t < nTeams; t++) {
                
                String pMoneyLine = Parameter.MONEY_LINE+" "+p+" "+t;
                
                String strMoneyLine = request.getParameter(pMoneyLine).trim();
                
                formData.setMoneyLine(p, t, strMoneyLine);
                
                if (strMoneyLine.length() == 0) strMoneyLine = null;
                
                if (strMoneyLine == null && cntMoneyLine > 0) {
                    request.setAttribute(Information.MONEY_LINE+" "+p+" "+t, "Incomplete moneylines");
                    validated = false;
                }
                if (strMoneyLine != null) {
                    Double line = null;
                    try {
                        line = Double.parseDouble(strMoneyLine);
                    } catch(Exception ex) {
                        request.setAttribute(Information.MONEY_LINE+" "+p+" "+t, "Invalid moneyline");
                        validated = false;
                    }
                    ParlayOdd moneyLine = new ParlayOdd();
                    moneyLine.setType(OddType.MONEY_LINE);
                    moneyLine.setLine(line);
                    moneyLine.setStatus(Status.SELLING);
                    moneyLines[p][t] = moneyLine;
                    cntMoneyLine++;
                    
                    activePeriod[p] = true;
                }
            }
            if (validated) {
                periods[p] = period;
            }
        }

        if (!validated) {
            request.setAttribute(Attribute.MATCH_EVENT, formData);
            request.setAttribute(Attribute.TOURNAMENT_SERVICE, tournamentService);
            forward(AnalystController.getJSP(AnalystController.NEW_MATCH));
            return;
        }

        try {
        matchEventService.create(match);
        
        for (int t = 0; t < nTeams; t++) {
            teams[t].setMatch(match);
            teamService.create(teams[t]);
        }
        for (int p = 0; p < nPeriods; p++) {
            periods[p].setMatch(match);
            if (!activePeriod[p]) {
                periods[p].setStatus(Status.INACTIVE);
            }
            matchPeriodService.create(periods[p]);
            
            if (drawLines[p] != null) {
                drawLines[p].setPeriod(periods[p]);
                parlayOddService.create(drawLines[p]);
            }
            if (totalsOver[p] != null) {
                totalsOver[p].setPeriod(periods[p]);
                parlayOddService.create(totalsOver[p]);

                totalsUnder[p].setPeriod(periods[p]);
                parlayOddService.create(totalsUnder[p]);
            }
            if (spreadsTeam0[p] != null) {
                spreadsTeam0[p].setPeriod(periods[p]);
                spreadsTeam0[p].setTeam(teams[0]);
                parlayOddService.create(spreadsTeam0[p]);

                spreadsTeam1[p].setPeriod(periods[p]);
                spreadsTeam1[p].setTeam(teams[1]);
                parlayOddService.create(spreadsTeam1[p]);
            }
            if (moneyLines[p][0] != null) {
                for (int t = 0; t < nTeams; t++) {
                    moneyLines[p][t].setPeriod(periods[p]);
                    moneyLines[p][t].setTeam(teams[t]);
                    parlayOddService.create(moneyLines[p][t]);
                }
            }
        }
        } catch(EJBException ex) {     
            //-----------
            // ROLLBACK!!
            // ----------
            if (match.getId() != null) {
                matchEventService.remove(match);
            }
            for (int t = 0; t < nTeams; t++) {
                if (teams[t].getId() != null) {
                    teamService.remove(teams[t]);
                }
            }
            for (int p = 0; p < nPeriods; p++) {
                if (periods[p].getId() != null) {
                    matchPeriodService.remove(periods[p]);
                }
                if (drawLines[p] != null && drawLines[p].getId() != null) {
                    parlayOddService.remove(drawLines[p]);
                }
                if (totalsOver[p] != null && totalsOver[p].getId() != null) {
                    parlayOddService.remove(totalsOver[p]);   
                }
                if (totalsUnder[p] != null && totalsUnder[p].getId() != null) {
                    parlayOddService.remove(totalsUnder[p]);   
                }
                if (spreadsTeam0[p] != null && spreadsTeam0[p].getId() != null) {
                    parlayOddService.remove(spreadsTeam0[p]);
                }
                if (spreadsTeam1[p] != null && spreadsTeam1[p].getId() != null) {
                    parlayOddService.remove(spreadsTeam1[p]);
                }
                if (moneyLines[p][0] != null) {
                    for (int t = 0; t < nTeams; t++) {
                        if (moneyLines[p][t].getId() != null) {
                            parlayOddService.remove(moneyLines[p][t]);
                        }
                    }
                }
            } // END ROLLBACK
            request.setAttribute(Information.ERROR, "Opss! Something went wrong. Please try again.");
            request.setAttribute(Attribute.MATCH_EVENT, formData);
            request.setAttribute(Attribute.TOURNAMENT_SERVICE, tournamentService);
            
            forward(AnalystController.getJSP(AnalystController.NEW_MATCH));
            return;
        }
        
        request.setAttribute(Attribute.MATCH_EVENT, match);
        request.setAttribute(Attribute.TEAM_SERVICE, teamService);
        request.setAttribute(Attribute.PARLAYODD_SERVICE, parlayOddService);
        request.setAttribute(Attribute.MATCH_PERIOD_SERVICE, matchPeriodService);
        forward(AnalystController.getJSP(AnalystController.MATCH_SUMMARY));
    }

    private void doEdit() {
        
        String strSportId = request.getParameter(Parameter.SPORT);
        String strTournamentId = request.getParameter(Parameter.TOURNAMENT);
        String strMatchId = request.getParameter(Parameter.MATCH_EVENT);
        String name = request.getParameter(Parameter.NAME);
        String strStartDate = request.getParameter(Parameter.START_DATE);
        String strStatus = request.getParameter(Parameter.STATUS);
        
        boolean validated = true;
        
        try {
            validator.checkMatchName(name);
        } catch (EJBException ex) {
            validated = false;
            request.setAttribute(Information.NAME, ex.getCausedByException().getMessage());
        }
        
        Integer sportId = null;
        try {
            sportId = Integer.parseInt(strSportId);
        } catch(Exception ex) {
            request.setAttribute(Information.SPORT, "Invalid Sport ID ");
            validated = false;
        }
        if (sportId == null || sportId < 0 || sportId >= SportID.N_SPORTS) {
            request.setAttribute(Information.SPORT, "Invalid Sport ID");
            return;
        }
        Long tournamentId = null;
        try {
            tournamentId = Long.parseLong(strTournamentId);
        } catch(Exception ex) {
            request.setAttribute(Information.TOURNAMENT, "Invalid Tournament ID ");
            validated = false;
        }
        Long matchId = null;
        try {
            matchId = Long.parseLong(strMatchId);
        } catch(Exception ex) {
            request.setAttribute(Information.MATCH_EVENT, "Invalid match ID ");
            validated = false;
        }
        Integer status = null;
        try {
            status = Integer.parseInt(strStatus);
        } catch(Exception ex) {
            request.setAttribute(Information.STATUS, "Invalid status");
            validated = false;
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dStartDate = null;
        try {
            dStartDate = dateFormat.parse(strStartDate);
        } catch (ParseException ex) {
            request.setAttribute(Information.START_DATE, "Invalid start date");
            validated = false;
        }
        
        Tournament tournament = tournamentService.getTournament(tournamentId);
        if (tournament == null) {
            request.setAttribute(Information.TOURNAMENT, "Tournament not found");
            validated = false;
        }
        Calendar startDate = Calendar.getInstance();
        if (dStartDate != null) {
            startDate.setTime(dStartDate);
            try {
                validator.checkMatchStartDate(startDate);
            } catch (EJBException ex) {
                validated = false;
                request.setAttribute(Information.START_DATE, ex.getCausedByException().getMessage());
            }
        }
        
        int nPeriods = SportID.nPeriods[sportId];
        int nTeams = SportID.nTeams[sportId];
        List<String> periodsName = SportID.periodsName[sportId];

        MatchEvent match = matchEventService.getMatchEvent(matchId);
        matchEventService.update(match);
        
        if (match == null) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(AnalystController.getJSP(AnalystController.SEARCH_MATCH));
            return;
        }
        else if ((match.getStatus() != Status.ACTIVE && match.getStatus() != Status.INACTIVE)
                    || (!match.getAuthor().equals(auth.sessionUser(request)))) {
            request.setAttribute(Information.ERROR, "Restricted operation");
            forward(AnalystController.getJSP(AnalystController.INDEX));
            return;
        }
        
        match.setAuthor(auth.sessionUser(request));
        match.setTournament(tournament);
        match.setName(name);
        match.setStartDate(startDate);
        match.setStatus(status);

        MatchEventPeriod[] periods = matchPeriodService.getPeriods(match).toArray(new MatchEventPeriod[]{});
        Team[] teams = teamService.getTeams(match).toArray(new Team[]{});
        
        ParlayOdd[] totalsOver = new ParlayOdd[nPeriods];
        ParlayOdd[] totalsUnder = new ParlayOdd[nPeriods];
        ParlayOdd[] spreadsTeam0 = new ParlayOdd[nPeriods];
        ParlayOdd[] spreadsTeam1 = new ParlayOdd[nPeriods];
        ParlayOdd[] drawLines = new ParlayOdd[nPeriods];
        ParlayOdd[][] moneyLines = new ParlayOdd[nPeriods][nTeams];
        
        ParlayOdd[] oldTotalsOver = new ParlayOdd[nPeriods];
        ParlayOdd[] oldTotalsUnder = new ParlayOdd[nPeriods];
        ParlayOdd[] oldSpreadsTeam0 = new ParlayOdd[nPeriods];
        ParlayOdd[] oldSpreadsTeam1 = new ParlayOdd[nPeriods];
        ParlayOdd[] oldDrawLines = new ParlayOdd[nPeriods];
        ParlayOdd[][] oldMoneyLines = new ParlayOdd[nPeriods][nTeams];
        
        MatchEventBean formData = new MatchEventBean(nPeriods, nTeams);
        formData.setId(matchId);
        formData.setnPeriods(nPeriods);
        formData.setnTeams(nTeams);
        formData.setSportId(sportId);
        formData.setStatus(status);
        formData.setName(name);
        formData.setStartDate(strStartDate);
        formData.setPeriodNames(periodsName);
        
        for (int t = 0; t < nTeams; t++) {
            String pTeamName = Parameter.NAME+" "+t;
            
            String strTeamName = request.getParameter(pTeamName).trim();
            
            try {
                validator.checkTeamName(strTeamName);
            } catch (EJBException ex) {
                validated = false;
                request.setAttribute(Information.TEAM+" "+t, ex.getCausedByException().getMessage());
            }
            
            Team team = teams[t];
            team.setName(strTeamName);
            team.setNumber(t);
            
            formData.setTeam(t, strTeamName);
        }
        
        for (int p = 0; p < nPeriods; p++) { // PERIOD

            MatchEventPeriod period = periods[p];

            period.setCutoff(startDate);
            period.setName(periodsName.get(p));
            period.setNumber(p);
            period.setStatus(status);
            
            oldDrawLines[p] = parlayOddService.getOddByPeriod(period, OddType.DRAW_LINE, Status.SELLING);
            
            if (nTeams == 2) {
                oldTotalsOver[p] = parlayOddService.getOddByPeriod(period, OddType.TOTAL_OVER, Status.SELLING);
                oldTotalsUnder[p] = parlayOddService.getOddByPeriod(period, OddType.TOTAL_UNDER, Status.SELLING);
                oldSpreadsTeam0[p] = parlayOddService.getOdd(teams[0], period, OddType.SPREAD_TEAM0, Status.SELLING);
                oldSpreadsTeam1[p] = parlayOddService.getOdd(teams[1], period, OddType.SPREAD_TEAM1, Status.SELLING);
                
                String pTotalPoints = Parameter.TOTAL_POINTS+" "+p;
                String pOverLine = Parameter.TOTAL_OVER_LINE+" "+p;
                String pUnderLine = Parameter.TOTAL_UNDER_LINE+" "+p;

                String strTotalPoints = request.getParameter(pTotalPoints).trim();
                String strOverLine = request.getParameter(pOverLine).trim();
                String strUnderLine = request.getParameter(pUnderLine).trim();

                formData.setTotalPoints(p, strTotalPoints);
                formData.setLineOver(p, strOverLine);
                formData.setLineUnder(p, strUnderLine);
                
                if (strTotalPoints.length() == 0) strTotalPoints = null;
                if (strOverLine.length() == 0) strOverLine = null;
                if (strUnderLine.length() == 0) strUnderLine = null;

                boolean addTotal = strTotalPoints != null && strOverLine != null  && strUnderLine != null;
                if (addTotal) {
                    Double totalPoints = null;
                    Double overValue = null;
                    Double underValue = null;

                    try {
                        totalPoints = Double.parseDouble(strTotalPoints);
                    } catch (Exception ex) {
                        request.setAttribute(Information.TOTAL+" "+p, "Invalid total points");
                        validated = false;
                    }
                    try {
                        overValue = Double.parseDouble(strOverLine);
                    } catch (Exception ex) {
                        request.setAttribute(Information.TOTAL+" "+p, "Invalid total line");
                        System.out.println("Invalid overValue "+strOverLine+" period "+p);
                        validated = false;
                    }
                    try {
                        underValue = Double.parseDouble(strUnderLine);
                    } catch (Exception ex) {
                        request.setAttribute(Information.TOTAL+" "+p, "Invalid total line");
                        validated = false;
                    }
                    if (validated) {
                        ParlayOdd over = new ParlayOdd();
                        over.setType(OddType.TOTAL_OVER);
                        over.setPoints(totalPoints);
                        over.setLine(overValue);
                        over.setStatus(Status.SELLING);
                        totalsOver[p] = over;
                        
                        ParlayOdd under = new ParlayOdd();
                        under.setType(OddType.TOTAL_UNDER);
                        under.setPoints(totalPoints);
                        under.setLine(underValue);
                        under.setStatus(Status.SELLING);
                        totalsUnder[p] = under;
                    }
                }
                
                String pSpreadPoints = Parameter.SPREAD+" "+p;
                String pSpreadTeam0Line = Parameter.SPREAD_TEAM0_LINE+" "+p;
                String pSpreadTeam1Line = Parameter.SPREAD_TEAM1_LINE+" "+p;

                String strSpreadPoints = request.getParameter(pSpreadPoints);
                String strSpreadTeam0Line = request.getParameter(pSpreadTeam0Line);
                String strSpreadTeam1Line = request.getParameter(pSpreadTeam1Line);

                formData.setSpreadPoints(p, strSpreadPoints);
                formData.setLineTeam0(p, strSpreadTeam0Line);
                formData.setLineTeam1(p, strSpreadTeam1Line);
                
                if (strSpreadPoints.length() == 0) strSpreadPoints = null;
                if (strSpreadTeam0Line.length() == 0) strSpreadTeam0Line = null;
                if (strSpreadTeam1Line.length() == 0) strSpreadTeam1Line = null;

                boolean addSpread = strSpreadPoints != null || strSpreadTeam0Line != null || strSpreadTeam1Line != null;
                if (addSpread) {
                    Double spreadPoints = null;
                    Double spreadTeam0Line = null;
                    Double spreadTeam1Line = null;

                    try {
                        spreadPoints = Double.parseDouble(strSpreadPoints);
                    } catch(Exception ex) {
                        request.setAttribute(Information.SPREAD+" "+p, "Invalid spread points");
                        validated = false;
                    }
                    try {
                        spreadTeam0Line = Double.parseDouble(strSpreadTeam0Line);
                    } catch(Exception ex) {
                        request.setAttribute(Information.SPREAD+" "+p, "Invalid spread line");
                        validated = false;
                    }
                    try {
                        spreadTeam1Line = Double.parseDouble(strSpreadTeam1Line);
                    } catch(Exception ex) {
                        request.setAttribute(Information.SPREAD+" "+p, "Invalid spread line");
                        validated = false;
                    }

                    if (validated) {
                        ParlayOdd spreadTeam0 = new ParlayOdd();
                        spreadTeam0.setLine(spreadTeam0Line);
                        spreadTeam0.setPoints(spreadPoints);
                        spreadTeam0.setType(OddType.SPREAD_TEAM0);
                        spreadTeam0.setStatus(Status.SELLING);
                        spreadsTeam0[p] = spreadTeam0;
                        
                        ParlayOdd spreadTeam1 = new ParlayOdd();
                        spreadTeam1.setLine(spreadTeam1Line);
                        spreadTeam1.setPoints(-spreadPoints);
                        spreadTeam1.setType(OddType.SPREAD_TEAM1);
                        spreadTeam1.setStatus(Status.SELLING);
                        spreadsTeam1[p] = spreadTeam1;
                    }
                }
            }
            
            String pDrawLine = Parameter.DRAW_LINE+" "+p;
            String strDrawLine = request.getParameter(pDrawLine);
            
            formData.setDrawLine(p, strDrawLine);
            
            if (strDrawLine.trim().length() == 0) strDrawLine = null;
            
            if (strDrawLine != null) {
                Double drawLine = null;
                try {
                    drawLine = Double.parseDouble(strDrawLine);
                } catch(Exception ex) {
                    request.setAttribute(Information.DRAW_LINE, "Invalid draw line");
                    validated = false;
                }

                if (validated) {
                    ParlayOdd draw = new ParlayOdd();
                    draw.setType(OddType.DRAW_LINE);
                    draw.setLine(drawLine);
                    draw.setStatus(Status.SELLING);
                    drawLines[p] = draw;
                }
            }
            
            int cntMoneyLine = 0;
            for (int t = 0; t < nTeams; t++) {
                oldMoneyLines[p][t] = parlayOddService.getOdd(teams[t], period, OddType.MONEY_LINE, Status.SELLING);
                    
                String pMoneyLine = Parameter.MONEY_LINE+" "+p+" "+t;
                
                String strMoneyLine = request.getParameter(pMoneyLine).trim();
                
                formData.setMoneyLine(p, t, strMoneyLine);
                
                if (strMoneyLine.length() == 0) strMoneyLine = null;
                
                if (strMoneyLine == null && cntMoneyLine > 0) {
                    request.setAttribute(Information.MONEY_LINE+" "+p+" "+t, "Incomplete moneylines");
                    validated = false;
                }
                if (strMoneyLine != null) {
                    Double line = null;
                    try {
                        line = Double.parseDouble(strMoneyLine);
                    } catch(Exception ex) {
                        request.setAttribute(Information.MONEY_LINE+" "+p+" "+t, "Invalid moneyline");
                        validated = false;
                    }
                    ParlayOdd moneyLine = new ParlayOdd();
                    moneyLine.setType(OddType.MONEY_LINE);
                    moneyLine.setLine(line);
                    moneyLine.setStatus(Status.SELLING);
                    moneyLines[p][t] = moneyLine;
                    cntMoneyLine++;
                }
            }
            if (validated) {
                periods[p] = period;
            }
        }

        if (!validated) {
            request.setAttribute(Attribute.MATCH_EVENT, formData);
            request.setAttribute(Attribute.TOURNAMENT_SERVICE, tournamentService);
            forward(AnalystController.getJSP(AnalystController.EDIT_MATCH));
            return;
        }

        try {
        matchEventService.edit(match);
        
        for (int t = 0; t < nTeams; t++) {
            teamService.edit(teams[t]);
        }
        for (int p = 0; p < nPeriods; p++) {
            if (oldDrawLines[p] != null) {
                oldDrawLines[p].setStatus(Status.PENDING);
                parlayOddService.edit(oldDrawLines[p]);
            }
            if (oldTotalsOver[p] != null) {
                oldTotalsOver[p].setStatus(Status.PENDING);
                parlayOddService.edit(oldTotalsOver[p]);
                
                oldTotalsUnder[p].setStatus(Status.PENDING);
                parlayOddService.edit(oldTotalsUnder[p]);
            }
            if (oldSpreadsTeam0[p] != null) {
                oldSpreadsTeam0[p].setStatus(Status.PENDING);
                parlayOddService.edit(oldSpreadsTeam0[p]);
                
                oldSpreadsTeam1[p].setStatus(Status.PENDING);
                parlayOddService.edit(oldSpreadsTeam1[p]);
            }
            if (oldMoneyLines[p][0] != null) {
                for (int t = 0; t < nTeams; t++) {
                    oldMoneyLines[p][t].setStatus(Status.PENDING);
                    parlayOddService.edit(oldMoneyLines[p][t]);
                }
            }
            boolean activePeriod = false;
            
            if (drawLines[p] != null) {
                drawLines[p].setPeriod(periods[p]);
                parlayOddService.create(drawLines[p]);
                activePeriod = true;
            }
            if (totalsOver[p] != null) {
                totalsOver[p].setPeriod(periods[p]);
                parlayOddService.create(totalsOver[p]);

                totalsUnder[p].setPeriod(periods[p]);
                parlayOddService.create(totalsUnder[p]);
                activePeriod = true;
            }
            if (spreadsTeam0[p] != null) {
                spreadsTeam0[p].setPeriod(periods[p]);
                spreadsTeam0[p].setTeam(teams[0]);
                parlayOddService.create(spreadsTeam0[p]);

                spreadsTeam1[p].setPeriod(periods[p]);
                spreadsTeam1[p].setTeam(teams[1]);
                parlayOddService.create(spreadsTeam1[p]);
                activePeriod = true;
            }
            if (moneyLines[p][0] != null) {
                for (int t = 0; t < nTeams; t++) {
                    moneyLines[p][t].setPeriod(periods[p]);
                    moneyLines[p][t].setTeam(teams[t]);
                    parlayOddService.create(moneyLines[p][t]);
                }
                activePeriod = true;
            }
            
            if (!activePeriod) {
                periods[p].setStatus(Status.INACTIVE);
            }
            matchPeriodService.edit(periods[p]);
        }
        } catch(EJBException ex) {
            request.setAttribute(Information.WARNING, "Opss! Something went wrong. Maybe all changes were not applied."
                    + "<br/>Please check.");
        }
        request.setAttribute(Attribute.MATCH_EVENT, match);
        request.setAttribute(Attribute.TEAM_SERVICE, teamService);
        request.setAttribute(Attribute.PARLAYODD_SERVICE, parlayOddService);
        request.setAttribute(Attribute.MATCH_PERIOD_SERVICE, matchPeriodService);
        forward(AnalystController.getJSP(AnalystController.MATCH_SUMMARY));
    }

    private void doSearch() {
        
        FinalUser sessionUser = auth.sessionUser(request);
        Agency agency = sessionUser.getAgency();
        
        String strSportId = request.getParameter(Parameter.SPORT);
        String strTournamentId = request.getParameter(Parameter.TOURNAMENT);
        String author = request.getParameter(Parameter.USERNAME);
        String strFrom = request.getParameter(Parameter.TIME_FROM);
        String strTo = request.getParameter(Parameter.TIME_TO);
        String strStatus = request.getParameter(Parameter.STATUS);
        
        String strRoleRequester = request.getParameter(Parameter.ROLE);
        Long roleRequester = null;
        try {
            roleRequester = Long.parseLong(strRoleRequester);
        } catch (Exception ex) {}
        if (roleRequester == null || !(new Role().someRole(auth.sessionRole(request), roleRequester))) {
            redirect(HomeController.URL);
            return;
        }
        
        boolean validated = true;
        
        if (author != null && author.trim().length() > 0) {
            try {
                validator.checkUsername(author);
            } catch (EJBException ex) {
                validated = false;
                request.setAttribute(Information.USERNAME, ex.getCausedByException().getMessage());
            }
        } else {
            author = null;
        }
        
        Integer sportId = null;
        if (strSportId != null && strSportId.length() > 0) {
            try {
                sportId = Integer.parseInt(strSportId);
            } catch(Exception ex) {
                request.setAttribute(Information.SPORT, "Invalid Sport ID "+strSportId);
                validated = false;
            }
        }
        Long tournamentId = null;
        if (strTournamentId != null && strTournamentId.length() > 0) {
            try {
                tournamentId = Long.parseLong(strTournamentId);
            } catch(Exception ex) {
                request.setAttribute(Information.TOURNAMENT, "Invalid tournament id "+strTournamentId);
                validated = false;
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar from = null;
        if (strFrom != null && strFrom.length() > 0) {
            from = Calendar.getInstance();
            try {
                from.setTime(formatter.parse(strFrom));
                from.set(Calendar.HOUR_OF_DAY, 0);
                from.set(Calendar.MINUTE, 0);
                from.set(Calendar.SECOND, 0);
            } catch (ParseException ex) {
                request.setAttribute(Information.TIME_FROM, "Invalid value for date "+strFrom);
                validated = false;
            }
        }
        Calendar to = null;
        if (strTo != null && strTo.length() > 0) {
            to = Calendar.getInstance();
            try {
                to.setTime(formatter.parse(strTo));
                to.set(Calendar.HOUR_OF_DAY, 23);
                to.set(Calendar.MINUTE, 59);
                to.set(Calendar.SECOND, 59);
            } catch (ParseException ex) {
                request.setAttribute(Information.TIME_TO, "Invalid value for date "+strTo);
                validated = false;
            }
        }
        
        try {
            validator.checkDateRange(from, to);
        } catch (EJBException ex) {
            validated = false;
            request.setAttribute(Information.TIME_FROM, ex.getCausedByException().getMessage());
        }
        
        Integer status = null;
        if (strStatus != null && strStatus.length() > 0) {
            try {
                status = Integer.parseInt(strStatus);
            } catch (Exception ex) {
                request.setAttribute(Information.TIME_FROM, "Invalid status value "+strStatus);
                validated = false;
            }
        }
        if (!validated) {
            request.setAttribute(Attribute.TIME_FROM, strFrom);
            request.setAttribute(Attribute.TIME_TO, strTo);
            if (roleRequester == Role.ANALYST) {
                forward(AnalystController.getJSP(AnalystController.SEARCH_MATCH));
            }
            else if (roleRequester == Role.MANAGER) {
                forward(ManagerController.getJSP(ManagerController.SEARCH_MATCH));
            }
            return;
        }
        
        List<MatchEvent> result;
        if (agency == null) {
            result = matchEventService.searchBy(null, sportId, tournamentId, author, from, to, status);
        } else {
            result = matchEventService.searchBy(agency.getId(), sportId, tournamentId, author, from, to, status);
        }
        request.setAttribute(Attribute.MATCH_EVENT, result);
        
        if (roleRequester == Role.ANALYST) {
            forward(AnalystController.getJSP(AnalystController.MATCH_SEARCH_RESULT));
        }
        else if (roleRequester == Role.MANAGER) {
            forward(ManagerController.getJSP(ManagerController.MATCH_SEARCH_RESULT));
        }
    }
    
    private void doCancel() {
        
        String strMatchId = request.getParameter(Parameter.MATCH_EVENT);
        
        Long matchId;
        try {
            matchId = Long.parseLong(strMatchId);
        } catch(Exception ex) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(AnalystController.getJSP(AnalystController.SEARCH_MATCH));
            return;
        }
        
        MatchEvent match = matchEventService.getMatchEvent(matchId);
        matchEventService.update(match);
        
        if (match == null) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(AnalystController.getJSP(AnalystController.SEARCH_MATCH));
            return;
        }
        else if ((match.getStatus() != Status.ACTIVE && match.getStatus() != Status.INACTIVE)
                    || (!match.getAuthor().equals(auth.sessionUser(request)))) {
            request.setAttribute(Information.ERROR, "Restricted operation");
            forward(AnalystController.getJSP(AnalystController.INDEX));
            return;
        }
        
        request.setAttribute(Attribute.MATCH_EVENT, match);
        request.setAttribute(Attribute.TEAM_SERVICE, teamService);
        request.setAttribute(Attribute.PARLAYODD_SERVICE, parlayOddService);
        request.setAttribute(Attribute.MATCH_PERIOD_SERVICE, matchPeriodService);
            
        List<ParlayOdd> odds = parlayOddService.getOdds(match);
        try {
            for (ParlayOdd odd : odds) {
                odd.setStatus(Status.CANCELLED);
                parlayOddService.edit(odd);
            }
            match.setStatus(Status.CANCELLED);
            matchEventService.edit(match);
        } catch(EJBException ex) {
            request.setAttribute(Information.ERROR, "Opss! something went wrong. Please try again.");
            forward(AnalystController.getJSP(AnalystController.MATCH_SUMMARY));
            return;
        }
        forward(AnalystController.getJSP(AnalystController.MATCH_SUMMARY));
    }
    
    private void doResult() {
        
        String strMatchId = request.getParameter(Parameter.MATCH_EVENT);
        
        Long matchId;
        try {
            matchId = Long.parseLong(strMatchId);
        } catch(Exception ex) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(AnalystController.getJSP(AnalystController.SEARCH_MATCH));
            return;
        }
        
        MatchEvent match = matchEventService.getMatchEvent(matchId);
        matchEventService.update(match);
        
        if (match == null) {
            request.setAttribute(Information.SEARCH_RESULT, "Match "+strMatchId+" not found");
            forward(AnalystController.getJSP(AnalystController.SEARCH_MATCH));
            return;
        }
        else if ((match.getStatus() != Status.PENDING_RESULT && match.getStatus() != Status.FINISHED) 
                    || (!match.getAuthor().equals(auth.sessionUser(request)))) {
            request.setAttribute(Information.ERROR, "Restricted operation");
            forward(AnalystController.getJSP(AnalystController.INDEX));
            return;
        }
        Tournament tournament = match.getTournament();
        Sport sport = tournament.getSport();
        int nPeriods = SportID.nPeriods[sport.getId()];
        int nTeams = SportID.nTeams[sport.getId()];
        MatchResultBean matchResult = new MatchResultBean(nPeriods, nTeams);
        matchResult.setPeriodNames(SportID.periodsName[sport.getId()]);
        matchResult.setMatchId(match.getId());
        matchResult.setSportName(sport.getName());
        matchResult.setMatchName(match.getName());
        matchResult.setTournamentName(match.getTournament().getName());
        
        switch(sport.getId()) {
            default:
                genericResult(matchResult, match); break;
        }
    }

    private void genericResult(MatchResultBean matchResult, MatchEvent match) {
        
        List<Team> teams = teamService.getTeams(match);
        for (int i = 0; i < teams.size(); i++) {
            matchResult.setTeamName(i, teams.get(i).getName());
        }
        
        int nPeriods = matchResult.getnPeriods();
        int nTeams = matchResult.getnTeams();
        
        double[][] result = new double[nTeams][nPeriods];
        boolean validated = true;
        
        for (int i = 0; i < nPeriods; i++) {
            for (int j = 0; j < nTeams; j++) {
                String pScore = Parameter.SCORE+" "+i+" "+j;
                String strScore = request.getParameter(pScore).trim();
                
                matchResult.setScore(i, j, strScore);
                
                if (strScore.length() == 0) {
                    request.setAttribute(Information.SCORE, "Some scores are missing");
                    validated = false;
                    strScore = null;
                }
                if (strScore != null) {
                    Double s = null;
                    try {
                        s = Double.parseDouble(strScore);
                    } catch(Exception ex) {
                        request.setAttribute(Information.SCORE, "Invalid score "+strScore);
                        validated = false;
                    }
                    if (s != null && s < 0) {
                        request.setAttribute(Information.SCORE, "Score cannot be negative "+s);
                        validated = false;
                    }
                    if (validated) {
                        result[j][i] = s;
                    }
                }
            }
        }
        if (!validated) {
            request.setAttribute(Attribute.MATCH_RESULT, matchResult);
            forward(AnalystController.getJSP(AnalystController.MATCH_RESULT));
            return;
        }
        matchEventService.updateMatchResult(match, teams, matchPeriodService.getPeriods(match), result);
        
        request.setAttribute(Attribute.MATCH_EVENT, match);
        request.setAttribute(Attribute.MATCH_PERIOD_SERVICE, matchPeriodService);
        request.setAttribute(Attribute.TEAM_SERVICE, teamService);
        request.setAttribute(Attribute.MATCH_PERIOD_SERVICE, matchPeriodService);
        request.setAttribute(Attribute.PARLAYODD_SERVICE, parlayOddService);
        forward(AnalystController.getJSP(AnalystController.MATCH_SUMMARY));
    }
}
