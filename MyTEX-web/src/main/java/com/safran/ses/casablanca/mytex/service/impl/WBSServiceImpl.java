package com.safran.ses.casablanca.mytex.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.safran.ses.casablanca.mytex.service.WBSService;
import com.safran.ses.casablanca.mytex.service.model.WBS;

@Component
public class WBSServiceImpl implements WBSService {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public void createWbs(WBS wBS) {
		entityManager.persist(wBS);
	}

	public List<WBS> getAllWBSs() {
		return entityManager.createQuery("From WBS", WBS.class).getResultList();
	}
	
	@Transactional
	 public void deleteWbs(WBS wbs) {
		WBS found = entityManager.find(WBS.class, wbs.getWbsId());
		entityManager.remove(found);
	}
	
	@Transactional
	public void updateWbs(WBS wBS){
		entityManager.merge(wBS);
	}
	@Transactional
	public void updateWbsStatut(WBS wBS){
		WBS found = entityManager.find(WBS.class, wBS.getWbsId());
		found.setActif(wBS.isActif());
		updateWbs(found);
	}
	
	@Transactional
	public void updateWbsVisibility(WBS wBS){
		WBS found = entityManager.find(WBS.class, wBS.getWbsId());
		found.setTakenByDefault(wBS.isTakenByDefault());
		updateWbs(found);
	}

	@Transactional
	public void updateWbsPresence(WBS wBS){
		WBS found = entityManager.find(WBS.class, wBS.getWbsId());
		found.setPresent(wBS.isPresent());
		updateWbs(found);
	}
	

	@Override
	public List<WBS> getDefaultWBSListe(boolean isDefault) {
		TypedQuery<WBS> tQuery= entityManager.createQuery("select w from WBS w where w.takenByDefault=:value",WBS.class);
		tQuery.setParameter("value", isDefault);
		
		return tQuery.getResultList();
	}
	
	
	
	
}