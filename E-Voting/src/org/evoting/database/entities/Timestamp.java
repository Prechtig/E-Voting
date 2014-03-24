package org.evoting.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Used to represent a timestamp
 */
@Entity
public class Timestamp extends BaseEntity {
	
	// The time
	@Column(name = "time", updatable = false, nullable = false, unique = true)
	private byte[] time;
	
	/**
	 * @param id The id of the Timestamp
	 * @param time The time of the Timestamp
	 */
	public Timestamp(int id, byte[] time) {
		super(id);
		this.time = time;
	}
	
	/**
	 * Private constructor needed for Hibernate 
	 */
	@SuppressWarnings("unused")
	private Timestamp() {
		super();
	}
	
	/**
	 * @return The time of this timestamp
	 */
	public byte[] getTime() {
		return this.time;
	}
}