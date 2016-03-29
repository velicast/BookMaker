/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tgetEntityManager()plate file, choose Tools | TgetEntityManager()plates
 * and open the tgetEntityManager()plate in the editor.
 */
package co.com.bookmaker.data_access.dao.event;

import co.com.bookmaker.data_access.dao.GenericDAO;
import co.com.bookmaker.data_access.entity.event.Sport;

/**
 *
 * @author eduarc
 */
public class SportDAO extends GenericDAO<Sport> {

    public SportDAO() {
        super(Sport.class);
    }
}
