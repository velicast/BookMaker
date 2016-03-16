/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.event;

import co.com.bookmaker.data_access.dao.event.ScoreDAO;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.MatchEventPeriod;
import co.com.bookmaker.data_access.entity.event.Score;
import co.com.bookmaker.data_access.entity.event.Team;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author eduarc
 */
@Stateless
public class ScoreService {
    
    @EJB
    private ScoreDAO scoreDAO;
    
    public void edit(Score score) {
        scoreDAO.edit(score);
    }
    
    public void create(Score score) {
        scoreDAO.create(score);
    }
    
    public List<Score> getScores(MatchEvent m) {
        return scoreDAO.findAll(new String[] {"period.match.id", "team.match.id"},
                                new Object[] {m.getId(), m.getId()});
    }
    
    public Score getScore(Team t, MatchEventPeriod p) {
        
        return scoreDAO.find(new String[] {"team.id", "period.id"}, 
                             new Object[] {t.getId(), p.getId()});
    }
}
