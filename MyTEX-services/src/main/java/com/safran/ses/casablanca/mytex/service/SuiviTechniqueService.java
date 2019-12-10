package com.safran.ses.casablanca.mytex.service;

import java.util.List;

import com.safran.ses.casablanca.mytex.service.model.SuiviTechnique;

public interface SuiviTechniqueService {
	
	public void createsuiviTech(SuiviTechnique suivitech);
	
	public List<SuiviTechnique> getAllSuiviTech();

}
