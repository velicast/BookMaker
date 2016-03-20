/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.event;

import co.com.bookmaker.data_access.dao.event.MatchPeriodDAO;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.MatchEventPeriod;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author eduarc
 */
public class MatchEventPeriodService {

    private final MatchPeriodDAO matchPeriodDAO;

    public MatchEventPeriodService() {
        
        matchPeriodDAO = new MatchPeriodDAO();
    }
    
    public void create(MatchEventPeriod period) {
        matchPeriodDAO.create(period);
    }
    
    public void edit(MatchEventPeriod period) {
        matchPeriodDAO.edit(period);
    }
    
    public void remove(MatchEventPeriod period) {
        matchPeriodDAO.remove(period);
    }
    
    public List<MatchEventPeriod> getPeriods(MatchEvent match, Integer status) {
        
        if (match == null) {
            return null;
        }
        List<MatchEventPeriod> periods = matchPeriodDAO.findAll(new String[] {"match.id", "status"}, 
                                                           new Object[] {match.getId(), status});
        periods.sort(new Comparator<MatchEventPeriod>() {
            @Override
            public int compare(MatchEventPeriod o1, MatchEventPeriod o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });
        return periods;
    }
    
    public List<MatchEventPeriod> getPeriods(MatchEvent m) {
        
        if (m == null) {
            return null;
        }
        List<MatchEventPeriod> periods = matchPeriodDAO.findAll(new String[] {"match.id"},
                                                                new Object[] {m.getId()});
        periods.sort(new Comparator<MatchEventPeriod>() {
            @Override
            public int compare(MatchEventPeriod o1, MatchEventPeriod o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });
        return periods;
    }
}
