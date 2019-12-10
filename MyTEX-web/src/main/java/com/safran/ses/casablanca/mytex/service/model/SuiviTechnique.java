 package com.safran.ses.casablanca.mytex.service.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="SuiviTechnique")
public class SuiviTechnique implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private int id_suiviTech;
	
	private Date actionDate;
	
	private int numero;
	private String PB;
	private String TS;
	private String taches;
	private float heureRef;
	private float heureRealisation;
	private float RAF;
	private float avancement;
	private Date date;
	private String statut;
	
	public int getId_suiviTech() {
		return id_suiviTech;
	}
	public void setId_suiviTech(int id_suiviTech) {
		this.id_suiviTech = id_suiviTech;
	}
	public Date getActionDate() {
		return actionDate;
	}
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public String getPB() {
		return PB;
	}
	public void setPB(String pB) {
		PB = pB;
	}
	public String getTS() {
		return TS;
	}
	public void setTS(String tS) {
		TS = tS;
	}
	public String getTaches() {
		return taches;
	}
	public void setTaches(String taches) {
		this.taches = taches;
	}
	public float getHeureRef() {
		return heureRef;
	}
	public void setHeureRef(float heureRef) {
		this.heureRef = heureRef;
	}
	public float getHeureRealisation() {
		return heureRealisation;
	}
	public void setHeureRealisation(float heureRealisation) {
		this.heureRealisation = heureRealisation;
	}
	public float getRAF() {
		return RAF;
	}
	public void setRAF(float rAF) {
		RAF = rAF;
	}
	public float getAvancement() {
		return avancement;
	}
	public void setAvancement(float avancement) {
		this.avancement = avancement;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	}
	
	
	
	
