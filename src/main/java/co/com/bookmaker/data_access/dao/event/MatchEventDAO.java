/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.data_access.dao.event;

import co.com.bookmaker.data_access.dao.GenericDAO;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.Tournament;
import java.util.List;
import javax.persistence.Query;
import co.com.bookmaker.util.type.Status;

/**
 *
 * @author eduarc
 */
public class MatchEventDAO extends GenericDAO<MatchEvent> {
    
    public MatchEventDAO() {
        super(MatchEvent.class);
    }
    
    public Integer countMatches(Tournament tournament, Integer status) {
        
        Query q = getEntityManager().createQuery("SELECT COUNT(m.id) FROM MatchEvent m WHERE "
                + "m.tournament.id = :tId AND m.status = :status")
                .setParameter("tId", tournament.getId())
                .setParameter("status", status);
        return (Integer) ((Long)q.getSingleResult()).intValue();
    }
    
    public Integer countMatches(Tournament tournament) {
        
        Query q = getEntityManager().createQuery("SELECT COUNT(m.id) FROM MatchEvent m WHERE "
                + "m.tournament.id = :tId")
                .setParameter("tId", tournament.getId());
        return (Integer) q.getSingleResult();
    }

    public List<MatchEvent> findOnlyByAgency(Agency agency) {
        
        Query q = getEntityManager().createQuery("SELECT m FROM MatchEvent m WHERE "
                + "m.author.agency is not NULL AND m.author.agency.id = :agencyId")
                .setParameter("agencyId", agency.getId());
        return q.getResultList();
    }
    
    public List<MatchEvent> findInHeadquarters() {
        
        Query q = getEntityManager().createQuery("SELECT m FROM MatchEvent m WHERE "
                + "m.author.status = :active AND m.author.agency is NULL")
                .setParameter("active", Status.ACTIVE);
        return q.getResultList();
    }
}
