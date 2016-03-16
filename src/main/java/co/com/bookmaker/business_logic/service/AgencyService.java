/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service;

import co.com.bookmaker.data_access.dao.AgencyDAO;
import co.com.bookmaker.data_access.dao.FinalUserDAO;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author eduarc
 */
@Stateless
public class AgencyService {

    @EJB
    private AgencyDAO agencyDAO;
    @EJB
    private FinalUserDAO finalUserDAO;
    
    public Agency getAgency(Long id) {
        return agencyDAO.find(id);
    }
    
    public void setAttributes(Agency agency, String name,
            String email, String telephone, String city, String address, 
            Integer minOdds, Integer maxOdds, Double maxPayout, Boolean acceptGlobalOdds, Integer status) {
        
        agency.setName(name);
        agency.setEmail(email);
        agency.setTelephone(telephone);
        agency.setCity(city);
        agency.setAddress(address);
        agency.setMinOddsParlay(minOdds);
        agency.setMaxOddsParlay(maxOdds);
        agency.setMaxProfit(maxPayout);
        agency.setAcceptGlobalOdds(acceptGlobalOdds);
        agency.setStatus(status);
    }
    
    public void create(Agency agency, FinalUser manager) {
        
        agencyDAO.create(agency);
        manager.setAgency(agency);
        finalUserDAO.edit(manager);
    }
    
    public void edit(Agency agency) {
        agencyDAO.edit(agency);
    }
    
    public List<FinalUser> getEmployees(Agency agency) {
        
        return finalUserDAO.findAll(new String[] {"agency.id"}, 
                                    new Object[] {agency.getId()});
    }
    
    public List<Agency> getAllAgencies() {
        return agencyDAO.findAll();
    }
}