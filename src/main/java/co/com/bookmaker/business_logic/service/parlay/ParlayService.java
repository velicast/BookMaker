/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.business_logic.service.parlay;

import co.com.bookmaker.data_access.dao.parlay.ParlayDAO;
import co.com.bookmaker.data_access.entity.Agency;
import co.com.bookmaker.data_access.entity.FinalUser;
import co.com.bookmaker.data_access.entity.parlay.Parlay;
import co.com.bookmaker.data_access.entity.parlay.ParlayOdd;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import co.com.bookmaker.util.type.Status;
import java.util.Collections;

/**
 *
 * @author eduarc
 */
public class ParlayService {

    public static final Long OFFSET_PARLAY_ID = 100000L;
    
    private final ParlayDAO parlayDAO;
    
    public ParlayService() {
        
        parlayDAO = new ParlayDAO();
    }
    
    public Parlay getParlay(Long id) {
        return parlayDAO.find(id);
    }
    
    public void create(Parlay p) {
        parlayDAO.create(p);
    }
    
    public void update(Parlay p) {
        
        if (p == null) {
            return;
        }
        updateStatus(p);
        updateProfit(p);
        parlayDAO.edit(p);
    }
    
    public Integer updateStatus(Parlay p) {
        
        if (p.getStatus().equals(Status.IN_QUEUE)) {
            return Status.IN_QUEUE;
        }
        if (p.getStatus().equals(Status.CANCELLED)) {
            return Status.CANCELLED;
        }
        List<ParlayOdd> bets = p.getOdds();
        
        for (ParlayOdd po : bets) {
            Integer st = po.getStatus();
            if (st.equals(Status.LOSE)) {
                p.setStatus(st);
                return st;
            }
        }
        for (ParlayOdd po : bets) {
            Integer st = po.getStatus();
            if (st.equals(Status.PENDING) || st.equals(Status.SELLING)) {
                p.setStatus(Status.PENDING);
                return st;
            }
        }
        p.setStatus(Status.WIN);
        return Status.WIN;
    }
    
    public double updateProfit(Parlay p) {
        
        double risk = p.getRisk();
        double profit;
        Integer st = p.getStatus();
        switch (st) {
            case Status.LOSE:
                profit = -risk; break;
            case Status.CANCELLED:
            case Status.PUSH:
                profit = risk; break;
            default:
                profit = getFactor(p)*risk;
        }
        if (p.getSeller() != null) {
            profit = Math.min(profit, p.getSeller().getAgency().getMaxProfit());
        }
        p.setProfit(profit);
        return profit;
    }
    
    public double getFactor(Parlay p) {
        
        double factor = 1;
        for (ParlayOdd po : p.getOdds()) {
            Integer st = po.getStatus();
            if (st.equals(Status.CANCELLED) || st.equals(Status.PUSH)) {
                continue;
            }
            double f = po.getLine();
            f = 1.+((f < 0) ? 100./-f : f/100.);
            factor *= f;
        }
        return factor;
    }

    public void edit(Parlay parlay) {
        parlayDAO.edit(parlay);
    }

    public List<Parlay> getParlays(Integer status, Agency agency) {
        return parlayDAO.findAll(new String[] {"status", "seller.agency.id"},
                                 new Object[] { status, agency.getId()});
    }
    public List<Parlay> getParlays(Integer status, FinalUser seller) {
        return parlayDAO.findAll(new String[] {"status", "seller.id"},
                                 new Object[] { status, seller.getId()});
    }
    
    /*public List<Parlay> getParlays(ParlayOdd odd) {
        
    }*/
    
    public List<Parlay> getParlaysInQueue(FinalUser seller) {
        
        List<Parlay> list = getParlays(Status.IN_QUEUE, seller);
        Collections.sort(list, new Comparator<Parlay>() {
            @Override
            public int compare(Parlay o1, Parlay o2) {
                return o1.getPurchaseDate().compareTo(o2.getPurchaseDate());
            }
        });
        return list;
    }
    
    public List<Parlay> searchBy(Long agencyId, Long id, String username, Calendar from, Calendar to, Integer status) {
        
        List<Parlay> pre;
        if (agencyId == null) {
            pre = parlayDAO.findAll();
        } else {
            List<String> attributes = new ArrayList();
            List values = new ArrayList();

            attributes.add("seller.agency.id");
            values.add(agencyId);

            if (id != null) {
                attributes.add("id");
                values.add(id);
            }
            if (username != null && username.length() > 0) {
                attributes.add("seller.username");
                values.add(username);
            }
            if (status != null) {
                attributes.add("status");
                values.add(status);
            }
            pre = parlayDAO.findAll(attributes.toArray(new String[]{}), values.toArray());
        }
        List<Parlay> result = new ArrayList();
        for (Parlay p : pre) {
            boolean add = true;
            Calendar pDate = p.getPurchaseDate();
            if (from != null) {
                add &= pDate.compareTo(from) >= 0;
            }
            if (to != null) {
                add &= pDate.compareTo(to) <= 0;
            }
            if (add) {
                result.add(p);
            }
        }
        Collections.sort(result, new Comparator<Parlay>() {
            @Override
            public int compare(Parlay o1, Parlay o2) {
                int cmpDate = o1.getPurchaseDate().compareTo(o2.getPurchaseDate());
                if (cmpDate > 0) return 1;
                if (cmpDate < 0) return -1;
                return o1.getStatus().compareTo(o2.getStatus());
            }
        });
        return result;
    }

    public Long getOFFSET_PARLAY_ID() {
        return OFFSET_PARLAY_ID;
    }
}
