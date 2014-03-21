package org.evoting.database;

import java.util.List;

import javax.persistence.EntityManager;

/**
 * Used to find candidates in the persistent storage 
 */
public class CandidateRepository extends EntityRepository<Candidate>{
	
	public CandidateRepository(EntityManager entMgr) {
		super(Candidate.class, entMgr);
	}
	
	/**
	 * @param candidateId The id of the candidate searching for
	 * @return The candidate with the given id if one exists, null otherwise
	 */
	public Candidate findById(int candidateId) {
		String query = "SELECT c FROM Candidate c WHERE candidateId = ?";
		List<Candidate> resultList = super.findByQuery(query, candidateId);
		if(resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}
	
	/**
	 * @return A list of all candidates, ordered by their id ascending
	 */
	public List<Candidate> findAll() {
		String query = "SELECT c FROM Candidate c ORDER BY c.id";
		List<Candidate> candidates = super.findByQuery(query);
		return candidates;
	}
}