/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tgetEntityManager()plate file, choose Tools | TgetEntityManager()plates
 * and open the tgetEntityManager()plate in the editor.
 */
package co.com.bookmaker.data_access.dao.event;

import co.com.bookmaker.data_access.dao.GenericDAO;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.event.Sport;
import co.com.bookmaker.data_access.entity.event.Tournament;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import co.com.bookmaker.util.type.Status;

/**
 *
 * @author eduarc
 */
@Stateless
public class TournamentDAO extends GenericDAO<Tournament> {

    public TournamentDAO() {
        super(Tournament.class);
    }
    
    public List<Tournament> findOnlyAgency(Agency agency) {
        
        Query q = getEntityManager().createQuery("SELECT t FROM Tournament t WHERE "
                + "t.author.agency is not NULL AND t.author.agency.id = :agencyId")
                .setParameter("agencyId", agency.getId());
        return q.getResultList();
    }
    
    public List<Tournament> findOnlyAgency(Agency agency, Long sportId, Integer status) {
        
        Query q = getEntityManager().createQuery("SELECT t FROM Tournament t WHERE "
                + "t.author.agency is not NULL AND t.author.agency.id = :agencyId AND "
                + "t.sport.id = :sportId AND t.status = :status")
                .setParameter("agencyId", agency.getId())
                .setParameter("sportId", sportId)
                .setParameter("status", status);
        return q.getResultList();
    }
        
    public List<Tournament> findByAgency(Agency agency) {
        
        Query q = getEntityManager().createQuery("SELECT t FROM Tournament t WHERE "
                + "(t.author.status = :active AND t.author.agency is NULL AND :acceptGlobal = TRUE) OR "
                + "(t.author.agency is not NULL AND t.author.agency.id = :agencyId)")
                .setParameter("active", Status.ACTIVE)
                .setParameter("acceptGlobal", agency.getAcceptGlobalOdds())
                .setParameter("agencyId", agency.getId());
        return q.getResultList();
    }
    
    public List<Tournament> findByAgency(Agency agency, Sport sport, Integer status) {
        
        Query q = getEntityManager().createQuery("SELECT t FROM Tournament t WHERE "
                + "(t.sport.id = :sportId AND t.status = :status) AND "
                + "((t.author.status = :active AND t.author.agency is NULL AND :acceptGlobal = TRUE) OR "
                + "(t.author.agency is not NULL AND t.author.agency.id = :agencyId))")
                .setParameter("sportId", sport.getId())
                .setParameter("status", status)
                .setParameter("active", Status.ACTIVE)
                .setParameter("acceptGlobal", agency.getAcceptGlobalOdds())
                .setParameter("agencyId", agency.getId());
        return q.getResultList();
    }

    public List<Tournament> findInHeadquarters(Long sportId, Integer status) {
        
        Query q = getEntityManager().createQuery("SELECT t FROM Tournament t WHERE "
                + "t.author.status = :active AND t.author.agency is NULL AND "
                + "t.status = :status AND t.sport.id = :sportId")
                .setParameter("active", Status.ACTIVE)
                .setParameter("status", status)
                .setParameter("sportId", sportId);
        return q.getResultList();
    }
    
    public List<Tournament> findInHeadquarters() {
        
        Query q = getEntityManager().createQuery("SELECT t FROM Tournament t WHERE "
                + "t.author.status = :active AND t.author.agency is NULL")
                .setParameter("active", Status.ACTIVE);
        return q.getResultList();
    }
}
