package com.safran.ses.casablanca.mytex.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.safran.ses.casablanca.mytex.service.model.JourFerie;

public interface JourFerieService {

	public void createJourFerie(JourFerie jourFerie);
	
	public List<JourFerie> getAllJourFeries();
	
	public void deleteJourFerie(JourFerie jourFerie);
	
	public void updateJourFerie(JourFerie jourFerie);
	
	
}