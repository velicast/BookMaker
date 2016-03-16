/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.data_access.entity.event;

import co.com.bookmaker.data_access.entity.parlay.ParlayOdd;
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
public class MatchEventPeriod implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer number;
    
    private String name;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar cutoff;
    
    @ManyToOne
    private MatchEvent match;
    
    private Integer status;

    @OneToMany(mappedBy="period")
    private List<Score> scores;
    
    @OneToMany(mappedBy="period")
    private List<ParlayOdd> odds;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getCutoff() {
        return cutoff;
    }

    public void setCutoff(Calendar cutoff) {
        this.cutoff = cutoff;
    }

    public MatchEvent getMatch() {
        return match;
    }

    public void setMatch(MatchEvent match) {
        this.match = match;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public List<ParlayOdd> getOdds() {
        return odds;
    }

    public void setOdds(List<ParlayOdd> odds) {
        this.odds = odds;
    }

    @Override
    public String toString() {
        return "data_access.entity.GamePeriod[ id=" + getId() + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final MatchEventPeriod other = (MatchEventPeriod) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
