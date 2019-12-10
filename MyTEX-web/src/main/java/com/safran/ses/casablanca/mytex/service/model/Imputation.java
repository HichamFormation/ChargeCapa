package com.safran.ses.casablanca.mytex.service.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="IMPUTATION")
public class Imputation implements Serializable {

	private static final long serialVersionUID = 5100308311688166120L;
	
	
	
	
	public Imputation() {
	}

	public Imputation(long imputationId, Date date, int houres,  Location location) {
		super();
		this.imputationId = imputationId;
		this.date = date;
		this.houres = houres;
		this.location = location;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long imputationId;
	
	private Date date;
	
	private int houres;
	
	
	
	@ManyToOne
	@JoinColumn(name="LOC_ID",nullable=false)
	private Location location;
	
	
	@ManyToOne
	@JoinColumn(name="IMPUTATION_LINE_ID", nullable=false)
	private ImputationLine imputationLine;

	public long getImputationId() {
		return imputationId;
	}

	public void setImputationId(long imputationId) {
		this.imputationId = imputationId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getHoures() {
		return houres;
	}

	public void setHoures(int houres) {
		this.houres = houres;
	}

	public ImputationLine getImputationLine() {
		return imputationLine;
	}

	public void setImputationLine(ImputationLine imputationLine) {
		this.imputationLine = imputationLine;
	}
	
}
