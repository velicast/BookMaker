/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.event;

import co.com.bookmaker.data_access.dao.event.SportDAO;
import co.com.bookmaker.data_access.entity.event.Sport;
import java.util.Comparator;
import java.util.List;
import co.com.bookmaker.util.type.Status;

/**
 *
 * @author eduarc
 */
public class SportService {

    private final SportDAO sportDAO;
    
    public SportService() {
        sportDAO = new SportDAO();
    }
    
    public Sport getSport(Integer id, Integer status) {
        
        return sportDAO.find(new String[] {"id", "status"},
                             new Object[] { id,   status});
    }
    
    public List<Sport> getSports(Integer status) {
        
        List<Sport> sports = sportDAO.findAll(new String[] {"status"},
                                new Object[] {Status.ACTIVE});
        sports.sort(new Comparator<Sport>() {
            @Override
            public int compare(Sport o1, Sport o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        return sports;
    }
}
