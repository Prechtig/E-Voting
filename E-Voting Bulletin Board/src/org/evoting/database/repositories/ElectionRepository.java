package org.evoting.database.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.evoting.database.entities.Election;

/**
 * Used to find timestamps in the persistent storage
 */
public class ElectionRepository extends EntityRepository<Election> {
	
	/**
	 * @param entMgr The EntityManager holding the connection to the persistent storage
	 */
	public ElectionRepository(EntityManager entMgr) {
		super(Election.class, entMgr);
	}
	
	/**
	 * @return The Election with the given id.
	 */
	public Election findElection(int id) {
		String query = "SELECT e FROM Election e WHERE e.id = ?1";
		List<Election> elections = super.findByQuery(query, id);
		if(elections.isEmpty()) {
			return null;
		}
		return elections.get(0);
	}
	
	public boolean timestampExists() {
		String query = "SELECT e FROM Election e";
		List<Election> timestamps = super.findByQuery(query);
		return timestamps.size() == 1;
	}
}