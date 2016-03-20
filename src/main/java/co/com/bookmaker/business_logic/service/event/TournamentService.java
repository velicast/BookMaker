/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.event;

import co.com.bookmaker.data_access.dao.AgencyDAO;
import co.com.bookmaker.data_access.dao.event.MatchEventDAO;
import co.com.bookmaker.data_access.dao.event.TournamentDAO;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.event.Sport;
import co.com.bookmaker.data_access.entity.event.Tournament;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import co.com.bookmaker.util.type.Pair;

/**
 *
 * @author eduarc
 */
public class TournamentService {

    private final TournamentDAO tournamentDAO;
    private final MatchEventDAO matchEventDAO;
    private final AgencyDAO agencyDAO;
    
    public TournamentService() {
        
        tournamentDAO = new TournamentDAO();
        matchEventDAO = new MatchEventDAO();
        agencyDAO = new AgencyDAO();
    }
    
    public void create(Tournament tournament) {
        tournamentDAO.create(tournament);
    }
    
    public void edit(Tournament tournament) {
        tournamentDAO.edit(tournament);
    }
    
    public Tournament getTournament(Long id) {
        return tournamentDAO.find(id);
    }

    public List<Tournament> getTournaments(Integer sportID, Integer status) {
        
        if (sportID == null) {
            return null;
        }
        return tournamentDAO.findAll(new String[] {"sport.id", "status"}, 
                                     new Object[] {sportID, status});
    }
    
    public List<Tournament> getClientTournaments(Agency agency, Sport sport, Integer status) {

        if (agency == null) {
            return null;
        }
        return tournamentDAO.findByAgency(agency, sport, status);
    }
    
    public List<Tournament> getTournaments(Long agencyId, Long sportId, Integer status) {

        if (agencyId == null) {
            return tournamentDAO.findInHeadquarters(sportId, status);
        }
        Agency agency = agencyDAO.find(agencyId);
        if (agency == null) {
            return null;
        }
        return tournamentDAO.findOnlyAgency(agency, sportId, status);
    }
    
        // Usado en el form de busqueda. Un usuario solo puede ver aquellas que
        // fueron creadas en su misma agencia, o cuartel
    public List<Pair<Tournament, Long>> searchBy(Long agencyId, Integer sportId, Integer status, boolean activeMatches) {

        List<Tournament> pre;
        if (agencyId == null) {
            pre = tournamentDAO.findInHeadquarters();
        } else {
            Agency agency = agencyDAO.find(agencyId);
            if (agency == null) {
                return null;
            }
            pre = tournamentDAO.findOnlyAgency(agency);
        }
        List<Pair<Tournament, Long>> result = new ArrayList();
        for (Tournament t : pre) {
            boolean add = true;
            if (sportId != null) {
                add &= Objects.equals(t.getSport().getId(), sportId);
            }
            if (status != null) {
                add &= Objects.equals(t.getStatus(), status);
            }
            Long active = matchEventDAO.countActiveMatches(t);
            if (activeMatches) {
                add &= active != 0;
            }
            if (add) {
                result.add(new Pair(t, active));
            }
        }
        return result;
    }
}
