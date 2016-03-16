/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.parlay;

import co.com.bookmaker.data_access.dao.parlay.ParlayOddDAO;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.MatchEventPeriod;
import co.com.bookmaker.data_access.entity.event.Team;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import co.com.bookmaker.data_access.entity.parlay.ParlayOdd;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author eduarc
 */
@Stateless
public class ParlayOddService {

    @EJB
    private ParlayOddDAO parlayOddDAO;
    
    public ParlayOdd getOdd(Long id) {
        return parlayOddDAO.find(id);
    }
    
    public List<ParlayOdd> getOdds(Parlay parlay) {
        return null;
    }

    public void create(ParlayOdd odd) {
        parlayOddDAO.create(odd);
    }
    
    public void edit(ParlayOdd odd) {
        parlayOddDAO.edit(odd);
    }
    
    public void remove(ParlayOdd odd) {
        parlayOddDAO.edit(odd);
    }
    
    public ParlayOdd getOddByPeriod(MatchEventPeriod p, Integer type, Integer status) {
        
        return parlayOddDAO.find(new String[] {"period.id", "type", "status"}, 
                                 new Object[] {p.getId(), type, status});
    }
    
    public ParlayOdd getOdd(MatchEventPeriod p, Integer type) {
        
        return parlayOddDAO.find(new String[] {"period.id", "type"}, 
                                 new Object[] {p.getId(), type});
    }
    
    public ParlayOdd getOdd(Team t, MatchEventPeriod p, Integer type, Integer status) {
        
        return parlayOddDAO.find(new String[] {"team.id", "period.id", "type", "status"}, 
                                 new Object[] {t.getId(), p.getId(), type, status});
    }
    
    public ParlayOdd getOddByTeam(Team t, MatchEventPeriod p, Integer type) {
        
        return parlayOddDAO.find(new String[] {"team.id", "period.id", "type"}, 
                                 new Object[] {t.getId(), p.getId(), type});
    }
    
    public List<ParlayOdd> getOdds(MatchEvent match) {
        
        return parlayOddDAO.findAll(new String[] {"period.match.id"}, 
                             new Object[]{match.getId()});
    }
    
    public List<ParlayOdd> getOdds(MatchEvent match, Integer status) {
        
        return parlayOddDAO.findAll(new String[] {"period.match.id", "status"}, 
                             new Object[]{match.getId(), status});
    }
}
