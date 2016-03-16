/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.data_access.entity.event;

import co.com.bookmaker.data_access.entity.FinalUser;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author eduarc
 */
@Entity
public class Tournament implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToOne
    private Sport sport;
    
    @OneToMany(mappedBy="tournament")
    private List<MatchEvent> matches;
    
    private Integer status;
    
    @OneToOne
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

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public List<MatchEvent> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchEvent> games) {
        this.matches = games;
    }
    
    public void addMatch(MatchEvent g) {
        this.matches.add(g);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public FinalUser getAuthor() {
        return author;
    }

    public void setAuthor(FinalUser author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "data_access.entity.League[ id=" + getId() + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.id);
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
        final Tournament other = (Tournament) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
