/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller.event;

import co.com.bookmaker.business_logic.controller.AnalystController;
import co.com.bookmaker.business_logic.controller.GenericController;
import co.com.bookmaker.business_logic.service.ParameterValidator;
import co.com.bookmaker.business_logic.service.event.SportService;
import co.com.bookmaker.business_logic.service.event.TournamentService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.Sport;
import co.com.bookmaker.data_access.entity.event.Tournament;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.Pair;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.Status;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "TournamentController", urlPatterns = {"/"+TournamentController.URL})
public class TournamentController extends GenericController {

    public static final String URL = "tournament";

    public static final String NEW = "new";
    public static final String EDIT = "edit";
    public static final String SEARCH = "search";
    public static final String SUMMARY = "summary";
    public static final String SEARCH_RESULT = "search_result";
    public static final String LIST = "list";
    
    private AuthenticationService auth;
    private SportService sportService;
    private TournamentService tournamentService;
    private ParameterValidator validator;
    
    @Override
    public void init() {
        
        auth = new AuthenticationService();
        validator = new ParameterValidator();
        sportService = new SportService();
        tournamentService = new TournamentService();
        
        allowDO(NEW, Role.ANALYST);
        allowDO(EDIT, Role.ANALYST);
        allowDO(SEARCH, Role.ANALYST|Role.MANAGER);
        allowDO(LIST, Role.ANALYST|Role.MANAGER);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/tournament/"+resource+".jsp";
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
            case LIST:
                doList(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void doNew() {
        
        String strSportId = request.getParameter(Parameter.SPORT);
        String name = request.getParameter(Parameter.NAME);
        String strStatus = request.getParameter(Parameter.STATUS);
        
        // INICIA VALIDACION
        boolean validated = true;
        
        try {
            validator.checkTournamentName(name);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.NAME, ex.getMessage());
        }
        
        Integer status = null;
        try {
            status = Integer.parseInt(strStatus);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.STATUS, "Estado inválido");
        }
        
        Integer sportId = null;
        try {
            sportId = Integer.parseInt(strSportId);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.SPORT, "ID de deporte inválido");
        }
        // FIN VALIDACION
        
        Sport sport = sportService.getSport(sportId, Status.ACTIVE);
        if (sport == null) {
            validated = false;
            request.setAttribute(Information.SPORT, "Deporte "+sportId+" no encontrado");
        }
        
        Tournament tournament = new Tournament();
        tournament.setSport(sport);
        tournament.setName(name);
        tournament.setStatus(status);
        tournament.setAuthor(auth.sessionUser(request));
        
        request.setAttribute(Attribute.TOURNAMENT, tournament);
        
        if (!validated) {
            List<Sport> sports = sportService.getSports(Status.ACTIVE);
            request.setAttribute(Attribute.SPORTS, sports);
            forward(AnalystController.getJSP(AnalystController.NEW_TOURNAMENT));
            return;
        }
        try {
            tournamentService.create(tournament);
        } catch(Exception ex) {
            request.setAttribute(Information.ERROR, "Opss! Algo estuvo mal. Por favor intente de nuevo.");
            return;
        }
        forward(AnalystController.getJSP(AnalystController.TOURNAMENT_SUMMARY));
    }

    private void doEdit() {
        
        String strTournamentId = request.getParameter(Parameter.TOURNAMENT);
        String strSportId = request.getParameter(Parameter.SPORT);
        String name = request.getParameter(Parameter.NAME);
        String strStatus = request.getParameter(Parameter.STATUS);
        
        // INICIA VALIDACION
        boolean validated = true;
        
        try {
            validator.checkTournamentName(name);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.NAME, ex.getMessage());
        }
        
        Integer status = null;
        try {
            status = Integer.parseInt(strStatus);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.STATUS, "Estado inválido");
        }
        
        Integer sportId = null;
        try {
            sportId = Integer.parseInt(strSportId);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.SPORT, "ID de deporte inválido");
        }
        
        Long tournamentId = null;
        try {
            tournamentId = Long.parseLong(strTournamentId);
        } catch (Exception ex) {
            validated = false;
            request.setAttribute(Information.NAME, "ID de torneo inválido");
        }
        
        Sport sport = sportService.getSport(sportId, Status.ACTIVE);
        if (sport == null) {
            request.setAttribute(Information.SPORT, "Deporte "+sportId+" no encontrado");
            forward(AnalystController.getJSP(AnalystController.SEARCH_TOURNAMENT));
            return;
        }
        
        Tournament tournament = tournamentService.getTournament(tournamentId);
        
        if (tournament == null) {
            request.setAttribute(Information.NAME, "Torneo "+sportId+" no encontrado");
            forward(AnalystController.getJSP(AnalystController.SEARCH_TOURNAMENT));
            return;
        }
        // FIN VALIDACION
        
        tournament.setSport(sport);
        tournament.setName(name);
        tournament.setStatus(status);
        
        request.setAttribute(Attribute.TOURNAMENT, tournament);
        
        if (!validated) {
            List<Sport> sports = sportService.getSports(Status.ACTIVE);
            request.setAttribute(Attribute.SPORTS, sports);
            forward(AnalystController.getJSP(AnalystController.EDIT_TOURNAMENT));
            return;
        }
        try {
            tournamentService.edit(tournament);
        } catch(Exception ex) {
            request.setAttribute(Information.ERROR, "Opss! Algo estuvo mal. Por favor intente de nuevo.");
            forward(AnalystController.getJSP(AnalystController.EDIT_TOURNAMENT));
            return;
        }
        forward(AnalystController.getJSP(AnalystController.TOURNAMENT_SUMMARY));
    }

    private void doSearch() {
        
        String strSportId = request.getParameter(Parameter.SPORT);
        String strStatus = request.getParameter(Parameter.STATUS);
        String activeMatches = request.getParameter(Parameter.ACTIVE_MATCHES);
        
        // INICIA VALIDACION
        boolean validated = true;
        
        Integer status = null;
        if (strStatus != null && strStatus.length() > 0) {
            try {
                status = Integer.parseInt(strStatus);
            } catch (Exception ex) {
                validated = false;
                request.setAttribute(Information.STATUS, "Estado inválido: "+strStatus);
            }
        }
        Integer sportId = null;
        if (strSportId != null && strSportId.length() > 0) {
            try {
                sportId = Integer.parseInt(strSportId);
            } catch (Exception ex) {
                validated = false;
                request.setAttribute(Information.SPORT, "ID de deporte inválido: "+strSportId);
            }
        }
        if (!validated) {
            forward(AnalystController.getJSP(AnalystController.SEARCH_TOURNAMENT));
            return;
        }
        List<Pair<Tournament, Integer>> result;
                
        FinalUser user = auth.sessionUser(request);
        Agency agency = user.getAgency();
        if (agency == null) {
            result = tournamentService.searchBy(null, sportId, status, activeMatches != null);
        } else {
            result = tournamentService.searchBy(agency.getId(), sportId, status, activeMatches != null);
        }
        request.setAttribute(Attribute.TOURNAMENTS, result);
        forward(AnalystController.getJSP(AnalystController.TOURNAMENT_SEARCH_RESULT));
    }

    private void doList() {
        
        String strSportId = request.getParameter(Parameter.SPORT);
        if (strSportId == null) {
            return;
        }
        List<Tournament> list = null;
        if (strSportId.length() == 0) {
            list = new ArrayList();
        }
        Long sportId = null;
        try {
            sportId = Long.parseLong(strSportId);
        } catch (NumberFormatException ex) {
            list = new ArrayList();
        }
        if (list == null) {
            FinalUser user = auth.sessionUser(request);
            Agency agency = user.getAgency();
            if (agency == null) {
                list = tournamentService.getTournaments(null, sportId, Status.ACTIVE);
            } else {
                list = tournamentService.getTournaments(agency.getId(), sportId, Status.ACTIVE);
            }
        }
        request.setAttribute(Attribute.TOURNAMENTS, list);
        forward(getJSP(LIST));
    }
}
