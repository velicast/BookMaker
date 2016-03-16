/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.data_access.entity.parlay;

import co.com.bookmaker.data_access.entity.FinalUser;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author eduarc
 */
@Entity
public class Parlay implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double risk;
    
    private Double profit;
    
    @ManyToMany(cascade=CascadeType.MERGE)
    private List<ParlayOdd> odds;
    
    private Integer status;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar purchaseDate;
    
    @OneToOne
    private FinalUser seller;

    private String clientName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRisk() {
        return risk;
    }

    public void setRisk(Double bet) {
        this.risk = bet;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public List<ParlayOdd> getOdds() {
        return odds;
    }

    public void setOdds(List<ParlayOdd> odds) {
        this.odds = odds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Calendar getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Calendar purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public FinalUser getSeller() {
        return seller;
    }

    public void setSeller(FinalUser seller) {
        this.seller = seller;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return "data_access.entity.Parlay[ id=" + getId() + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Parlay other = (Parlay) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
