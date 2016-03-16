/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tgetEntityManager()plate file, choose Tools | TgetEntityManager()plates
 * and open the tgetEntityManager()plate in the editor.
 */
package co.com.bookmaker.data_access.dao.event;

import co.com.bookmaker.data_access.dao.GenericDAO;
import co.com.bookmaker.data_access.entity.event.MatchEventPeriod;
import javax.ejb.Stateless;

/**
 *
 * @author eduarc
 */
@Stateless
public class MatchPeriodDAO extends GenericDAO<MatchEventPeriod> {

    public MatchPeriodDAO() {
        super(MatchEventPeriod.class);
    }
        
}
