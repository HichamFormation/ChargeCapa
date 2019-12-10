package com.safran.ses.casablanca.mytex.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.safran.ses.casablanca.mytex.service.SuiviTechniqueService;
import com.safran.ses.casablanca.mytex.service.UserService;
import com.safran.ses.casablanca.mytex.service.model.SuiviTechnique;
import com.safran.ses.casablanca.mytex.service.model.User;

@Component
public class SuiviTechniqueImpl implements SuiviTechniqueService {

	@PersistenceContext
	private EntityManager entityManager;
	
	

	@Transactional
	public void createsuiviTech(SuiviTechnique suivitech) {
		entityManager.persist(suivitech);
		
	}

	@Transactional
	public List<SuiviTechnique> getAllSuiviTech() {
		return entityManager.createQuery("From SuiviTechnique", SuiviTechnique.class).getResultList();
	}
	
}
