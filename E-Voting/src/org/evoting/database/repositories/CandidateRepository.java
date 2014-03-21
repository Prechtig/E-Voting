package org.evoting.database.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.evoting.database.entities.Candidate;

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
		return super.findSingleByQuery(query, candidateId);
	}
	
	/**
	 * @return A list of all candidates, ordered by their id ascending
	 */
	public List<Candidate> findAll() {
		String query = "SELECT c FROM Candidate c ORDER BY c.id";
		return super.findByQuery(query);
	}
}