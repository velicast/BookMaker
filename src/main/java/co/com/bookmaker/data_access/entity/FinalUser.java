/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.data_access.entity;

import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.Tournament;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import co.com.bookmaker.util.type.Role;
import java.util.List;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author eduarc
 */
@Entity
public class FinalUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;

    private Long rol;
    
    private String password;
    
    private String email;
    
    private String firstName;
    
    private String lastName;
    
    private String city;
    
    private String telephone;
    
    private String address;
    
    private Integer status;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar birthDate;
    
    @OneToOne
    private Agency agency;
    
        // Tracking info. Not related to the business.
        // In future versions this info will be decoupled from business code
    @ManyToOne
    private FinalUser author;    
    
    @OneToMany(mappedBy="author")
    private List<FinalUser> createdUsers;
    
    @OneToMany(mappedBy="author")
    private List<Agency> createAgencies;
    
    @OneToMany(mappedBy="author")
    private List<MatchEvent> createdMatches;
    
    @OneToMany(mappedBy="author")
    private List<Tournament> createdTournaments;
    
    public FinalUser() {
    }
    
    public FinalUser(String username, Long rol, String password) {
        
        this.username = username;
        this.rol = rol;
        this.password = password;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String id) {
        this.username = id;
    }

    public Long getRol() {
        return rol;
    }

    public void setRol(Long rol) {
        this.rol = rol;
    }

    public void addRol(Long rol) {
        
        if (this.rol == null) {
            this.rol = rol;
        } else {
            this.rol |= rol;
        }
    }
    
    public void removeRol(Long rol) {
        
        if (this.rol == null) {
            return;
        }
        this.rol &= ~rol;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Calendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Calendar bithDate) {
        this.birthDate = bithDate;
    }

    public boolean inRole(Long r) {
        return new Role().inRole(rol, r);
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

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public List<FinalUser> getCreatedUsers() {
        return createdUsers;
    }

    public void setCreatedUsers(List<FinalUser> createdUsers) {
        this.createdUsers = createdUsers;
    }

    public List<Agency> getCreateAgencies() {
        return createAgencies;
    }

    public void setCreateAgencies(List<Agency> createAgencies) {
        this.createAgencies = createAgencies;
    }

    public List<MatchEvent> getCreatedMatches() {
        return createdMatches;
    }

    public void setCreatedMatches(List<MatchEvent> createdMatches) {
        this.createdMatches = createdMatches;
    }

    public List<Tournament> getCreatedTournaments() {
        return createdTournaments;
    }

    public void setCreatedTournaments(List<Tournament> createdTournaments) {
        this.createdTournaments = createdTournaments;
    }
    
    @Override
    public String toString() {
        return "data_access.entity.FinalUser[ username=" + username + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final FinalUser other = (FinalUser) obj;
        return Objects.equals(this.id, other.id);
    }
}
