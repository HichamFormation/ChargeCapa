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



import com.safran.ses.casablanca.mytex.service.UserService;
import com.safran.ses.casablanca.mytex.service.model.User;


@Component("UserManagedBean")
@Scope("request")
public class UserManagedBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private EntityManagerFactory emf;
	
	Logger LOGGER = Logger.getLogger(WbsManagedBean.class);

	private User user;

	private List<User> userList;

	private User selectedUser;

	private boolean value2;

	private String message;

	@Autowired
	private UserService userService;


	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public List<User> getUserList() {
		return userList;
	}



	public void setUserList(List<User> userList) {
		this.userList = userList;
	}



	public User getSelectedUser() {
		return selectedUser;
	}



	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}



	@PostConstruct
	public void inti(){
		user = new User();
		userList= userService.getAllUsers();
	}
	


	public String sayHello(){
		return "Hello from Younes KHALITI";
	}

	public void saveUser(){
		boolean found = false;
		for(User item : userList){
			if (user.getMatricule().equalsIgnoreCase(item.getMatricule())){
				found= true;
				break;
			}
		}
		if(!found){
			userService.createUser(user);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirmation d'ajout", "le nuveau utilisateur "+user.getLastName()+" "+user.getFirstName()+" est ajouté avec succès.");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			inti();
		}else{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de duplication", "Un user ayant la même matricule existe déjà!");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
		}

	}



	public void ajouterUser() {
		addMessage1("Validation", "Utilisateur ajouté avec succès.");
	}

	/*public void onRowEdit(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("WBS modifié.","WBS: "+((Wbs) event.getObject()).getWbs_txt());
		FacesContext.getCurrentInstance().addMessage(null, msg);
		 
	}
	*/

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Modification annulée.","L'utilisateur : " + ((User) event.getObject()).getMatricule());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void saveUserEdition(User user){
		boolean found = false;
		for(User item : userList){
			if(user.getUserId()!= item.getUserId()){
			if (user.getMatricule().equalsIgnoreCase(item.getMatricule())){
					found= true;
			}
			}
		}if(!found){
			userService.updateUser(user);
			FacesMessage msg = new FacesMessage("Utilisateur modifié.","L'utilisateur : " +user.getMatricule());
			FacesContext.getCurrentInstance().addMessage(null, msg);
			inti();

		}else{
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur de duplication", "Un utilisateur ayant la même matricule existe déjà!");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
			FacesMessage msg = new FacesMessage("Modification annulée.","L'utilisateur: "+ user.getMatricule());
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
	}
	
	public void addMessage1(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}


	public void addStatutMessage(User user) {
		String summary = "L'utilisateur " +user.getFirstName() +" " +user.getLastName() +" est "+  (user.isActif()  ? "Active" : "Inactive");
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Infos",summary));
	}
	

/*
	public void deleteWBS(WBS wBS){
		wBSService.deleteWbs(wBS);
		wbsList.remove(wBS);
		selectedWBS =null;
	}
	*/
	
	public void deleteUser(User user) {
		try{
			userService.deleteUser(user);
			userList.remove(user);
		}catch(Exception e){
			String message = "Impossible de supprimer cet utilisateur : ";
			if(e instanceof DataIntegrityViolationException){
				message+=" ";
			}else{
				message+="Merci de contacter l'administrateur";
			}
			
			
			    FacesContext.getCurrentInstance().addMessage(null, 
			        new FacesMessage(FacesMessage.SEVERITY_FATAL, message, null));
			    
			    LOGGER.error("Impossible de supprimer cet utilisateur: "+user.getMatricule(),e);
			
		}
		selectedUser=null;
	}
	
	public void updateUserStatut(User user){
		userService.updateUserStatut(user);
		selectedUser=user;
		addStatutMessage(user);
	}
	
	

}