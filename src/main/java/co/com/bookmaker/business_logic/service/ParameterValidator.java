/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The checker throws an IllegalArgumentException if the arguments doesn't meet the constrains
 * 
 * @author eduarc
 */
public class ParameterValidator {

    private static final Pattern usernamePattern;
    private static final Pattern passwordPattern;
    private static final Pattern firstNamePattern;
    private static final Pattern lastNamePattern;
    private static final Pattern agencyNamePattern;
    private static final Pattern tournamentNamePattern;
    private static final Pattern matchNamePattern;
    
    static {
        usernamePattern = Pattern.compile("[\\w\\.]{3,20}");
        passwordPattern = Pattern.compile(".{4,}");
        firstNamePattern = Pattern.compile("[\\w\\s]{2,30}");
        lastNamePattern = Pattern.compile("[\\w\\s]{2,30}");
        agencyNamePattern = Pattern.compile(".{4,}");
        tournamentNamePattern = Pattern.compile(".{1,}");
        matchNamePattern = Pattern.compile(".{1,}");
    }
    //throw new IllegalArgumentException("");
    
    public void checkUsername(String username) {
        
        if (username == null || username.length() == 0) {
            throw new IllegalArgumentException("Usuario no proporcionado");
        }
        Matcher matcher = usernamePattern.matcher(username);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Usuario inválido");
        }
    }
    
    public void checkDateRange(Calendar from, Calendar to) {
        
        if (from != null && to != null) {
            if (from.after(to)) {
                throw new IllegalArgumentException("Rango de fechas inválido. Desde está después del Hasta");
            }
        }
    }
    
    public void checkPassword(String password) {
        
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Contraseña no proporcionada");
        }
        Matcher matcher = passwordPattern.matcher(password);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Contraseña inválida");
        }
    }
    
    public void checkFirstName(String firstName) {
        
        if (firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException("Nombre no proporcionado");
        }
        Matcher matcher = firstNamePattern.matcher(firstName);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Nombre inválido");
        }
    }
    
    public void checkLastName(String lastName) {
        
        if (lastName == null || lastName.trim().length() == 0) {
            throw new IllegalArgumentException("Apellido no proporcionado");
        }
        Matcher matcher = lastNamePattern.matcher(lastName);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Apellido inválido");
        }
    }
    
    public void checkAgencyName(String agencyName) {
        
        if (agencyName == null || agencyName.trim().length() == 0) {
            throw new IllegalArgumentException("Nombre de agencia no proporcionado");
        }
        Matcher matcher = agencyNamePattern.matcher(agencyName);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Nombre de agencia inválido");
        }
    }
    
    public void checkTournamentName(String tournamentName) {
        
        if (tournamentName == null || tournamentName.trim().length() == 0) {
            throw new IllegalArgumentException("Nombre de torneo no proporcionado");
        }
        Matcher matcher = tournamentNamePattern.matcher(tournamentName);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Nombre de torneo inválido");
        }
    }
    
    public void checkMatchName(String matchName) {
        
        if (matchName == null || matchName.trim().length() == 0) {
            throw new IllegalArgumentException("Nombre de juego no proporcionado");
        }
        Matcher matcher = matchNamePattern.matcher(matchName);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Nombre de juego inválido");
        }
    }
    
    public void checkCity(String city) {
        
    }
    
    public void checkAddress(String address) {
    }
    
    public void checkTelephone(String telephone) {
    }
    
    public void checkBirthDate(Calendar birthDate) {
        
        if (birthDate == null) {
            throw new IllegalArgumentException("Fecha de nacimiento no proporcionada");
        }
        Calendar now = Calendar.getInstance();
        long years = now.get(Calendar.YEAR)-1-birthDate.get(Calendar.YEAR);
        long nowMonth = now.get(Calendar.MONTH);
        long birthDateMonth = birthDate.get(Calendar.MONTH);
        if (nowMonth > birthDateMonth) {
            years++;
        }
        else if (nowMonth == birthDateMonth && now.get(Calendar.DAY_OF_MONTH) >= birthDate.get(Calendar.DAY_OF_MONTH)) {
            years++;
        }
        if (years < 18 || years > 110) {
            throw new IllegalArgumentException("Sólo edades entre los 18 y 100 años");
        }
    }
    
    public void checkMatchStartDate(Calendar startDate) {
        
        if (startDate == null) {
            throw new IllegalArgumentException("Fecha de inicio no proporcionada");
        }
        Calendar now = Calendar.getInstance();
        if (now.after(startDate)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser futura");
        }
    }
    
    public void checkMinOdds(Integer minOdds) {
        
        if (minOdds != null && minOdds < 0) {
            throw new IllegalArgumentException("Debe ser mayor o igual a cero");
        }
    }
    
    public void checkMaxOdds(Integer maxOdds) {
        
        if (maxOdds != null && maxOdds < 0) {
            throw new IllegalArgumentException("Debe ser mayor o igual a cero");
        }
    }
    
    public void checkOddsRange(Integer minOdds, Integer maxOdds) {
        
        if (maxOdds != null && minOdds != null && maxOdds < minOdds) {
            throw new IllegalArgumentException("Debe ser menor o igual al número max. de logros");
        }
    }
    
    public void checkMaxProfit(Double maxProfit) {
        
        if (maxProfit != null && maxProfit <= 0.0) {
            throw new IllegalArgumentException("Debe der mayor que cero");
        }
    }

    public void checkTeamName(String strTeamName) {
        
        if (strTeamName == null || strTeamName.trim().length() == 0) {
            throw new IllegalArgumentException("Nombre de equipo no proporcionado");
        }
    }
}