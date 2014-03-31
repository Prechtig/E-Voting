package org.evoting.database.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.evoting.database.entities.Timestamp;
import org.evoting.database.exceptions.DatabaseInvariantException;

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
	 * @return The Timestamp in the database.
	 * @throws DatabaseInvariantException Thrown if there is zero, or more than one timestamp in the persistent storage
	 */
	public Timestamp findTime() throws DatabaseInvariantException {
		String query = "SELECT t FROM Timestamp t";
		List<Timestamp> timestamps = super.findByQuery(query);
		if(timestamps.size() == 0) {
			throw new DatabaseInvariantException("No timestamp found in the persistent storage.");
		} else if(timestamps.size() > 1) {
			throw new DatabaseInvariantException("More than one timestamp found in the persistent storage.");
		}
		return timestamps.get(0);
	}
	
	public boolean timestampExists() {
		String query = "SELECT t FROM Timestamp t";
		List<Timestamp> timestamps = super.findByQuery(query);
		return timestamps.size() == 1;
	}
}