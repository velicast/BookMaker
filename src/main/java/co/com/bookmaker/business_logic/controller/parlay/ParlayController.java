/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.controller.parlay;

import co.com.bookmaker.business_logic.controller.ClientController;
import co.com.bookmaker.business_logic.controller.GenericController;
import co.com.bookmaker.business_logic.controller.HomeController;
import co.com.bookmaker.business_logic.controller.ManagerController;
import co.com.bookmaker.business_logic.controller.SellerController;
import co.com.bookmaker.business_logic.service.ParameterValidator;
import co.com.bookmaker.business_logic.service.event.MatchEventService;
import co.com.bookmaker.business_logic.service.event.MatchEventPeriodService;
import co.com.bookmaker.business_logic.service.event.SportService;
import co.com.bookmaker.business_logic.service.event.TeamService;
import co.com.bookmaker.business_logic.service.event.TournamentService;
import co.com.bookmaker.business_logic.service.parlay.ParlayOddService;
import co.com.bookmaker.business_logic.service.parlay.ParlayService;
import co.com.bookmaker.business_logic.service.security.AuthenticationService;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.event.MatchEvent;
import co.com.bookmaker.data_access.entity.event.Sport;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import co.com.bookmaker.data_access.entity.parlay.ParlayOdd;
import co.com.bookmaker.util.form.bean.ParlayOddBean;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import co.com.bookmaker.util.form.bean.SearchParlayBean;
import co.com.bookmaker.util.type.Attribute;
import co.com.bookmaker.util.type.Information;
import co.com.bookmaker.util.type.OddType;
import co.com.bookmaker.util.type.Parameter;
import co.com.bookmaker.util.type.Role;
import co.com.bookmaker.util.type.Status;
import java.text.NumberFormat;
import java.util.Map;
import java.util.TreeMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author eduarc
 */
@WebServlet(name = "ParlayController", urlPatterns = {"/"+ParlayController.URL})
public class ParlayController extends GenericController {

    public static final String URL = "parlay";
    
    public static final String GET_PROFIT = "getprofit";
    public static final String GET_RISK = "getrisk";
    public static final String BUY = "buy";
    public static final String ACCEPT = "accept";
    public static final String CANCEL = "cancel";
    public static final String TABLE_SUMMARY = "table_summary";
    public static final String SUMMARY = "summary";
    public static final String PRINT = "print";
    public static final String SEARCH = "search";
    
    @EJB
    private AuthenticationService auth;
    @EJB
    private ParlayService parlayService;
    @EJB
    private ParlayOddService parlayOddService;
    @EJB
    private SportService sportService;
    @EJB
    private TeamService teamService;
    @EJB
    private TournamentService tournamentService;
    @EJB
    private MatchEventService matchEventService;
    @EJB
    private MatchEventPeriodService matchPeriodService;
    @EJB
    private ParameterValidator validator;
    
    @Override
    public void init() {
        
        allowDO(BUY, Role.CLIENT);
        allowDO(GET_PROFIT, Role.CLIENT);
        allowDO(GET_RISK, Role.CLIENT);
        allowDO(SEARCH, Role.SELLER|Role.MANAGER);
        allowDO(ACCEPT, Role.SELLER);
        allowDO(CANCEL, Role.SELLER);
        allowDO(PRINT, Role.SELLER);
    }

    public static String getJSP(String resource) {
        return "/WEB-INF/parlay/"+resource+".jsp";
    }

    @Override
    protected void processTO(String resource) {
        redirectError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void processDO(String resource) {
        
        FinalUser user = auth.sessionUser(request);
        Agency agency = user.getAgency();
        if (agency == null) {
            request.setAttribute(Information.ERROR, "Access denied");
            forward(HomeController.getJSP(HomeController.INDEX));
            return;
        }
        switch (resource) {
            case GET_PROFIT:
                doGetProfit(); break;
            case GET_RISK:
                doGetRisk(); break;
            case BUY:
                doBuy(); break;
            case ACCEPT:
                doAccept(); break;
            case CANCEL:
                doCancel(); break;
            case SEARCH:
                doSearch(); break;       
            case PRINT:
                doPrint(); break;
            default:
                redirectError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void doGetProfit() {
        
        FinalUser client = auth.sessionUser(request);
        Agency agency = client.getAgency();

        Double maxProfit = agency.getMaxProfit();
        
        String oddselection = request.getParameter(Parameter.ODDS);
        Double risk;
        try {
            risk = Double.parseDouble(request.getParameter(Parameter.RISK));
        } catch (Exception ex) {
            Logger.getLogger(ParlayOddController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if (risk <= 0) {
            return;
        }
        List<ParlayOdd> odds = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(oddselection);
        while (tokenizer.hasMoreTokens()) {
            Long oddId;
            try {
                oddId = Long.parseLong(tokenizer.nextToken());
            } catch (Exception ex) {
                Logger.getLogger(ParlayOddController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            ParlayOdd odd = parlayOddService.getOdd(oddId);
            if (odd == null) {
                return;
            }
            MatchEvent m = odd.getPeriod().getMatch();
            matchEventService.update(m);
            
            Agency a = m.getAuthor().getAgency();
            if (m.getStatus() != Status.ACTIVE || (a == null && !agency.getAcceptGlobalOdds()) || (a != null && !agency.equals(a))) {
                return;
            }
            odds.add(odd);
        }
        Parlay parlay = new Parlay();
        parlay.setStatus(Status.PENDING);
        parlay.setOdds(odds);
        parlay.setRisk(risk);

        parlayService.updateProfit(parlay);
        
        String res = String.format("%.2f", Math.min(maxProfit, parlay.getProfit()));
        try {
            response.getOutputStream().write(res.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(ParlayOddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void doGetRisk() {
        
        FinalUser client = auth.sessionUser(request);
        Agency agency = client.getAgency();

        Double maxProfit = agency.getMaxProfit();
        
        String oddselection = request.getParameter(Parameter.ODDS);
        Double profit;
        try {
            profit = Double.parseDouble(request.getParameter(Parameter.PROFIT));
        } catch (Exception ex) {
            Logger.getLogger(ParlayOddController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if (profit <= 0 || profit > maxProfit) {
            return;
        }
        
        List<ParlayOdd> odds = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(oddselection);
        while (tokenizer.hasMoreTokens()) {
            Long oddId;
            try {
                oddId = Long.parseLong(tokenizer.nextToken());
            } catch (Exception ex) {
                Logger.getLogger(ParlayOddController.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            ParlayOdd odd = parlayOddService.getOdd(oddId);
            if (odd == null) {
                return;
            }
            MatchEvent m = odd.getPeriod().getMatch();
            matchEventService.update(m);
            
            Agency a = m.getAuthor().getAgency();
            if (m.getStatus() != Status.ACTIVE || (a == null && !agency.getAcceptGlobalOdds()) || (a != null && !agency.equals(a))) {
                return;
            }
            odds.add(odd);
        }
        
        Parlay parlay = new Parlay();
        parlay.setStatus(Status.PENDING);
        parlay.setOdds(odds);
        Double factor = parlayService.getFactor(parlay);
        
        String res = String.format("%.2f", profit/factor);
        try {
            response.getOutputStream().write(res.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(ParlayOddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doBuy() {
        
        FinalUser client = auth.sessionUser(request);
        Agency agency = client.getAgency();

        Integer minOdds = agency.getMinOddsParlay();
        Integer maxOdds = agency.getMaxOddsParlay();
        Double maxProfit = agency.getMaxProfit();
        
        boolean validated = true;
        String info = "";
        
        String name = request.getParameter(Parameter.NAME);
        String oddselection = request.getParameter(Parameter.ODDS);
        String strBet = request.getParameter(Parameter.RISK);
        Double bet = null;
        try {
            bet = Double.parseDouble(strBet);
        } catch (Exception ex) {
            info += "* Invalid bet value: "+strBet+"<br/>";
            validated = false;
        }
        
        List<ParlayOdd> odds = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(oddselection);
        while (tokenizer.hasMoreTokens()) {
            Long oddId = null;
            try {
                oddId = Long.parseLong(tokenizer.nextToken());
            } catch (Exception ex) {}
            ParlayOdd odd = parlayOddService.getOdd(oddId);
            if (odd == null || oddId == null) {
                info += "* You have requested for an invalid odd "+oddId+"<br/>";
                break;
            }
            MatchEvent m = odd.getPeriod().getMatch();
            matchEventService.update(m);
            
            Agency a = m.getAuthor().getAgency();
            if (m.getStatus() != Status.ACTIVE || (a == null && !agency.getAcceptGlobalOdds()) || (a != null && !agency.equals(a))) {
                info += "* You have requested for an invalid odd "+oddId+"<br/>";
                break;
            }
            odds.add(odd);
        }
        
        if (odds.size() < minOdds || odds.size() > maxOdds) {
            info += " * Please select between "+minOdds+" and "+maxOdds+" per parlay. <br/>";
            validated = false;
        }
        if (bet <= 0) {
            info += "* The bet must be positive: "+bet+" <br/>";
            validated = false;
        }
        if (name == null || name.length() == 0) {
            info += "* Please give your name. <br/>";
            validated = false;
        }
        
        List<Sport> sports = sportService.getSports(Status.ACTIVE);
        request.setAttribute(Attribute.SPORT, sports);
        request.setAttribute(Attribute.TEAM_SERVICE, teamService);
        request.setAttribute(Attribute.PARLAYODD_SERVICE, parlayOddService);
        request.setAttribute(Attribute.TOURNAMENT_SERVICE, tournamentService);
        request.setAttribute(Attribute.MATCH_EVENT_SERVICE, matchEventService);
        request.setAttribute(Attribute.MATCH_PERIOD_SERVICE, matchPeriodService);
        
        if (!validated) {
            request.setAttribute(Information.INFO, info);
            forward(ClientController.getJSP(ClientController.INDEX));
            return;
        }
        
        Parlay parlay = new Parlay();
        parlay.setStatus(Status.IN_QUEUE);
        parlay.setOdds(odds);
        parlay.setRisk(bet);
        parlayService.updateProfit(parlay);
        parlay.setProfit(Math.min(maxProfit, parlay.getProfit()));
        parlay.setPurchaseDate(Calendar.getInstance());
        parlay.setSeller(client);
        parlay.setClientName(name);
        
        try {
            parlayService.create(parlay);
        } catch(EJBException ex) {
            request.setAttribute(Information.ERROR, "Opss! Something went wrong. Please try again.");
            forward(ClientController.getJSP(ClientController.INDEX));
            return;
        }
        request.setAttribute(Information.INFO, "<b>Ticket ID: "+parlay.getId()+"</b>"
                +"<br/><b style=\"font-size: 20px \">Thanks for purchasing!</b>");
        
        forward(ClientController.getJSP(ClientController.INDEX));
    }

    private void doAccept() {
        
        FinalUser source = auth.sessionUser(request);
        Agency agency = source.getAgency();
        
        String strParlayId = request.getParameter(Parameter.PARLAY);
        
            // INICIO VALIDACION
        Long parlayId;
        try {
            parlayId = Long.parseLong(strParlayId);
        } catch (Exception ex) {
            request.setAttribute(Information.ERROR, "Parlay not found");
            forward(SellerController.getJSP(SellerController.SELLING_QUEUE));
            return;
        }
        
        Parlay parlay = parlayService.getParlay(parlayId);
        if (parlay == null) {
            request.setAttribute(Information.ERROR, "Parlay "+parlayId+" not found");
            forward(SellerController.getJSP(SellerController.SELLING_QUEUE));
            return;
        }
        
            // VERIFICAR QUE EL VENDEDOR QUE ACEPTA PERTENECE A LA MISMA AGENCIA QUE
            // EL VENDEDOR DEL PARLAY
        FinalUser seller = parlay.getSeller();
        if (!agency.equals(seller.getAgency())) {
            request.setAttribute(Information.ERROR, "You are not allowed to execute this operation");
            forward(SellerController.getJSP(SellerController.SELLING_QUEUE));
            return;
        }
        
        if (!parlay.getStatus().equals(Status.IN_QUEUE)) {
            request.setAttribute(Information.ERROR, "Parlay not in queue");
            forward(SellerController.getJSP(SellerController.SELLING_QUEUE));
            return;
        }
        try {
            for (ParlayOdd odd : parlay.getOdds()) {
                Integer matchStatus = odd.getPeriod().getMatch().getStatus();
                if (matchStatus.equals(Status.PENDING_RESULT) || matchStatus.equals(Status.FINISHED)) {
                    request.setAttribute(Information.ERROR, "The Parlay was cancelled due to time expired.");
                    parlay.setStatus(Status.CANCELLED);
                    parlayService.edit(parlay);
                    forward(SellerController.getJSP(SellerController.SELLING_QUEUE));
                    return;
                }
            }
                // FIN VALIDACION
            request.setAttribute(Attribute.PARLAY, parlay);
            request.setAttribute(Attribute.PARLAY_ODDS, parlay.getOdds());
            
            parlay.setStatus(Status.PENDING);
            parlay.setSeller(auth.sessionUser(request));
            parlayService.edit(parlay);
        } catch (EJBException ex) {
            parlay.setStatus(Status.IN_QUEUE);
            request.setAttribute(Information.ERROR, "Opss! something went wrong. Please try again.");
            forward(SellerController.getJSP(SellerController.PARLAY_SUMMARY));
            return;
        }
        request.setAttribute(Information.INFO, "<b>Parlay "+parlayId+" Accepted! Now you can print the ticket.</b>");
        forward(SellerController.getJSP(SellerController.PARLAY_SUMMARY));
    }

    private void doCancel() {
        
        FinalUser source = auth.sessionUser(request);
        Agency agency = source.getAgency();

        String strParlayId = request.getParameter(Parameter.PARLAY);
        
            // INICIO VALIDACION
        Long parlayId;
        try {
            parlayId = Long.parseLong(strParlayId);
        } catch (Exception ex) {
            request.setAttribute(Information.ERROR, "Parlay not found");
            forward(SellerController.getJSP(SellerController.SELLING_QUEUE));
            return;
        }
        
        Parlay parlay = parlayService.getParlay(parlayId);
        if (parlay == null) {
            request.setAttribute(Information.ERROR, "Parlay "+parlayId+" not found");
            forward(SellerController.getJSP(SellerController.SELLING_QUEUE));
            return;
        }
        if (!parlay.getStatus().equals(Status.IN_QUEUE)) {
            request.setAttribute(Information.ERROR, "Parlay not in queue");
            forward(SellerController.getJSP(SellerController.SELLING_QUEUE));
            return;
        }
        // VERIFICAR QUE EL VENDEDOR QUE CANCELA PERTENECE A LA MISMA AGENCIA QUE
        // EL VENDEDOR DEL PARLAY
        FinalUser seller = parlay.getSeller();
        if (!agency.equals(seller.getAgency())) {
            request.setAttribute(Information.ERROR, "Restricted operation");
            forward(SellerController.getJSP(SellerController.SELLING_QUEUE));
            return;
        }
            // FIN VALIDACION
        parlay.setStatus(Status.CANCELLED);
        parlay.setSeller(auth.sessionUser(request));
        try {
            parlayService.update(parlay);
        } catch (EJBException ex) {
            parlay.setStatus(Status.IN_QUEUE);
            request.setAttribute(Information.ERROR, "Opss! Something went wrong. Please try again.");
            request.setAttribute(Attribute.PARLAY, parlay);
            request.setAttribute(Attribute.PARLAY_ODDS, parlay.getOdds());
            forward(SellerController.getJSP(SellerController.PARLAY_SUMMARY));
            return;
        }
        request.setAttribute(Information.INFO, "<b>Parlay Id "+parlayId+" Canceled!</b>");
        forward(SellerController.getJSP(SellerController.SELLING_QUEUE));
    }

    private void doSearch() {
        
        FinalUser sessionUser = auth.sessionUser(request);
        
        String strRoleRequester = request.getParameter(Parameter.ROLE);
        Long roleRequester = null;
        try {
            roleRequester = Long.parseLong(strRoleRequester);
        } catch (Exception ex) {}
        if (roleRequester == null || !(new Role().someRole(auth.sessionRole(request), roleRequester))) {
            redirect(HomeController.URL);
            return;
        }
        
        Agency agency = sessionUser.getAgency();
        
        String strParlayId = request.getParameter(Parameter.PARLAY);
        String username = request.getParameter(Parameter.USERNAME);
        String strFrom = request.getParameter(Parameter.TIME_FROM);
        String strTo = request.getParameter(Parameter.TIME_TO);
        String strStatus = request.getParameter(Parameter.STATUS);
        
        
        boolean validated = true;
        
        if (username != null && username.trim().length() > 0) {
            try {
                validator.checkUsername(username);
            } catch (EJBException ex) {
                validated = false;
                request.setAttribute(Information.USERNAME, ex.getCausedByException().getMessage());
            }
        }
        if (username != null && username.trim().length() == 0) {
            username = null;
        }
        
        Long parlayId = null;
        if (strParlayId != null && strParlayId.length() > 0) {
            try {
                parlayId = Long.parseLong(strParlayId);
            } catch(Exception ex) {
                request.setAttribute(Information.PARLAY, "Invalid parlay id "+strParlayId);
                validated = false;
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar from = null;
        if (strFrom != null && strFrom.length() > 0) {
            from = Calendar.getInstance();
            try {
                from.setTime(formatter.parse(strFrom));
                from.set(Calendar.HOUR_OF_DAY, 0);
                from.set(Calendar.MINUTE, 0);
                from.set(Calendar.SECOND, 0);
            } catch (ParseException ex) {
                request.setAttribute(Information.TIME_FROM, "Invalid value for date "+strFrom);
                validated = false;
            }
        }
        Calendar to = null;
        if (strTo != null && strTo.length() > 0) {
            to = Calendar.getInstance();
            try {
                to.setTime(formatter.parse(strTo));
                to.set(Calendar.HOUR_OF_DAY, 23);
                to.set(Calendar.MINUTE, 59);
                to.set(Calendar.SECOND, 59);
            } catch (ParseException ex) {
                request.setAttribute(Information.TIME_TO, "Invalid value for date "+strTo);
                validated = false;
            }
        }
        
        try {
            validator.checkDateRange(from, to);
        } catch (EJBException ex) {
            validated = false;
            request.setAttribute(Information.TIME_FROM, ex.getCausedByException().getMessage());
        }
        
        Integer status = null;
        if (strStatus != null && strStatus.length() > 0) {
            try {
                status = Integer.parseInt(strStatus);
            } catch (Exception ex) {
                request.setAttribute(Information.TIME_FROM, "Invalid status value "+strStatus);
                validated = false;
            }
        }
        if (!validated) {
            SearchParlayBean sp = new SearchParlayBean();
            sp.setId(strParlayId);
            sp.setUsername(username);
            sp.setFrom(strFrom);
            sp.setTo(strTo);
            sp.setStatus(status);
            request.setAttribute(Attribute.PARLAY, sp);
            if (roleRequester == Role.SELLER) {
                forward(SellerController.getJSP(SellerController.SEARCH_PARLAY));
            }
            else if (roleRequester == Role.MANAGER) {
                forward(ManagerController.getJSP(ManagerController.SEARCH_PARLAY));
            }
            return;
        }
        List<Parlay> result = parlayService.searchBy(agency.getId(), parlayId, username, from, to, status);
        request.setAttribute(Attribute.PARLAYS, result);
        
        if (roleRequester == Role.SELLER) {
            forward(SellerController.getJSP(SellerController.PARLAY_SEARCH_RESULT));
        }
        else if (roleRequester == Role.MANAGER) {
            forward(ManagerController.getJSP(ManagerController.PARLAY_SEARCH_RESULT));
        }
    }

    private String format(Double v) {
        
        String s = String.format("%d", v.intValue());
        if (Math.abs(v-v.intValue()) > 0.0) {
            s = String.format("%.1f", v);
        }
        if (v > 0.0) s = "+"+s;
        return s;
    }
    
    private void doPrint() {
        
        String strParlayId = request.getParameter(Parameter.PARLAY);
        
        Long parlayId;
        try {
            parlayId = Long.parseLong(strParlayId);
        } catch (Exception ex) {
            redirect(HomeController.URL);
            return;
        }
        Parlay parlay = parlayService.getParlay(parlayId);
        if (parlay == null || !parlay.getStatus().equals(Status.PENDING)) {
            redirect(HomeController.URL);
            return;
        }
        try {
            for (ParlayOdd odd : parlay.getOdds()) {
                Integer matchStatus = odd.getPeriod().getMatch().getStatus();
                if (matchStatus.equals(Status.PENDING_RESULT) || matchStatus.equals(Status.FINISHED)) {
                    request.setAttribute(Information.ERROR, "The Parlay was cancelled due to time expired.");
                    parlay.setStatus(Status.CANCELLED);
                    parlayService.edit(parlay);
                    forward(SellerController.getJSP(SellerController.PARLAY_SUMMARY));
                    return;
                }
            }
        } catch (EJBException ex) {
            parlay.setStatus(Status.PENDING);
            request.setAttribute(Information.ERROR, "Opss! something went wrong. Please try again.");
            request.setAttribute(Attribute.PARLAY, parlay);
            forward(SellerController.getJSP(SellerController.PARLAY_SUMMARY));
            return;
        }
        
        NumberFormat moneyFormatter = NumberFormat.getNumberInstance();
        moneyFormatter.setMaximumFractionDigits(0);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        
        List<ParlayOddBean> odds = new ArrayList();
        for (ParlayOdd odd : parlay.getOdds()) {
            ParlayOddBean oddBean = new ParlayOddBean();
            
            MatchEvent match = odd.getPeriod().getMatch();
            oddBean.setMatch(match.getName());
            oddBean.setStartDate(dateFormatter.format(match.getStartDate().getTime()));
            
            switch (odd.getType()) {
                case OddType.MONEY_LINE:
                    oddBean.setTeam(odd.getTeam().getName());
                    oddBean.setOdd(format(odd.getLine()));
                    break;
                case OddType.TOTAL_OVER:
                    oddBean.setOdd("Over "+format(odd.getPoints())+" "+format(odd.getLine()));
                    break;
                case OddType.TOTAL_UNDER:
                    oddBean.setOdd("Under "+format(odd.getPoints())+" "+format(odd.getLine()));
                    break;
                case OddType.SPREAD_TEAM0:
                case OddType.SPREAD_TEAM1:
                    oddBean.setTeam(odd.getTeam().getName());
                    oddBean.setOdd(format(odd.getPoints())+" "+format(odd.getLine()));
                    break;
                case OddType.DRAW_LINE:
                    oddBean.setOdd("Draw "+format(odd.getLine()));
                    break;
            }
            odds.add(oddBean);
        }

        Map<String, String> parameters = new TreeMap();
        parameters.put("agency", parlay.getSeller().getAgency().getName());
        parameters.put("date", dateFormatter.format(parlay.getPurchaseDate().getTime()));
        parameters.put("ticketID", parlay.getId()+"");
        parameters.put("risk", "$ "+moneyFormatter.format(parlay.getRisk()));
        parameters.put("profit", "$ "+moneyFormatter.format(parlay.getProfit()));
        
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(odds);
        
        try {
                // compiled at co.com.bookmaker.business_logic.service.security.ActiveSessions.java
            JasperReport report = (JasperReport) request.getServletContext().getAttribute(Attribute.COMPILED_TICKED_REPORT);
            JasperPrint jPrint = JasperFillManager.fillReport(report, parameters, dataSource);
            byte[] pdfData = JasperExportManager.exportReportToPdf(jPrint);
            
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=ticket"+parlay.getId()+".pdf");
            response.setContentLength(pdfData.length);
            response.getOutputStream().write(pdfData);
        } catch (JRException | IOException ex) {
            Logger.getLogger(ParlayController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
