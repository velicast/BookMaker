/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.event;

import co.com.bookmaker.data_access.dao.event.TeamDAO;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.Team;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author eduarc
 */
public class TeamService {
    
    private final TeamDAO teamDAO;
    
    public TeamService() {
        
        teamDAO = new TeamDAO();
    }
    
    public Team getTeam(MatchEvent m, Integer number) {
    
        return teamDAO.find(new String[] {"match.id", "number"},
                            new Object[] {m.getId(), number});
    }
    
    public List<Team> getTeams(MatchEvent m) {
        
        if (m == null) {
            return null;
        }
        List<Team> teams = teamDAO.findAll(new String[] {"match.id"}, 
                                           new Object[] {m.getId()});
        teams.sort(new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });
        return teams;
    }
    
    public void create(Team team) {
        teamDAO.create(team);
    }
    
    public void edit(Team team) {
        teamDAO.edit(team);
    }
    
    public void remove(Team team) {
        teamDAO.remove(team);
    }
}
