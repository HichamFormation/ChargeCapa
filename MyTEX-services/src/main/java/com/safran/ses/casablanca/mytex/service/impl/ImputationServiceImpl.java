package com.safran.ses.casablanca.mytex.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.sql.Select;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.safran.ses.casablanca.mytex.service.ImputationService;
import com.safran.ses.casablanca.mytex.service.model.Imputation;
import com.safran.ses.casablanca.mytex.service.model.ImputationLine;


@Component
public class ImputationServiceImpl implements ImputationService{

	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public List<ImputationLine> getImputationLines(int userId) {
		 TypedQuery<ImputationLine> query =entityManager.createQuery("select line from ImputationLine line where line.user.userId= :userId ",ImputationLine.class);
		 return query.setParameter("userId", userId).getResultList();
	}
	
	@Override
	public List<ImputationLine> getImputationLines(int userId,int wbsId) {
		 TypedQuery<ImputationLine> query =entityManager.createQuery("select line from ImputationLine line where line.user.userId= :userId ",ImputationLine.class);
		 return query.setParameter("userId", userId).getResultList();
	}

	@Override
	@Transactional
	public void addImputationLine(ImputationLine imputationLine) {
		entityManager.persist(imputationLine);
	}

	@Override
	@Transactional
	public void updateImputations(ImputationLine imputationLine) {
		entityManager.merge(imputationLine);
	}

	@Override
	public List<Imputation> getUserImputations(int userId) {
		List<Imputation> result = new ArrayList<Imputation>();
		
		List<ImputationLine> imputationLines = getImputationLines(userId);
		
		if(null!=imputationLines && imputationLines.size()>0){
			for (ImputationLine imputationLine : imputationLines) {
				result.addAll(imputationLine.getImputations());
			}
		}
		return result;
	}

	@Override
	@Transactional
	public void addImputation(Imputation imputation) {
		entityManager.persist(imputation);
		
	}

	@Override
	public List<ImputationLine> getImputationLinesForPeriode(int userId,
			int week, int year) {
		
		
		TypedQuery<ImputationLine> query =entityManager.createQuery("select line from ImputationLine line where line.user.userId= :userId and line.week= :week and line.year= :year",ImputationLine.class);
		query.setParameter("userId", userId);
		query.setParameter("week", week);
		query.setParameter("year", year);
		return query.getResultList();
	}

}
