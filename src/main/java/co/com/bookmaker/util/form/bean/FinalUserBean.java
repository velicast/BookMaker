/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.util.form.bean;

import java.io.Serializable;

/**
 *
 * @author eduarc
 */
public class FinalUserBean implements Serializable {
    
    String targetUsername;
    String username;
    Boolean rAdmin;
    Boolean rManager;
    Boolean rAnalyst;
    Boolean rSeller;
    Boolean rClient;
    String email;
    String firstName;
    String lastName;
    String city;
    String telephone;
    String address;
    Integer status;
    String birthDate;

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getrAdmin() {
        return rAdmin;
    }

    public void setrAdmin(Boolean rAdmin) {
        this.rAdmin = rAdmin;
    }

    public Boolean getrManager() {
        return rManager;
    }

    public void setrManager(Boolean rManager) {
        this.rManager = rManager;
    }

    public Boolean getrAnalyst() {
        return rAnalyst;
    }

    public void setrAnalyst(Boolean rAnalyst) {
        this.rAnalyst = rAnalyst;
    }

    public Boolean getrSeller() {
        return rSeller;
    }

    public void setrSeller(Boolean rSeller) {
        this.rSeller = rSeller;
    }

    public Boolean getrClient() {
        return rClient;
    }

    public void setrClient(Boolean rClient) {
        this.rClient = rClient;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    
}
