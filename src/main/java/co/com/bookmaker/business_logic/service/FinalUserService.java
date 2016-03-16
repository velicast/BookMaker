/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service;

import co.com.bookmaker.data_access.dao.FinalUserDAO;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import co.com.bookmaker.util.type.Role;

/**
 *
 * @author eduarc
 */
@Stateless
public class FinalUserService {

    @EJB
    private FinalUserDAO finalUserDAO;
    
    public List<FinalUser> searchBy(String username, Long role, Integer status) {
        
        List<String> attributes = new ArrayList();
        List<Object> values = new ArrayList();
        
        if (username != null) {
            attributes.add("username");
            values.add(username);
        }
        if (status != null) {
            attributes.add("status");
            values.add(status);
        }
        List<FinalUser> preliminar = finalUserDAO.findAll(attributes.toArray(new String[]{}), values.toArray());
        List<FinalUser> result = new ArrayList();
        for (FinalUser user : preliminar) {
            if (user.inRole(role)) {
                result.add(user);
            }
        }
        return result;
    }
    
    public FinalUser getLoginUser(String username, String password) {
        
        return finalUserDAO.find(new String[] {"username", "password"},
                                 new Object[] { username,   password});
    }
    
    public FinalUser getUser(String username) {
        
        return finalUserDAO.find(new String[] {"username"},
                                 new Object[] { username});
    }
    
    public FinalUser getUser(String username, Agency agency) {
        
        return finalUserDAO.find(new String[] {"username", "agency.id"},
                                 new Object[] { username, agency.getId()});
    }
    
    public void setAttributes(FinalUser user, 
            String username, String password, String email, Calendar birthDate, String firstName, String lastName, 
            String city, String telephone, String address, Integer status,
            boolean admin, boolean manager, boolean analyst, boolean seller, boolean client) {
        
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setBirthDate(birthDate);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCity(city);
        user.setTelephone(telephone);
        user.setAddress(address);
        user.setStatus(status);
        
        user.removeRol(Role.ADMIN);
        if (admin) {
            user.addRol(Role.ADMIN);
        }
        user.removeRol(Role.MANAGER);
        if (manager) {
            user.addRol(Role.MANAGER);
        }
        user.removeRol(Role.ANALYST);
        if (analyst) {
            user.addRol(Role.ANALYST);
        }
        user.removeRol(Role.SELLER);
        if (seller) {
            user.addRol(Role.SELLER);
        }
        user.removeRol(Role.CLIENT);
        if (client) {
            user.addRol(Role.CLIENT);
        }
    }
    
    public FinalUser create(FinalUser user) {
        
        if (getUser(user.getUsername()) != null) {
            throw new IllegalArgumentException("User '"+user.getUsername()+"' already exists");
        }
        finalUserDAO.create(user);
        return user;
    }
    
    public FinalUser update(FinalUser user, String targetUsername) {
        
        String newUsername = user.getUsername();
        if (getUser(newUsername) != null && !newUsername.equals(targetUsername)) {
            throw new IllegalArgumentException("User '"+user.getUsername()+"' already exists");
        }
        finalUserDAO.edit(user);
        return user;
    }
    
    public FinalUser edit(FinalUser user) {
        
        finalUserDAO.edit(user);
        return user;
    }
}
