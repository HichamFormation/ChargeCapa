package com.safran.ses.casablanca.mytex.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.safran.ses.casablanca.mytex.service.HeuresTravailService;
import com.safran.ses.casablanca.mytex.service.WBSService;
import com.safran.ses.casablanca.mytex.service.model.HeuresTravail;
import com.safran.ses.casablanca.mytex.service.model.WBS;

@Component
public class HeuresTravailImpl implements HeuresTravailService {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void createHeuresTravail(HeuresTravail heuresTravail) {
		entityManager.persist(heuresTravail);
	}

	public List<HeuresTravail> getAllHeuresTravail() {
		return entityManager.createQuery("From HeuresTravail", HeuresTravail.class).getResultList();
	}
	
	@Transactional
	 public void deleteHeuresTravail(HeuresTravail heuresTravail) {
		HeuresTravail found = entityManager.find(HeuresTravail.class, heuresTravail.getHeureTravailId());
		entityManager.remove(found);
	}
	
	@Transactional
	public void updateHeuresTravail(HeuresTravail heuresTravail){
		entityManager.merge(heuresTravail);
	}
	
	@Transactional
	public void updateHeureTravailByDefault(HeuresTravail ht){
		HeuresTravail found = entityManager.find(HeuresTravail.class, ht.getHeureTravailId());
		found.setByDefault(ht.isByDefault());
		updateHeuresTravail(found);
	}

	
	
}