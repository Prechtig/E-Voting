package org.evoting.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class VoteRepository {
	private EntityManager entMgr;
	
	public VoteRepository(EntityManager entMgr) {
		this.entMgr = entMgr;
	}
	
	private List<Vote> findByQuery(String queryString, Object... params) {
		TypedQuery<Vote> query = entMgr.createQuery(queryString, Vote.class);
		
		for(int i = 0; i < params.length; i++) {
			//Queries are 1 indexed
			query.setParameter(i+1, params[i]);
		}
		return query.getResultList();
	}
	
	public Vote findById(int userId) {
		String query = "SELECT v FROM Vote v WHERE userId = ?";
		List<Vote> resultList = findByQuery(query, userId);
		if(resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}
}