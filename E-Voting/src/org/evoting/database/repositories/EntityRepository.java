package org.evoting.database.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.evoting.database.entities.BaseEntity;

/**
 * Generic repository used to build the actual repositories upon 
 * @param <T> The type of the class used for the repository
 */
abstract public class EntityRepository<T extends BaseEntity> {
	// The EntityManager holding the connection to the persistent storage
	private EntityManager entMgr;
	// The class of the generic type
	private final Class<T> entityType;
	
	/**
	 * @param entityType The class of the generic type
	 * @param entMgr The EntityManager holding the connection to the persistent storage
	 */
	public EntityRepository(Class<T> entityType, EntityManager entMgr) {
		this.entityType = entityType;
		this.entMgr = entMgr;
	}
	
	/**
	 * @param queryString The query to be executed
	 * @param params The parameters to insert in the query, in the order of appearance in the query
	 * @return A list of the found entities
	 */
	public List<T> findByQuery(String queryString, Object... params) {
		TypedQuery<T> query = entMgr.createQuery(queryString, getEntityType());
		
		for(int i = 0; i < params.length; i++) {
			//Queries are 1 indexed
			query.setParameter(i+1, params[i]);
		}
		return query.getResultList();
	}
	
	/**
	 * @param queryString The query to be executed
	 * @param params The parameters to insert in the query, in the order of appearance in the query
	 * @return The entity if one was found, otherwise null
	 */
	public T findSingleByQuery(String queryString, Object... params) {
		TypedQuery<T> query = entMgr.createQuery(queryString, getEntityType());
		
		for(int i = 0; i < params.length; i++) {
			//Queries are 1 indexed
			query.setParameter(i+1, params[i]);
		}
		List<T> resultList = query.getResultList();
		if(resultList.isEmpty()) {
			return null;
		}
		return resultList.get(0);
	}
	
	/**
	 * @return The class of the generic type
	 */
	private Class<T> getEntityType() {
		return this.entityType;
	}
}