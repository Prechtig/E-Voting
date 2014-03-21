package org.evoting.database;

import java.util.List;

import javax.persistence.EntityManager;

public class CandidateRepository extends EntityRepository<Candidate>{
	
	public CandidateRepository(EntityManager entMgr) {
		super(Candidate.class, entMgr);
	}
	
	public Candidate findById(int candidateId) {
		String query = "SELECT c FROM Candidate c WHERE candidateId = ?";
		List<Candidate> resultList = super.findByQuery(query, candidateId);
		if(resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}
}