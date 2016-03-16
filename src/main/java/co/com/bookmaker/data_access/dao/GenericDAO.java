/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.bookmaker.data_access.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author eduarc
 * @param <T>
 */
public class GenericDAO<T> {
    
    private final Class<T> entityClass;

    @PersistenceContext(unitName = "BookMakerPU")
    private EntityManager em;
    
    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public EntityManager getEntityManager() {
        return em;
    }
    
    public void create(T entity) {
        
        try {
            em.persist(entity);
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        }
    }

    public void edit(T entity) {
        
        try {
            em.merge(entity);
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        }
    }

    public void remove(T entity) {
        
        try {
            em.remove(em.merge(entity));
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        }
    }

    public T find(String[] attributes, Object[] values) {
        
        String queryString = "SELECT t FROM "+entityClass.getSimpleName()+" t WHERE ";
        int n = attributes.length;
        for (int i = 0; i+1 < n; i++) {
            if (values[i] instanceof String) {
                queryString += "t."+attributes[i]+" LIKE :"+attributes[i].replaceAll("\\.", "_")+" AND ";
            } 
            else if (values[i] == null) {
                queryString += "t."+attributes[i]+" is :"+attributes[i].replaceAll("\\.", "_");
            }
            else {
                queryString += "t."+attributes[i]+" = :"+attributes[i].replaceAll("\\.", "_")+" AND ";
            }
        }
        if (values[n-1] instanceof String) {
            queryString += "t."+attributes[n-1]+" LIKE :"+attributes[n-1].replaceAll("\\.", "_");
        }
        else if (values[n-1] == null) {
            queryString += "t."+attributes[n-1]+" is :"+attributes[n-1].replaceAll("\\.", "_");
        }
        else {
            queryString += "t."+attributes[n-1]+" = :"+attributes[n-1].replaceAll("\\.", "_");
        }
        Query q = em.createQuery(queryString);
        for (int i = 0; i < n; i++) {
            q.setParameter(attributes[i].replaceAll("\\.", "_"), values[i]);
        }
        try {
            return (T) q.getSingleResult();
        } catch(NoResultException | NonUniqueResultException ex) {
            return null;
        }
    }
    
    public List<T> findAll(String[] attributes, Object[] values) {
        
        if (attributes.length == 0) {
            return findAll();
        }
        String queryString = "SELECT t FROM "+entityClass.getSimpleName()+" t WHERE ";
        int n = attributes.length;
        for (int i = 0; i+1 < n; i++) {
            if (values[i] instanceof String) {
                queryString += "t."+attributes[i]+" LIKE :"+attributes[i].replaceAll("\\.", "_")+" AND ";
            }
            else if (values[i] == null) {
                queryString += "t."+attributes[i]+" is :"+attributes[i].replaceAll("\\.", "_");
            }
            else {
                queryString += "t."+attributes[i]+" = :"+attributes[i].replaceAll("\\.", "_")+" AND ";
            }
        }
        if (values[n-1] instanceof String) {
            queryString += "t."+attributes[n-1]+" LIKE :"+attributes[n-1].replaceAll("\\.", "_");
        }     
        else if (values[n-1] == null) {
                queryString += "t."+attributes[n-1]+" is :"+attributes[n-1].replaceAll("\\.", "_");
        }
        else {
            queryString += "t."+attributes[n-1]+" = :"+attributes[n-1].replaceAll("\\.", "_");
        }
        Query q = em.createQuery(queryString);
        for (int i = 0; i < n; i++) {
            q.setParameter(attributes[i].replaceAll("\\.", "_"), values[i]);
        }
        return q.getResultList();
    }

    public T findRefresh(Object id) {
        
        return em.find(entityClass, id);
    }
    
    public T find(Object id) {
        return em.find(entityClass, id);
    }

    public List<T> findAll() {
        
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
