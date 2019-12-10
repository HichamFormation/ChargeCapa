package com.safran.ses.casablanca.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.InitBinder;

import com.safran.ses.casablanca.mytex.service.SuiviTechniqueService;
import com.safran.ses.casablanca.mytex.service.WBSService;
import com.safran.ses.casablanca.mytex.service.model.SuiviTechnique;
import com.safran.ses.casablanca.mytex.service.model.User;
import com.safran.ses.casablanca.mytex.service.model.WBS;


@Component("SuiviTechniqueManagedBean")
@Scope("request")
public class SuiviTechniqueManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private EntityManagerFactory emf;
	
	private List<SuiviTechnique> suivis;

	private SuiviTechnique suiviTech;
	
	private SuiviTechnique selectedSuiviTech;

	private List<SuiviTechnique> filteredSuiviTech;
	
	@Autowired
	private SuiviTechniqueService suiviTechService;

	@PostConstruct
	public void init(){
		suiviTech = new SuiviTechnique();
		suivis= suiviTechService.getAllSuiviTech();
	}
	
	public List<SuiviTechnique> getFilteredSuiviTech() {
		return filteredSuiviTech;
	}

	public void setFilteredSuiviTech(List<SuiviTechnique> filteredSuiviTech) {
		this.filteredSuiviTech = filteredSuiviTech;
	}

	
	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

	
	public List<SuiviTechnique> getSuivis() {
		return suivis;
	}

	public void setSuivis(List<SuiviTechnique> suivis) {
		this.suivis = suivis;
	}

	
	public SuiviTechnique getSuiviTech() {
		return suiviTech;
	}

	public void setSuiviTech(SuiviTechnique suiviTech) {
		this.suiviTech = suiviTech;
	}


	public SuiviTechnique getSelectedSuiviTech() {
		return selectedSuiviTech;
	}


	public void setSelectedSuiviTech(SuiviTechnique selectedSuiviTech) {
		this.selectedSuiviTech = selectedSuiviTech;
	}


	public SuiviTechniqueService getSuiviTechService() {
		return suiviTechService;
	}


	public void setSuiviTechService(SuiviTechniqueService suiviTechService) {
		this.suiviTechService = suiviTechService;
	}

	public void handleSelectSuivi(SelectEvent event) {
		int selectedIdSuivi = (int) event.getObject();
		for (SuiviTechnique st : suivis) {
			if(selectedIdSuivi == st.getId_suiviTech()){
			filteredSuiviTech.clear();
				filteredSuiviTech.add(st);
				this.suiviTech=st;
				break;
			}
		}
	}
	


}