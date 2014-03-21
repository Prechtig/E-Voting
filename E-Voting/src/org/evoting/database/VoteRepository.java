package org.evoting.database;

import java.util.List;

import javax.persistence.EntityManager;

public class VoteRepository extends EntityRepository<Vote> {
	
	public VoteRepository(EntityManager entMgr) {
		super(Vote.class, entMgr);
	}
	
	public Vote findById(int userId) {
		String query = "SELECT v FROM Vote v WHERE userId = ?";
		List<Vote> resultList = super.findByQuery(query, userId);
		if(resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}
}