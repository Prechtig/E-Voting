package org.evoting.database.repositories;

import javax.persistence.EntityManager;

import org.evoting.database.entities.Timestamp;

/**
 * Used to find timestamps in the persistent storage
 */
public class TimestampRepository extends EntityRepository<Timestamp> {
	
	/**
	 * @param entMgr The EntityManager holding the connection to the persistent storage
	 */
	public TimestampRepository(EntityManager entMgr) {
		super(Timestamp.class, entMgr);
	}
	
	/**
	 * @param time The time to check is existing in the persistent storage
	 * @return The Timestamp with the given time, if found, otherwise null
	 */
	public Timestamp findByTime(long time) {
		String query = "SELECT t FROM Timestamp t WHERE time = ?";
		return super.findSingleByQuery(query, time);
	}
}