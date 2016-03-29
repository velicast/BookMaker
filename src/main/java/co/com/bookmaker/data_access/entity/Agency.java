/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.data_access.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author eduarc
 */
@Entity
public class Agency implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String email;
    
    private String city;
    
    private String address;
    
    private String telephone;
    
    @OneToMany(mappedBy="agency")
    private List<FinalUser> employees;
    
    private Double maxProfit;
    
    private Integer minOddsParlay;
    
    private Integer maxOddsParlay;
    
    private Boolean acceptGlobalOdds;
    
    private Integer status;
    
    @ManyToOne
    private FinalUser author;
    
    public Agency() {
    }

    public Agency(String city, String address) {
        
        this.city = city;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FinalUser> getEmployees() {
        return employees;
    }

    public void setEmployees(List<FinalUser> employees) {
        this.employees = employees;
    }

    public Double getMaxProfit() {
        return maxProfit;
    }

    public void setMaxProfit(Double maxProfit) {
        this.maxProfit = maxProfit;
    }

    public Integer getMinOddsParlay() {
        return minOddsParlay;
    }

    public void setMinOddsParlay(Integer minOddsParlay) {
        this.minOddsParlay = minOddsParlay;
    }

    public Integer getMaxOddsParlay() {
        return maxOddsParlay;
    }

    public void setMaxOddsParlay(Integer maxOddsParlay) {
        this.maxOddsParlay = maxOddsParlay;
    }

    public Boolean getAcceptGlobalOdds() {
        return acceptGlobalOdds;
    }

    public void setAcceptGlobalOdds(Boolean acceptGlobalOdds) {
        this.acceptGlobalOdds = acceptGlobalOdds;
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
        return "data_access.entity.Agency[ id=" + getId() + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final Agency other = (Agency) obj;
        return Objects.equals(this.id, other.id);
    }

}
