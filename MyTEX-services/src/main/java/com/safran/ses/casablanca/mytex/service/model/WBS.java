 package com.safran.ses.casablanca.mytex.service.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="WBS")
public class WBS implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_wbs")
	private int wbsId;
	private String code;
	private String name;
	private boolean actif;
	private boolean present;
	
	@Column(name="byDefault",nullable=false)
	private boolean takenByDefault;
	
	public boolean isPresent() {
		return present;
	}
	public void setPresent(boolean present) {
		this.present = present;
	}
	public int getWbsId() {
		return wbsId;
	}
	public void setWbsId(int wbsId) {
		this.wbsId = wbsId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActif() {
		return actif;
	}
	public void setActif(boolean actif) {
		this.actif = actif;
	}
	public boolean isTakenByDefault() {
		return takenByDefault;
	}
	public void setTakenByDefault(boolean takenByDefault) {
		this.takenByDefault = takenByDefault;
	}
}