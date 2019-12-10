package com.safran.ses.casablanca.mytex.service;

import java.util.List;

import com.safran.ses.casablanca.mytex.service.model.HeuresTravail;
import com.safran.ses.casablanca.mytex.service.model.ImputationLine;
import com.safran.ses.casablanca.mytex.service.model.WBS;

public interface HeuresTravailService {

		
		public void createHeuresTravail(HeuresTravail heuresTravail);
		
		public List<HeuresTravail> getAllHeuresTravail();
		
		public void deleteHeuresTravail(HeuresTravail heuresTravail);
		
		public void updateHeuresTravail(HeuresTravail heuresTravail);
		
		public void updateHeureTravailByDefault(HeuresTravail ht);

}
