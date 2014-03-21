package org.evoting.database;

import java.util.List;

import javax.persistence.EntityManager;

/**
 * Used to find votes in the persistent storage 
 */
public class VoteRepository extends EntityRepository<Vote> {
	
	public VoteRepository(EntityManager entMgr) {
		super(Vote.class, entMgr);
	}
	
	/**
	 * @param userId The id of the user to find the vote for
	 * @return The vote for the user with the given userId, otherwise null
	 */
	public Vote findById(int userId) {
		String query = "SELECT v FROM Vote v WHERE userId = ?";
		List<Vote> resultList = super.findByQuery(query, userId);
		if(resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}
}