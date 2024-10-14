package com.ndp.service;


import com.ndp.entity.syncer.Business;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class BusinessService {

    @Inject
    EntityManager entityManager;

    @Inject
    Logger logger;

    @Transactional
    public Business saveOrUpdate(Business business) {
        Business existingBusiness = findByCompanyAndBusiness(business.getCompany(), business.getBusiness());
        if (existingBusiness != null) {
            existingBusiness.setBusiness(business.getBusiness());
            existingBusiness.setPath(business.getPath());
            existingBusiness.setUser(business.getUser());
            existingBusiness.setPass(business.getPass());
            existingBusiness.setCompanyBD(business.getCompanyBD());
            return existingBusiness;
        } else {
            entityManager.persist(business);
            return business;
        }
    }


    @Transactional
public Business findByCompanyAndBusiness(String company, String business) {
    try {
        return entityManager.createQuery("SELECT b FROM Business b WHERE b.company = :company and b.business = :business", Business.class)
                .setParameter("company", company)
                .setParameter("business", business)
                .getSingleResult();
    } catch (jakarta.persistence.NoResultException e) {
        logger.warn("No result found for company: " + company + " and business: " + business);
        return null;
    } catch (Exception e) {
        logger.error("Error finding business by company: " + company, e);
        return null;
    }
}

    public Business findByPath(String path) {
        try {
            return entityManager.createQuery("SELECT b FROM Business b WHERE b.path = :path", Business.class)
                    .setParameter("path", path)
                    .getSingleResult();
        } catch (Exception e) {
            logger.error("Error finding business by path: " + path, e);
            return null;
        }
    }

    public List<Business> getAllBusinesses(int page, int size) {
        int firstResult = (page - 1) * size;
        return entityManager.createQuery("SELECT b FROM Business b", Business.class)
                .setFirstResult(firstResult)
                .setMaxResults(size)
                .getResultList();
    }
}
