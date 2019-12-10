package com.safran.ses.casablanca.mytex.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.safran.ses.casablanca.mytex.service.model.WBS;

public interface WBSService {

	public void createWbs(WBS wBS);
	
	public List<WBS> getAllWBSs();
	
	public void deleteWbs(WBS wBS);
	
	public void updateWbs(WBS wBS);

	public void updateWbsStatut(WBS wBS);
	
	public void updateWbsVisibility(WBS wBS);
	
	public void updateWbsPresence(WBS wBS);
	
	public List<WBS> getDefaultWBSListe(boolean isDefault);
		 
	
}