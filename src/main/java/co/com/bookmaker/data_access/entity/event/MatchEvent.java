/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.data_access.entity.event;

import co.com.bookmaker.data_access.entity.FinalUser;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author eduarc
 */
@Entity
public class MatchEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar startDate;

    @ManyToOne
    private Tournament tournament;
    
    private Integer status;
    
    @OneToMany(mappedBy="match")
    private List<Team> teams;
    
    @OneToMany(mappedBy="match")
    private List<MatchEventPeriod> periods;
    
    @ManyToOne
    private FinalUser author;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<MatchEventPeriod> getPeriods() {
        return periods;
    }

    public void setPeriods(List<MatchEventPeriod> periods) {
        this.periods = periods;
    }

    public FinalUser getAuthor() {
        return author;
    }

    public void setAuthor(FinalUser author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "data_access.entity.event.MatchEvent[ id=" + getId() + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MatchEvent other = (MatchEvent) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
