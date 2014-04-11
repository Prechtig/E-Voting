package org.evoting.database.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.evoting.database.entities.ElectionOption;

/**
 * Used to find electionOptions in the persistent storage 
 */
public class ElectionOptionRepository extends EntityRepository<ElectionOption>{
	
	public ElectionOptionRepository(EntityManager entMgr) {
		super(ElectionOption.class, entMgr);
	}
	
	/**
	 * @param electionOptionId The id of the electionOption searching for
	 * @return The electionOption with the given id if one exists, null otherwise
	 */
	public ElectionOption findById(int electionOptionId) {
		String query = "SELECT c FROM ElectionOption c WHERE id = ?1";
		return super.findSingleByQuery(query, electionOptionId);
	}
	
	/**
	 * @return A list of all electionOptions, ordered by their id ascending
	 */
	public List<ElectionOption> findAll() {
		String query = "SELECT c FROM ElectionOption c ORDER BY c.id";
		return super.findByQuery(query);
	}
}