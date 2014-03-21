package org.evoting.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

abstract public class EntityRepository<T extends BaseEntity> {
	private EntityManager entMgr;
	private final Class<T> entityType;
	
	public EntityRepository(Class<T> entityType, EntityManager entMgr) {
		this.entityType = entityType;
		this.entMgr = entMgr;
	}
	
	public List<T> findByQuery(String queryString, Object... params) {
		TypedQuery<T> query = entMgr.createQuery(queryString, getEntityType());
		
		for(int i = 0; i < params.length; i++) {
			//Queries are 1 indexed
			query.setParameter(i+1, params[i]);
		}
		return query.getResultList();
	}
	
	private Class<T> getEntityType() {
		return this.entityType;
	}
}