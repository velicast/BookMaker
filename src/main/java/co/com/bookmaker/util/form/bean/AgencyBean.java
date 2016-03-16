/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.util.form.bean;

/**
 *
 * @author eduarc
 */
public class AgencyBean {
    
    Long id;
    String managerUsername;
    String name;
    String email;
    String telephone;
    String city;
    String address;
    Integer status;
    String minOdds;
    String maxOdds;
    String maxProfit;
    Boolean acceptGlobalOdds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMinOdds() {
        return minOdds;
    }

    public void setMinOdds(String minOdds) {
        this.minOdds = minOdds;
    }

    public String getMaxOdds() {
        return maxOdds;
    }

    public void setMaxOdds(String maxOdds) {
        this.maxOdds = maxOdds;
    }

    public String getMaxProfit() {
        return maxProfit;
    }

    public void setMaxProfit(String maxProfit) {
        this.maxProfit = maxProfit;
    }

    public Boolean getAcceptGlobalOdds() {
        return acceptGlobalOdds;
    }

    public void setAcceptGlobalOdds(Boolean acceptGlobalOdds) {
        this.acceptGlobalOdds = acceptGlobalOdds;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
