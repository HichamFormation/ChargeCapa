package com.safran.ses.casablanca.web.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.safran.ses.casablanca.mytex.service.HeuresTravailService;
import com.safran.ses.casablanca.mytex.service.ImputationService;
import com.safran.ses.casablanca.mytex.service.WBSService;
import com.safran.ses.casablanca.mytex.service.model.HeuresTravail;
import com.safran.ses.casablanca.mytex.service.model.Imputation;
import com.safran.ses.casablanca.mytex.service.model.ImputationLine;
import com.safran.ses.casablanca.mytex.service.model.JourFerie;
import com.safran.ses.casablanca.mytex.service.model.WBS;
import com.safran.ses.casablanca.web.controller.ImputationManagedBean.ImputationColumn;
import com.safran.ses.casablanca.web.controller.ImputationManagedBean.SummaryColumn;
import com.safran.ses.casablanca.web.controller.ImputationManagedBean.SummaryLine;



@Component("heurestravailManagedBean")
@Scope("request")
public class HeuresTravailManagedBean implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private EntityManagerFactory emf;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private HeuresTravail ht;

	private List<HeuresTravail> heuresTravailList ;

	private HeuresTravail selectedHt;

	private Date date1;
	private Date date2;
	
	private List<HeuresTravail> byDefaultHours;
	
	private List<HeuresTravail> personalisedHours;
	
	@Autowired
	private HeuresTravailService htService;

	
	

	public HeuresTravail getHt() {
		return ht;
	}

	public void setHt(HeuresTravail ht) {
		this.ht = ht;
	}

	public List<HeuresTravail> getHeuresTravailList() {
		return heuresTravailList;
	}

	public void setHeuresTravailList(List<HeuresTravail> heuresTravailList) {
		this.heuresTravailList = heuresTravailList;
	}

	public HeuresTravail getSelectedHt() {
		return selectedHt;
	}

	public void setSelectedHt(HeuresTravail selectedHt) {
		this.selectedHt = selectedHt;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public List<HeuresTravail> getByDefaultHours() {
		return byDefaultHours;
	}

	public void setByDefaultHours(List<HeuresTravail> byDefaultHours) {
		this.byDefaultHours = byDefaultHours;
	}
	
	
	public List<HeuresTravail> getPersonalisedHours() {
		return personalisedHours;
	}

	public void setPersonalisedHours(List<HeuresTravail> personalisedHours) {
		personalisedHours = personalisedHours;
	}

	@PostConstruct
	public void inti(){
		ht = new HeuresTravail();
		heuresTravailList= htService.getAllHeuresTravail();
		byDefaultHours = new ArrayList<HeuresTravail>();
		personalisedHours = new ArrayList<HeuresTravail>();
		
		for(HeuresTravail item : heuresTravailList){
			if(item.isByDefault()){
				byDefaultHours.add(item);
			}else{
				personalisedHours.add(item);
			}
		}
	}
	
	
	public void saveHeuresTravail(){
		boolean found = false;
		for(HeuresTravail item : heuresTravailList){
			
			if(ht.getDebut().equals(item.getDebut()) && ht.getFin().equals(item.getFin())
				||
				(ht.getDebut().compareTo(item.getDebut())>0 && ht.getDebut().compareTo(item.getFin())<0)
				||
				(ht.getFin().compareTo(item.getFin())<0 && ht.getFin().compareTo(item.getDebut())>0)
					){ 
				found= true;
				break;
			}
		}
		if((ht.getDebut()).compareTo(ht.getFin())<0 || (ht.getDebut()).compareTo(ht.getFin())==0 ){
			if(!found){
				htService.createHeuresTravail(ht);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmation d'ajout", "La période de "+ht.getDebut() + "à " + ht.getFin() +"est bien paramétrée.");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				inti();
			}else{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de duplication", "La période paramétrée existe déjà ou est inclus dans une autre période !");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
			}
		}else{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur ", "La date de fin ne peut pas etre inférieure à la date du début !!");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
		}
	}
	
	public void ajouterHeuresTravail() {
		addMessage1("Validation", "Période paramétrée avec succès.");
	}
	
	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Modification annulée.","La période de "+ ((HeuresTravail) event.getObject()).getDebut() + " à " + ((HeuresTravail) event.getObject()).getFin()) ;
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void saveHeuresTravailEdition(HeuresTravail heureT){
		boolean found = false;
		for (HeuresTravail item : heuresTravailList) {
			if(heureT.getHeureTravailId() != item.getHeureTravailId()){
				if(heureT.getDebut().equals(item.getDebut()) && heureT.getFin().equals(item.getFin())
						||
						(heureT.getDebut().compareTo(item.getDebut())>0 && heureT.getDebut().compareTo(item.getFin())<0)
						||
						(heureT.getFin().compareTo(item.getFin())<0 && heureT.getFin().compareTo(item.getDebut())>0)
							){ 
					found= true;
			}
			}
		}if(!found){
			if((heureT.getDebut()).compareTo(heureT.getFin())<0 || (heureT.getDebut()).compareTo(heureT.getFin())==0 ){
				htService.updateHeuresTravail(heureT);
				FacesMessage msg = new FacesMessage("Paramètres modifiés avec succès.", "La période de "+ heureT.getDebut() + " à " + heureT.getFin());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				inti();
				
			}else{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur ", "La date de fin ne peut pas etre inférieure à la date du début !!");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
			}
			
		}else{
			heureT.setDebut(heureT.getDebut());
			heureT.setFin(heureT.getFin());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de duplication", "La période paramétrée existe déjà ou est inclus dans une autre période !");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			FacesMessage msg = new FacesMessage("Modification annulée.","La période de "+ heureT.getDebut() + " à " + heureT.getFin());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	public void addMessage1(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void deleteHeuresTravail(HeuresTravail heureT){
		htService.deleteHeuresTravail(heureT);
		personalisedHours.remove(heureT);
		selectedHt =null;
	}
	
	
	public void updateHeureTravailByDefault(HeuresTravail ht){
		htService.updateHeureTravailByDefault(ht);
		selectedHt=ht;
		addByDefaultMessage(ht);
	}
	
	
	public void addByDefaultMessage(HeuresTravail ht) {
		String summary = "Les paramètres de la période de "+ht.getDebut()+ "à " + ht.getFin() + " sont " + (ht.isByDefault() ? "par défaut" : "personnalisés");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Infos",summary));
	}
	
	
	
}
