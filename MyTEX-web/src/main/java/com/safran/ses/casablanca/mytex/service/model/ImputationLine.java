package com.safran.ses.casablanca.mytex.service.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="IMPUTATION_LINE")
public class ImputationLine implements Serializable{

	private static final long serialVersionUID = -4027595761224701918L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long imputationLineId;
	
	@ManyToOne
	@JoinColumn(name="WBS_ID",nullable=false)
	private WBS wbs;
	
	private int week;
	
	private int year;
	
	@ManyToOne
	@JoinColumn(name="CUST_ID",nullable=false)
	private User user;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="imputationLine", fetch=FetchType.LAZY)
	private Set<Imputation> imputations;

	public long getImputationLineId() {
		return imputationLineId;
	}

	public void setImputationLineId(long imputationLineId) {
		this.imputationLineId = imputationLineId;
	}

	public WBS getWbs() {
		return wbs;
	}

	public void setWbs(WBS wbs) {
		this.wbs = wbs;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Imputation> getImputations() {
		return imputations;
	}

	public void setImputations(Set<Imputation> imputations) {
		this.imputations = imputations;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	
}
