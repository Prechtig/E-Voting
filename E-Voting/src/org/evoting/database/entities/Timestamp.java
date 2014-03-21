package org.evoting.database.entities;

import javax.persistence.Column;

/**
 * Used to represent a timestamp
 */
public class Timestamp extends BaseEntity {
	
	// The time
	@Column(name = "time", updatable = false, nullable = false, unique = true)
	private long time;
	
	/**
	 * @param id The id of the Timestamp
	 * @param time The time of the Timestamp
	 */
	public Timestamp(int id, long time) {
		super(id);
		this.time = time;
	}
	
	/**
	 * @return The time of this timestamp
	 */
	public long getTime() {
		return this.time;
	}
}