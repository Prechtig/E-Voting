package org.evoting.database.repositories;

import javax.persistence.EntityManager;

import org.evoting.database.entities.Vote;

/**
 * Used to find votes in the persistent storage 
 */
public class VoteRepository extends EntityRepository<Vote> {
	
	/**
	 * @param entMgr The EntityManager holding the connection to the persistent storage
	 */
	public VoteRepository(EntityManager entMgr) {
		super(Vote.class, entMgr);
	}
	
	/**
	 * @param userId The id of the user to find the vote for
	 * @return The vote for the user with the given userId, otherwise null
	 */
	public Vote findById(int userId) {
		String query = "SELECT v FROM Vote v WHERE id = ?";
		return super.findSingleByQuery(query, userId);
	}
}