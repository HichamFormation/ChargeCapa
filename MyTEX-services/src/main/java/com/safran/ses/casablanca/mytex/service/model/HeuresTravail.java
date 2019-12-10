package com.safran.ses.casablanca.mytex.service.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="HeuresTravail")
public class HeuresTravail implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int heureTravailId;
	private Date debut;
	private Date fin;
	private int Lundi;
	private int Mardi;
	private int Mercredi;
	private int Jeudi;
	private int Vendredi;
	private int Samedi;
	private int Dimanche;
	private boolean byDefault;
	
	
	public int getHeureTravailId() {
		return heureTravailId;
	}
	public void setHeureTravailId(int heureTravailId) {
		this.heureTravailId = heureTravailId;
	}
	public Date getDebut() {
		return debut;
	}
	public void setDebut(Date debut) {
		this.debut = debut;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public int getLundi() {
		return Lundi;
	}
	public void setLundi(int lundi) {
		Lundi = lundi;
	}
	public int getMardi() {
		return Mardi;
	}
	public void setMardi(int mardi) {
		Mardi = mardi;
	}
	public int getMercredi() {
		return Mercredi;
	}
	public void setMercredi(int mercredi) {
		Mercredi = mercredi;
	}
	public int getJeudi() {
		return Jeudi;
	}
	public void setJeudi(int jeudi) {
		Jeudi = jeudi;
	}
	public int getVendredi() {
		return Vendredi;
	}
	public void setVendredi(int vendredi) {
		Vendredi = vendredi;
	}
	public int getSamedi() {
		return Samedi;
	}
	public void setSamedi(int samedi) {
		Samedi = samedi;
	}
	public int getDimanche() {
		return Dimanche;
	}
	public void setDimanche(int dimanche) {
		Dimanche = dimanche;
	}
	public boolean isByDefault() {
		return byDefault;
	}
	public void setByDefault(boolean byDefault) {
		this.byDefault = byDefault;
	}
	
	
	
	
}



	
	

