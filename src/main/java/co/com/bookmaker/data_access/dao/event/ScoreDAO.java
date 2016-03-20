/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.data_access.dao.event;

import co.com.bookmaker.data_access.dao.GenericDAO;
import co.com.bookmaker.data_access.entity.event.Score;

/**
 *
 * @author eduarc
 */
public class ScoreDAO extends GenericDAO<Score> {

    public ScoreDAO() {
        super(Score.class);
    }
    
}
