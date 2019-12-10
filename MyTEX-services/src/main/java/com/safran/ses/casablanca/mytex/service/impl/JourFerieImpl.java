package com.safran.ses.casablanca.mytex.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.safran.ses.casablanca.mytex.service.JourFerieService;
import com.safran.ses.casablanca.mytex.service.model.JourFerie;

@Component
public class JourFerieImpl implements JourFerieService {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void createJourFerie(JourFerie jourFerie) {
		entityManager.persist(jourFerie);
	}

	public List<JourFerie> getAllJourFeries() {
		return entityManager.createQuery("From JourFerie", JourFerie.class).getResultList();
	}
	
	@Transactional
	 public void deleteJourFerie(JourFerie jourFerie) {
		JourFerie found = entityManager.find(JourFerie.class, jourFerie.getId_jrFerie());
		entityManager.remove(found);
	}
	
	@Transactional
	public void updateJourFerie(JourFerie jourFerie){
		entityManager.merge(jourFerie);
	}
	
}