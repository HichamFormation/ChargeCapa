package com.safran.ses.casablanca.web.controller;

import java.io.Serializable;
import java.sql.SQLException;
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

import org.apache.log4j.Logger;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.InitBinder;






import com.safran.ses.casablanca.mytex.service.WBSService;
import com.safran.ses.casablanca.mytex.service.model.WBS;


@Component("WbsManagedBean")
@Scope("request")
public class WbsManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private EntityManagerFactory emf;
	
	Logger LOGGER = Logger.getLogger(WbsManagedBean.class);

	private WBS wBS;

	private List<WBS> wbsList;

	private WBS selectedWBS;

	private boolean value2;

	private String message;

	@Autowired
	private WBSService wBSService;


	public WBS getWbs() {
		return wBS;
	}

	public void setWbs(WBS wBS) {
		this.wBS = wBS;
	}

	public List<WBS> getWbsList() {
		return wbsList;
	}

	public void setWbsList(List<WBS> wbsList) {
		this.wbsList = wbsList;
	}

	public WBS getSelectedWBS() {
		return selectedWBS;
	}

	public void setSelectedWBS(WBS selectedWBS) {
		this.selectedWBS = selectedWBS;
	}


	@PostConstruct
	public void inti(){
		wBS = new WBS();
		wbsList= wBSService.getAllWBSs();
	}
	


	public String sayHello(){
		return "Hello from Younes KHALITI";
	}

	public void saveWBS(){
		boolean found = false;
		for(WBS item : wbsList){
			if (wBS.getCode().equalsIgnoreCase(item.getCode())){
				found= true;
				break;
			}
		}
		if(!found){
			wBSService.createWbs(wBS);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmation d'ajout", "Une nouvelle affaire ("+wBS.getCode()+"//"+wBS.getName()+") est ajoutée");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			inti();
		}else{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de duplication", "Un WBS ayant le même code existe déjà!");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
		}

	}



	public void ajouterWbs() {
		addMessage1("Validation", "WBS ajouté avec succès.");
	}

	/*public void onRowEdit(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("WBS modifié.","WBS: "+((Wbs) event.getObject()).getWbs_txt());
		FacesContext.getCurrentInstance().addMessage(null, msg);
		 
	}
	*/

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Modification annulée.","WBS: "+ ((WBS) event.getObject()).getCode());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void saveWBSEdition(WBS wBS){
		boolean found = false;
		for(WBS item : wbsList){
			if(wBS.getWbsId() != item.getWbsId()){
			if (wBS.getCode().equalsIgnoreCase(item.getCode())){
					found= true;
			}
			}
		}if(!found){
			wBSService.updateWbs(wBS);
			FacesMessage msg = new FacesMessage("WBS modifié.","WBS: "+wBS.getCode());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			inti();

		}else{
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de duplication", "Un WBS ayant le même code existe déjà!");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			FacesMessage msg = new FacesMessage("Modification annulée.","WBS: "+ wBS.getCode());
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
	}
	
	public void addMessage1(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public boolean isValue2() {
		return value2;
	}

	public void setValue2(boolean value2) {
		this.value2 = value2;
	}

	public void addStatutMessage(WBS wBS) {
		String summary = "L\'affaire "+wBS.getCode()+" // "+wBS.getName()+" est "+  (wBS.isActif()  ? "Active" : "Inactive");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Infos",summary));
	}
	
	public void addVisibilityMessage(WBS wBS) {
		String summary = "L\'affaire "+wBS.getCode()+" // "+wBS.getName()+" est "+  (wBS.isTakenByDefault()  ? "Visible" : "Invisible");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Infos",summary));
	}
	
	public void addPresenceMessage(WBS wBS) {
		String summary = "L\'affaire "+wBS.getCode()+" // "+wBS.getName()+" est "+  (wBS.isPresent()  ? "Avec présence" : "Sans présence");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Infos",summary));
	}
/*
	public void deleteWBS(WBS wBS){
		wBSService.deleteWbs(wBS);
		wbsList.remove(wBS);
		selectedWBS =null;
	}
	*/
	
	public void deleteWBS(WBS wBS) {
		try{
			wBSService.deleteWbs(wBS);
			wbsList.remove(wBS);
		}catch(Exception e){
			String message = "Impossible de supprimer ce WBS: ";
			if(e instanceof DataIntegrityViolationException){
				message+="Une ou plusieurs ressources a/ont déjà imputé sur ce WBS.";
			}else{
				message+="Merci de contacter l'administrateur";
			}
			
			
			    FacesContext.getCurrentInstance().addMessage(null, 
			        new FacesMessage(FacesMessage.SEVERITY_FATAL, message, null));
			    
			    LOGGER.error("Impossible de supprimer WBS: "+wBS.getCode(),e);
			
		}
		selectedWBS =null;
	}
	
	public void updateWbsStatut(WBS wBS){
		wBSService.updateWbsStatut(wBS);
		selectedWBS=wBS;
		addStatutMessage(wBS);
	}
	
	public void updateWbsVisibility(WBS wBS){
		wBSService.updateWbsVisibility(wBS);
		selectedWBS=wBS;
		addVisibilityMessage(wBS);
	}
	
	public void updateWbsPresence(WBS wBS){
		wBSService.updateWbsPresence(wBS);
		selectedWBS=wBS;
		addPresenceMessage(wBS);
	}

}