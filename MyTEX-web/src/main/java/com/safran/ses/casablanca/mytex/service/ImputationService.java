package com.safran.ses.casablanca.mytex.service;

import java.util.List;

import com.safran.ses.casablanca.mytex.service.model.Imputation;
import com.safran.ses.casablanca.mytex.service.model.ImputationLine;
import com.safran.ses.casablanca.mytex.service.model.WBS;

public interface ImputationService {
	
	public List<ImputationLine> getImputationLines(int userId);
	
	public List<ImputationLine> getImputationLines(int userId,int wbsId);
	
	public void addImputationLine(ImputationLine imputationLine);
	
	public void updateImputations(ImputationLine imputationLine);
	
	public List<Imputation> getUserImputations(int userId);
	
	public void addImputation(Imputation imputation);
	
	public List<ImputationLine> getImputationLinesForPeriode(int userId,int week, int year);
	


}
