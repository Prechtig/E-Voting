package org.evoting.database.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.evoting.database.entities.Election;
import org.evoting.database.exceptions.DatabaseInvariantException;

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
	
	public int findNextId() {
		String query = "SELECT MAX(e.id) FROM Election e";
		List<Election> elections = super.findByQuery(query);
		if(elections.isEmpty()) {
			return 0;
		}
		return elections.get(0).getId() + 1;
	}
	
	/**
	 * @return The Election with the given id.
	 * @throws DatabaseInvariantException Thrown if there is zero, or more than one timestamp in the persistent storage
	 */
	public Election findElection(int id) throws DatabaseInvariantException {
		String query = "SELECT e FROM Election e WHERE e.id = ?1";
		List<Election> elections = super.findByQuery(query, id);
		if(elections.size() == 0) {
			throw new DatabaseInvariantException("No election found with the given id.");
		}
		return elections.get(0);
	}
	
	public boolean timestampExists() {
		String query = "SELECT e FROM Election e";
		List<Election> timestamps = super.findByQuery(query);
		return timestamps.size() == 1;
	}
}