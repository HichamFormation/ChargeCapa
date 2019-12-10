package com.safran.ses.casablanca.web.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.CellEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.safran.ses.casablanca.mytex.service.ImputationService;
import com.safran.ses.casablanca.mytex.service.WBSService;
import com.safran.ses.casablanca.mytex.service.model.Imputation;
import com.safran.ses.casablanca.mytex.service.model.ImputationLine;
import com.safran.ses.casablanca.mytex.service.model.Location;
import com.safran.ses.casablanca.mytex.service.model.User;
import com.safran.ses.casablanca.mytex.service.model.WBS;


@Component("imputationManagedBean")
@Scope("view")
public class ImputationManagedBean implements Serializable{

	private static final long serialVersionUID = 5752225998744678564L;
	private String enteredImputation;
	private Date periodeStartDate;
	private Date periodeEndDate;
	private List<Imputation> imputations = new ArrayList<Imputation>();
	private List<WBS> defaultWBSListe = new ArrayList<WBS>();
	private List<WBS> selectableWBSListe = new ArrayList<WBS>();
	private List<ImputationLine> imputationLines;
	private Imputation imputation;
	private List<ImputationColumn> columns;
	private List<ImputationColumn> allColumns;
	private List<SummaryLine> summaryLines;
	private List<SummaryColumn> summaryColumns;

	private WBS selectedWBS;

	private int week;

	private int year;

	private User user;

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Autowired
	private ImputationService imputationService;

	@Autowired
	private WBSService wbsService;

	private Location userLocation = new Location();

	@PostConstruct
	public void init(){
		user = new User();
		user.setUserId(1);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		week = calendar.get(Calendar.WEEK_OF_YEAR);
		year = calendar.getWeekYear(); 


		imputationLines = imputationService.getImputationLinesForPeriode(1, week, year);

		if(null!=imputationLines && imputationLines.size()>0){
			for (ImputationLine imputationLine : imputationLines) {
				if(imputationLine.getWbs().isActif()){
					imputations.addAll(imputationLine.getImputations());
				}
				

			}
		}

		defaultWBSListe = wbsService.getDefaultWBSListe(true);
		selectableWBSListe = wbsService.getDefaultWBSListe(false);

		userLocation.setLocationId(1);
		userLocation.setLocationName("location1");




		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		periodeStartDate = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, 6);
		periodeEndDate = calendar.getTime();
		columns = new ArrayList<ImputationColumn>(); 
		allColumns = new ArrayList<ImputationColumn>();

		//if imputationLines is null
		//buildImputationLines();
		/*
		if(null==imputationLines || imputationLines.isEmpty()){
			buildImputationLines();
		}*/

		buildColumnsForDate(new Date());

		summaryLines=calculateSummaryLines();
		summaryColumns= calculateSummaryColumns();
	}

	public String calculateCellImputationValue(String headerDate, ImputationLine line){

		for (Imputation imputation : line.getImputations()) {
			if(headerDate.equals(sdf.format(imputation.getDate()))){
				return ""+imputation.getHoures();
			}
		}
		return "";
	}

	public void affectImputation(String dateValue,ImputationLine imputationLine){

		for (Imputation imputation : imputationLine.getImputations()) {
			if(dateValue.equals(sdf.format(imputation.getDate()))){
				break;
			}
		}
	}

	public void buildColumnsForDate(Date date){

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		week = calendar.get(Calendar.WEEK_OF_YEAR);

		year = calendar.getWeekYear();


		List<ImputationLine> periodeImputationLines = new ArrayList<ImputationLine>();
		for (WBS wbs : defaultWBSListe) {
			if(!doesLineExist(wbs.getWbsId(), week, year)){
				ImputationLine line = new ImputationLine();
				line.setUser(user);
				line.setWeek(week);
				line.setYear(year);
				line.setImputations(new HashSet<Imputation>());
				line.setWbs(wbs);
				imputationService.addImputationLine(line);
				imputationLines.add(line);
				periodeImputationLines.add(line);
			}


		}


		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		periodeStartDate = calendar.getTime();


		calendar.add(Calendar.DAY_OF_MONTH, -1);

		for(int i= 0; i< 7 ; i++){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			Date currentDate = calendar.getTime();

			List<Imputation> columImputations = new ArrayList<Imputation>();

			boolean aleadyImputed = false;

			for (Imputation imput : imputations) {
				if(sdf.format(imput.getDate()).equals(sdf.format(currentDate))){
					columImputations.add(imput);
					aleadyImputed = true;
					break;
				}
			}

			if(!aleadyImputed){


				for (ImputationLine imputLine : periodeImputationLines) {

					Imputation imputation = new Imputation();
					imputation.setDate(currentDate);
					imputation.setHoures(0);
					imputation.setLocation(userLocation);
					imputation.setImputationLine(imputLine);
					imputationService.addImputation(imputation);

					imputLine.getImputations().add(imputation);
					columImputations.add(imputation);

					imputations.add(imputation);
				}
			}

			ImputationColumn imputationColumn = new ImputationColumn(currentDate,columImputations);
			columns.add(imputationColumn);
		}

		allColumns.addAll(columns);

		Collections.sort(allColumns, new Comparator<ImputationColumn>() {

			@Override
			public int compare(ImputationColumn o1, ImputationColumn o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
	}


	public List<Imputation> buildImputationsForLineAndDate(ImputationLine line, Date date){

		List<Imputation> lineImputations = new ArrayList<Imputation>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int numberOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),0);

		for(int i= 1; i<=numberOfDays ; i++){
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			Date currentDate = calendar.getTime();
			Imputation newImputation = new Imputation();
			newImputation.setImputationLine(line);
			newImputation.setDate(currentDate);
			newImputation.setHoures(0);
			newImputation.setLocation(userLocation);
			imputations.add(newImputation);
			lineImputations.add(newImputation);
		}
		return lineImputations;
	}



	public Imputation showImputationValue(ImputationLine line, Date columnDate){
		if(null==columnDate||null==line){
			return null;
		}
		for (Imputation	 imputation : line.getImputations()) {
			if(sdf.format(columnDate).equals(sdf.format(imputation.getDate()))){
				return imputation;
			}
		}
		return null;
	}
	/*
	public String showStatisticsValue(SummaryLine line, Date columnDate){
		if(null==columnDate||null==line){
			return null;
		}
		for (SummaryColumn	 col : line.getColumns()) {
			if(sdf.format(columnDate).equals(sdf.format(col.getDate()))){
				return ""+col.getValue();
			}
		}
		return null;
	}*/

	public void onCellEdit(CellEditEvent event) {
		/*

		String oldValue = (String) event.getOldValue();
        String newValue = (String) event.getNewValue();

        if(newValue != null && !newValue.equals(oldValue)) {imputationService.updateImputations(imputationLines.get(0)) init()

        }*/

		for (ImputationLine line : imputationLines) {
			imputationService.updateImputations(line);
		}
	}

	public void onWBSChange(AjaxBehaviorEvent  event){
		String stringwbsId= (String) ((SelectOneMenu)event.getSource()).getValue();
		WBS selectedWbs= null;
		int wbsId =Integer.parseInt(stringwbsId);
		for (WBS wbs : selectableWBSListe) {
			if(wbsId==wbs.getWbsId()){
				selectedWbs= wbs;
				break;
			}
		}
		if(!doesLineExist(wbsId, week, year)){
			ImputationLine line = new ImputationLine();
			line.setUser(user);
			line.setWeek(week);
			line.setYear(year);
			line.setImputations(new HashSet<Imputation>());
			line.setWbs(selectedWbs);
			imputationService.addImputationLine(line);
			imputationLines.add(line);

			Calendar cal = Calendar.getInstance();
			cal.setTime(periodeStartDate);

			cal.add(Calendar.DAY_OF_MONTH, -1);

			for(int i= 0; i< 7 ; i++){

				cal.add(Calendar.DAY_OF_MONTH, 1);
				Date currentDate = cal.getTime();

				Imputation imputation = new Imputation();
				imputation.setDate(currentDate);
				imputation.setHoures(0);
				imputation.setLocation(userLocation);
				imputation.setImputationLine(line);
				imputationService.addImputation(imputation);
				line.getImputations().add(imputation);
				imputations.add(imputation);
			}
		}
	}


	public List<ImputationColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<ImputationColumn> columns) {
		this.columns = columns;
	}

	public String getEnteredImputation() {
		return enteredImputation;
	}
	public void setEnteredImputation(String enteredImputation) {
		this.enteredImputation = enteredImputation;
	}
	public List<Imputation> getImputations() {
		return imputations;
	}
	public void setImputations(List<Imputation> imputations) {
		this.imputations = imputations;
	}
	public Imputation getImputation() {
		return imputation;
	}
	public void setImputation(Imputation imputation) {
		this.imputation = imputation;
	}
	public List<ImputationLine> getImputationLines() {
		return imputationLines;
	}
	public void setImputationLines(List<ImputationLine> imputationLines) {
		this.imputationLines = imputationLines;
	}
	public Date getPeriodeStartDate() {
		return periodeStartDate;
	}
	public void setPeriodeStartDate(Date periodeStartDate) {
		this.periodeStartDate = periodeStartDate;
	}
	public Date getPeriodeEndDate() {
		return periodeEndDate;
	}
	public void setPeriodeEndDate(Date periodeEndDate) {
		this.periodeEndDate = periodeEndDate;
	}

	public List<ImputationColumn> getAllColumns() {
		return allColumns;
	}

	public void setAllColumns(List<ImputationColumn> allColumns) {
		this.allColumns = allColumns;
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


	public List<WBS> getSelectableWBSListe() {
		return selectableWBSListe;
	}

	public void setSelectableWBSListe(List<WBS> selectableWBSListe) {
		this.selectableWBSListe = selectableWBSListe;
	}


	public WBS getSelectedWBS() {
		return selectedWBS;
	}

	public void setSelectedWBS(WBS selectedWBS) {
		this.selectedWBS = selectedWBS;
	}

	public List<SummaryLine> getSummaryLines() {
		return summaryLines;
	}

	public void setSummaryLines(List<SummaryLine> SummaryLines) {
		this.summaryLines = SummaryLines;
	}



	public List<SummaryColumn> getSummaryColumns() {
		return summaryColumns;
	}

	public void setSummaryColumns(List<SummaryColumn> summaryColumns) {
		this.summaryColumns = summaryColumns;
	}

	public void getNextPeriode(){



		columns.clear();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(periodeEndDate);

		calendar.add(Calendar.DAY_OF_MONTH, 1);
		periodeStartDate = calendar.getTime();

		week = calendar.get(Calendar.WEEK_OF_YEAR);
		year = calendar.getWeekYear(); 
		imputationLines = imputationService.getImputationLinesForPeriode(1, week, year);
		calendar.add(Calendar.DAY_OF_MONTH, 6);
		periodeEndDate = calendar.getTime();

		for (ImputationColumn column : allColumns) {
			if(comparteDates(periodeStartDate, column.getDate(), periodeEndDate)>=0){
				columns.add(column);
			}
		}
		if(columns.isEmpty()){
			buildColumnsForDate(periodeStartDate);
		}

		summaryColumns= calculateSummaryColumns();
	}

	public void getPreviousPeriode(){

		columns.clear();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(periodeStartDate);

		calendar.add(Calendar.DAY_OF_MONTH, -1);
		periodeEndDate = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -6);
		periodeStartDate = calendar.getTime();

		week = calendar.get(Calendar.WEEK_OF_YEAR);
		year = calendar.getWeekYear(); 
		imputationLines = imputationService.getImputationLinesForPeriode(1, week, year);
		for (ImputationColumn column : allColumns) {
			if(comparteDates(periodeStartDate, column.getDate(), periodeEndDate)>=0){
				columns.add(column);
			}
		}
		if(columns.isEmpty()){
			buildColumnsForDate(periodeStartDate);
		}
		summaryColumns= calculateSummaryColumns();
	}

	private boolean doesLineExist(int wbsId, int week, int year){
		for (ImputationLine line : imputationLines) {
			if(line.getWbs().getWbsId()==wbsId && line.getWeek()==week && line.getYear()==year){
				return true;
			}

		}

		return false;
	}


	private int comparteDates(Date startDate, Date dateToCheck, Date endDate){
		try{
			return sdf.parse(sdf.format(startDate)).compareTo(sdf.parse(sdf.format(dateToCheck))) * sdf.parse(sdf.format(dateToCheck)).compareTo(sdf.parse(sdf.format(endDate)));
		}catch (Exception e) {
			return 0;
		}
	}

	public String calculateStyle(ImputationColumn column){
		Calendar cal = Calendar.getInstance();
		cal.setTime(column.getDate());
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek==1|| dayOfWeek ==7){
			return "background-color:#F0F0F0;";
		}
		return "";
	}

	public String calculateSummaryStyle(SummaryColumn column){

		if(column.getSum()==column.getNorm()){
			return "medRow";
		}else if(column.getSum()>column.getNorm()){
			return "upRow";
		}else {
			return "downRow";
		}
	}

	public List<SummaryLine> calculateSummaryLines(){

		ArrayList<SummaryLine> statisticsList = new ArrayList<SummaryLine>();

		SummaryLine sumLine = new SummaryLine();
		sumLine.setRowId(1);
		sumLine.setProperty("sum");
		sumLine.setRowTitle("Cumul");

		SummaryLine regLine = new SummaryLine();
		regLine.setRowId(2);
		regLine.setProperty("norm");
		regLine.setRowTitle("Présence prévue");

		SummaryLine regLineReel = new SummaryLine();
		regLineReel.setRowId(3);
		regLineReel.setProperty("normReel");
		regLineReel.setRowTitle("Présence réelle");

		
		SummaryLine supLine = new SummaryLine();
		supLine.setProperty("sup");
		supLine.setRowId(4);
		supLine.setRowTitle("Heures Supp");

		

		statisticsList.add(supLine);
		statisticsList.add(regLine);
		statisticsList.add(sumLine);
		statisticsList.add(regLineReel);


		Collections.sort(statisticsList, new Comparator<SummaryLine>() {

			@Override
			public int compare(SummaryLine o1, SummaryLine o2) {
				if(o1.getRowId()<o2.getRowId()){
					return -1;
				}else if(o1.getRowId()>o2.getRowId()){
					return 1;
				}else{
					return 0;
				}
			}
		});

		return statisticsList;

	}

	public List<SummaryColumn> calculateSummaryColumns(){
		List<SummaryColumn> summaryColumns = new ArrayList<SummaryColumn>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(periodeStartDate);

		for(int i= 0; i< 7 ; i++){
			Date currentDate = calendar.getTime();
			int columnSum = 0;	//cumul
			int normalHoures=9;	//presence prévue
			int heuresPresReel=0;	//presence reelle
			int supHoures =0;	//heures superieures
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek==1|| dayOfWeek ==7){
				normalHoures = 0;
			}else if(dayOfWeek==6){
				normalHoures =8;
			}
			for (ImputationLine line : imputationLines) {
				for (Imputation imputation : line.getImputations()) {
					if(sdf.format(currentDate).equals(sdf.format(imputation.getDate()))){
							columnSum+=imputation.getHoures();
						 if(line.getWbs().isPresent()){
								heuresPresReel+=imputation.getHoures();
							}
					}
				}
			}

			if(heuresPresReel>normalHoures){
				supHoures = heuresPresReel - normalHoures;
			}
			
			

			SummaryColumn col = new SummaryColumn();
			col.setDate(currentDate);
			col.setNorm(normalHoures);
			col.setSup(supHoures);
			col.setSum(columnSum);
			col.setNormReel(heuresPresReel);


			summaryColumns.add(col);

			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		return summaryColumns;
	}

	public	class SummaryColumn implements Serializable{

		private static final long serialVersionUID = 1L;
		private Date date; 
		int sum;
		int sup;
		int norm;
		int normReel;

		public SummaryColumn() {
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public int getSum() {
			return sum;
		}

		public void setSum(int sum) {
			this.sum = sum;
		}

		public int getSup() {
			return sup;
		}

		public void setSup(int sup) {
			this.sup = sup;
		}

		public int getNorm() {
			return norm;
		}

		public void setNorm(int norm) {
			this.norm = norm;
		}

		public int getNormReel() {
			return normReel;
		}

		public void setNormReel(int normReel) {
			this.normReel = normReel;
		}
		

	}

	public	class ImputationColumn implements Serializable{

		private static final long serialVersionUID = 1L;
		private Date date; 
		private List<Imputation> imputations;

		public ImputationColumn() {
		}

		public ImputationColumn(Date date, List<Imputation> imputations) {
			this.date = date;
			this.imputations = imputations;
		}

		public List<Imputation> getImputations() {
			if(null==this.imputations){
				this.imputations = new ArrayList<Imputation>();
			}
			return this.imputations;
		}

		public void setImputations(List<Imputation> imputations) {
			this.imputations = imputations;
		}

		public Date getDate() {
			return this.date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
	}

	public class SummaryLine implements Serializable{

		private static final long serialVersionUID = 1L;
		private int rowId;
		private String rowTitle;
		private String property;


		public SummaryLine() {
		}
		public int getRowId() {
			return rowId;
		}
		public void setRowId(int rowId) {
			this.rowId = rowId;
		}
		public String getRowTitle() {
			return rowTitle;
		}
		public void setRowTitle(String rowTitle) {
			this.rowTitle = rowTitle;
		}
		public String getProperty() {
			return property;
		}
		public void setProperty(String property) {
			this.property = property;
		}

	} 

	public List<WBS> calculateSelectableWBS(){
		List<WBS> result = new ArrayList<WBS>();
		for (WBS wbs : selectableWBSListe) {
			if(wbs.isActif()){
				boolean found =false;
				for (ImputationLine line : imputationLines) {
					if(line.getYear()== year && line.getWeek() ==week
							&& line.getWbs().getWbsId()==wbs.getWbsId()){
						found= true;
						break;
					}
				}
				if(!found){
					result.add(wbs);
				}
			}
		
		}
		return result;
	}
	
	

}
