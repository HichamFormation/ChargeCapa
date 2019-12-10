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
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.InitBinder;

import com.safran.ses.casablanca.mytex.service.JourFerieService;
import com.safran.ses.casablanca.mytex.service.WBSService;
import com.safran.ses.casablanca.mytex.service.model.JourFerie;
import com.safran.ses.casablanca.mytex.service.model.WBS;


@Component("JourFerieManagedBean")
@Scope("request")
public class JourFerieManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private EntityManagerFactory emf;


	private JourFerie jourFerie;

	private List<JourFerie> jourFerieList;

	private JourFerie selectedJourFerie;

	private Date date1;
	private Date date2;
	
	

	@Autowired
	private JourFerieService jourFerieService;

	public JourFerie getJourFerie() {
		return jourFerie;
	}

	public void setJourFerie(JourFerie jourFerie) {
		this.jourFerie = jourFerie;
	}

	public List<JourFerie> getJourFerieList() {
		return jourFerieList;
	}

	public void setJourFerieList(List<JourFerie> jourFerieList) {
		this.jourFerieList = jourFerieList;
	}

	public JourFerie getSelectedJourFerie() {
		return selectedJourFerie;
	}

	public void setSelectedJourFerie(JourFerie selectedJourFerie) {
		this.selectedJourFerie = selectedJourFerie;
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

	@PostConstruct
	public void inti(){
		jourFerie = new JourFerie();
		jourFerieList= jourFerieService.getAllJourFeries();
	}


	public void saveJourFerie(){
		boolean found = false;
		for(JourFerie item : jourFerieList){
			if (jourFerie.getOccasion().equalsIgnoreCase(item.getOccasion())
					||
					(jourFerie.getDate_debut().equals(item.getDate_debut()) && jourFerie.getDate_fin().equals(item.getDate_fin()))
					||
					(jourFerie.getDate_debut().compareTo(item.getDate_debut())>0 && jourFerie.getDate_debut().compareTo(item.getDate_fin())<0)
					||
					(jourFerie.getDate_fin().compareTo(item.getDate_fin())<0 && jourFerie.getDate_fin().compareTo(item.getDate_debut())>0)
					){
				found= true;
				break;
			}
		}
		if((jourFerie.getDate_debut()).compareTo(jourFerie.getDate_fin())<0 || (jourFerie.getDate_debut()).compareTo(jourFerie.getDate_fin())==0 ){
			if(!found){
				int p = calculatePeriode(jourFerie.getDate_debut(), jourFerie.getDate_fin());
				jourFerie.setPeriode(p);
				jourFerieService.createJourFerie(jourFerie);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmation d'ajout", "Un nouveau jour férié ("+jourFerie.getOccasion()+") est ajoutée.");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				inti();
			}else{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de duplication", "Ce jour férié existe déjà!");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
			}
		}else{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur ", "La date de fin ne peut pas etre inférieure à la date du début !!");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
		}
		

	}
	


	public void ajouterJourFerie() {
		addMessage1("Validation", "Jour férié ajouté avec succès.");
	}

	/*
	public void onRowEdit(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Jour férié modifié.","L'occasion: "+((JourFerie) event.getObject()).getOccasion());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
*/
	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Modification annulée.","L'occasion: "+ ((JourFerie) event.getObject()).getOccasion());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}


	
	
	public void saveJourFerieEdition(JourFerie jourFerie){
		boolean found = false;
		for (JourFerie item : jourFerieList) {
			if(jourFerie.getId_jrFerie() != item.getId_jrFerie()){
			if (jourFerie.getOccasion().equalsIgnoreCase(item.getOccasion())
					||
					(jourFerie.getDate_debut().equals(item.getDate_debut()) && jourFerie.getDate_fin().equals(item.getDate_fin()))
					||
					(jourFerie.getDate_debut().compareTo(item.getDate_debut())>0 && jourFerie.getDate_debut().compareTo(item.getDate_fin())<0)
					||
					(jourFerie.getDate_fin().compareTo(item.getDate_fin())<0 && jourFerie.getDate_fin().compareTo(item.getDate_debut())>0)
					){
					found= true;
			}
			}
		}if(!found){
			if((jourFerie.getDate_debut()).compareTo(jourFerie.getDate_fin())<0 || (jourFerie.getDate_debut()).compareTo(jourFerie.getDate_fin())==0 ){
				jourFerie.setPeriode(calculatePeriode(jourFerie.getDate_debut(), jourFerie.getDate_fin()));
				jourFerieService.updateJourFerie(jourFerie);
				FacesMessage msg = new FacesMessage("JourFérié modifié.","L'occasion: "+jourFerie.getOccasion());
				FacesContext.getCurrentInstance().addMessage(null, msg);
				inti();
				
			}else{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur ", "La date de fin ne peut pas etre inférieure à la date du début !!");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
			}
			
		}else{
			jourFerie.setOccasion(jourFerie.getOccasion());
			jourFerie.setDate_debut(jourFerie.getDate_debut());
			jourFerie.setDate_fin(jourFerie.getDate_fin());
		
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de duplication", "Le jour férié indiqué existe déjà!");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			FacesMessage msg = new FacesMessage("Modification annulée.","L'occasion: "+ jourFerie.getOccasion());
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
	}

	public void addMessage1(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void deleteJourFerie(JourFerie jourFerie){
		jourFerieService.deleteJourFerie(jourFerie);
		jourFerieList.remove(jourFerie);
		selectedJourFerie =null;
	}
	
	public void click() {
        RequestContext requestContext = RequestContext.getCurrentInstance();
         
        requestContext.update("form:display");
        requestContext.execute("PF('dlg').show()");
    }

	static final long MILISECOND_PER_DAY = 24 * 60 * 60 * 1000;
	
	 public int calculatePeriode(Date debut, Date fin)
	 {
	 int p = (int) ((long) Math.floor(fin.getTime() - debut.getTime()) / MILISECOND_PER_DAY);
	 return p+1;
	 }
}