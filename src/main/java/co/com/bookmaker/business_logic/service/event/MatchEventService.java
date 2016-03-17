/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.event;

import co.com.bookmaker.business_logic.service.parlay.ParlayOddService;
import co.com.bookmaker.business_logic.service.parlay.ParlayService;
import co.com.bookmaker.data_access.dao.AgencyDAO;
import co.com.bookmaker.data_access.dao.event.MatchEventDAO;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.MatchEventPeriod;
import co.com.bookmaker.data_access.entity.event.Score;
import co.com.bookmaker.data_access.entity.event.Team;
import co.com.bookmaker.data_access.entity.event.Tournament;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import co.com.bookmaker.data_access.entity.parlay.ParlayOdd;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import co.com.bookmaker.util.type.Status;
import co.com.bookmaker.util.type.OddType;

/**
 *
 * @author eduarc
 */
@Stateless
public class MatchEventService {

    @EJB
    private MatchEventDAO matchEventDAO;
    @EJB
    private AgencyDAO agencyDAO;
    @EJB
    private ParlayOddService parlayOddService;
    @EJB
    private ParlayService parlayService;
    @EJB
    private ScoreService scoreService;
    
    public void create(MatchEvent match) {
        matchEventDAO.create(match);
    }
    
    public void edit(MatchEvent match) {
        matchEventDAO.edit(match);
    }

    public void remove(MatchEvent match) {
        matchEventDAO.remove(match);
    }
    
    public MatchEvent getMatchEvent(Long id) {
        return matchEventDAO.findRefresh(id);
    }
    
    public List<MatchEvent> getMatchesByTournament(Tournament tournament, Integer status) {
        
        if (tournament == null) {
            return null;
        }
        
        List<MatchEvent> pre = matchEventDAO.findAll(new String[] {"tournament.id", "status"}, 
                                                         new Object[] {tournament.getId(), status});
        List<MatchEvent> matches = new ArrayList();
        for (MatchEvent m : pre) {
            update(m);
            if (m.getStatus().equals(status)) {
                matches.add(m);
            }
        }
        matches.sort(new Comparator<MatchEvent>() {
            @Override
            public int compare(MatchEvent o1, MatchEvent o2) {
                if (o1.getTournament().getSport().getId() > o2.getTournament().getSport().getId()) return 1;
                if (o1.getTournament().getSport().getId() < o2.getTournament().getSport().getId()) return -1;
                int cmpName = o1.getTournament().getName().compareTo(o2.getTournament().getName());
                if (cmpName > 0) return 1;
                if (cmpName < 0) return -1;
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        return matches;
    }
    
    public List<MatchEvent> searchAllBy(Integer sportId, Long tournamentId, String author, Calendar from, Calendar to, Integer status) {
        
        List<MatchEvent> pre = matchEventDAO.findAll();
        return filterSearch(pre, sportId, tournamentId, author, from, to, status);
    }
    
    public List<MatchEvent> searchBy(Long agencyId, Integer sportId, Long tournamentId, String author, Calendar from, Calendar to, Integer status) {
        
        List<MatchEvent> pre;
        if (agencyId == null) {
            pre = matchEventDAO.findInHeadquarters();
        } else {
            Agency agency = agencyDAO.find(agencyId);
            if (agency == null) {
                return null;
            }
            pre = matchEventDAO.findOnlyByAgency(agency);
        }
        return filterSearch(pre, sportId, tournamentId, author, from, to, status);
    }

    private List<MatchEvent> filterSearch(List<MatchEvent> pre, Integer sportId, Long tournamentId, String author, Calendar from, Calendar to, Integer status) {
        
        List<MatchEvent> matches = new ArrayList();
        for (MatchEvent m : pre) {
            update(m);
            
            boolean add = true;
            if (sportId != null) {
                add &= Objects.equals(m.getTournament().getSport().getId(), sportId);
            }
            if (tournamentId != null) {
                add &= Objects.equals(m.getTournament().getId(), tournamentId);
            }
            if (author != null) {
                add &= m.getAuthor().getUsername().equals(author);
            }
            if (status != null) {
                add &= m.getStatus().equals(status);
            }
            Calendar mDate = m.getStartDate();
            if (from != null) {
                add &= mDate.compareTo(from) >= 0;
            }
            if (to != null) {
                add &= mDate.compareTo(to) <= 0;
            }
            if (add) {
                matches.add(m);
            }
        }
        matches.sort(new Comparator<MatchEvent>() {
            @Override
            public int compare(MatchEvent o1, MatchEvent o2) {
                if (o1.getTournament().getSport().getId() > o2.getTournament().getSport().getId()) return 1;
                if (o1.getTournament().getSport().getId() < o2.getTournament().getSport().getId()) return -1;
                int cmpName = o1.getTournament().getName().compareTo(o2.getTournament().getName());
                if (cmpName > 0) return 1;
                if (cmpName < 0) return -1;
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        return matches;
    }
    
    public void update(MatchEvent match) {
        
        if (match == null) {
            return;
        }
        if (match.getStatus() == Status.ACTIVE || match.getStatus() == Status.INACTIVE) {
            Calendar now = Calendar.getInstance();
            if (match.getStartDate().compareTo(now) <= 0) {
                match.setStatus(Status.PENDING_RESULT);
                matchEventDAO.edit(match);
                for (ParlayOdd p : parlayOddService.getOdds(match, Status.SELLING)) {
                    p.setStatus(Status.PENDING);
                    parlayOddService.edit(p);
                }
            }
        }
    }
    
    public List<MatchEvent> getMatches(FinalUser author, Integer status) {
        
        List<MatchEvent> matches = matchEventDAO.findAll(new String[] {"author.id", "status"}, 
                                                         new Object[] {author.getId(), status});
        matches.sort(new Comparator<MatchEvent>() {
            @Override
            public int compare(MatchEvent o1, MatchEvent o2) {
                if (o1.getTournament().getSport().getId() > o2.getTournament().getSport().getId()) return 1;
                if (o1.getTournament().getSport().getId() < o2.getTournament().getSport().getId()) return -1;
                int cmpName = o1.getTournament().getName().compareTo(o2.getTournament().getName());
                if (cmpName > 0) return 1;
                if (cmpName < 0) return -1;
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        return matches;
    }

    public List<MatchEvent> getMatches(Integer status) {
        
        List<MatchEvent> matches = matchEventDAO.findAll(new String[] {"status"}, 
                                                         new Object[] {status});
        matches.sort(new Comparator<MatchEvent>() {
            @Override
            public int compare(MatchEvent o1, MatchEvent o2) {
                if (o1.getTournament().getSport().getId() > o2.getTournament().getSport().getId()) return 1;
                if (o1.getTournament().getSport().getId() < o2.getTournament().getSport().getId()) return -1;
                int cmpName = o1.getTournament().getName().compareTo(o2.getTournament().getName());
                if (cmpName > 0) return 1;
                if (cmpName < 0) return -1;
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        return matches;
    }
    
    public void updateMatchResult(MatchEvent m, List<Team> teams, List<MatchEventPeriod> periods, double[][] result) {
        
        int nTeams = result.length;
        int nPeriods = result[0].length;
        
        Score[][] score_table = new Score[nTeams][nPeriods];
        List<Score> scores = scoreService.getScores(m);
        for (Score s : scores) {
            score_table[s.getTeam().getNumber()][s.getPeriod().getNumber()] = s;
        }
        
        List<Integer> winner[] = new List[nPeriods];
        int total[] = new int[nPeriods];
        
        for (int i = 0; i < nPeriods; i++) {
            double maxi = Double.MIN_VALUE;
            for (int j = 0; j < nTeams; j++) {
                total[i] += result[j][i];
                if (result[j][i] > maxi) {
                    maxi = result[j][i];
                }
                if (score_table[j][i] == null) {
                    score_table[j][i] = new Score();
                    score_table[j][i].setTeam(teams.get(j));
                    score_table[j][i].setPeriod(periods.get(i));
                    score_table[j][i].setVal(result[j][i]);
                    scoreService.create(score_table[j][i]);
                } else {
                    score_table[j][i].setVal(result[j][i]);
                    scoreService.edit(score_table[j][i]);
                }
            }
            winner[i] = new ArrayList();
            for (int j = 0; j < nTeams; j++) {
                if (result[j][i] == maxi) {
                    winner[i].add(j);
                }
            }
        }
        
        List<ParlayOdd> odds = parlayOddService.getOdds(m);
        List<Parlay> parlays = new ArrayList();
        
        for (ParlayOdd odd : odds) {
            Integer type = odd.getType();
            MatchEventPeriod period = odd.getPeriod();
            int p = period.getNumber();
            
            if (type == OddType.DRAW_LINE) {
                odd.setStatus(Status.LOSE);
                if (winner[p].size() == nTeams) {
                    odd.setStatus(Status.WIN);
                }
            }
            else if (type == OddType.MONEY_LINE) {
                odd.setStatus(Status.LOSE);
                if (winner[p].size() != nTeams && winner[p].contains(odd.getTeam().getNumber())) {
                    odd.setStatus(Status.WIN);
                }
            }
            else if (type == OddType.TOTAL_OVER) {
                odd.setStatus(Status.LOSE);
                if (total[p] > odd.getPoints()) {
                    odd.setStatus(Status.WIN);
                }
                else if (total[p] == odd.getPoints()) {
                    odd.setStatus(Status.PUSH);
                }
            }
            else if (type == OddType.TOTAL_UNDER) {
                odd.setStatus(Status.LOSE);
                if (total[p] < odd.getPoints()) {
                    odd.setStatus(Status.WIN);
                }
                else if (total[p] == odd.getPoints()) {
                    odd.setStatus(Status.PUSH);
                }
            }
            else if (type == OddType.SPREAD_TEAM0) {
                double points = odd.getPoints();
                odd.setStatus(Status.LOSE);
                if (result[0][p]+points > result[1][p]) {
                    odd.setStatus(Status.WIN);
                }
                else if (result[0][p]+points == result[1][p]) {
                    odd.setStatus(Status.PUSH);
                }
            }
            else if (type == OddType.SPREAD_TEAM1) {
                double points = odd.getPoints();
                odd.setStatus(Status.LOSE);
                if (result[1][p]+points > result[0][p]) {
                    odd.setStatus(Status.WIN);
                }
                else if (result[1][p]+points == result[0][p]) {
                    odd.setStatus(Status.PUSH);
                }
            }
            parlayOddService.edit(odd);
            parlays.addAll(odd.getParlays());
        }
        for (Parlay p : parlays) {
            parlayService.update(p);
        }
        m.setStatus(Status.FINISHED);
        matchEventDAO.edit(m);
    }

}
