package org.evoting.database.repositories;

import java.util.List;

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
	
	public Vote findById(int id) {
		String query = "SELECT v FROM Vote v WHERE v.id = ?1";
		return super.findSingleByQuery(query, id);
	}
	
	/**
	 * @param userId The id of the user to find the vote for
	 * @return The vote for the user with the given userId, otherwise null
	 */
	public List<Vote> findByUserId(String userId) {
		String query = "SELECT v FROM Vote v WHERE v.userId = ?1";
		return super.findByQuery(query, userId);
	}
	
	public List<Vote> findAll() {
		String query = "SELECT v FROM Vote v";
		return super.findByQuery(query);
	}
	
	public List<Vote> findAllValid() {
		String query = "SELECT v FROM Vote v WHERE v.timestamp = (SELECT MAX(vv.timestamp) FROM Vote vv WHERE vv.userId = v.userId)";
		return super.findByQuery(query);
	}
}