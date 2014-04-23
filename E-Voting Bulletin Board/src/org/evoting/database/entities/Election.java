package org.evoting.database.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Used to represent a Election
 */
@Entity
public class Election extends BaseEntity {
	@Column(name = "endTime", updatable = false, nullable = false)
	private long endTime;
	
	/**
	 * @param id The id of the Election
	 * @param startTime the start time of the Election
	 * @param endTime the end time of the Election
	 */
	public Election(int id, Date endTime) {
		super(id);
		this.endTime = endTime.getTime();
	}
	
	/**
	 * Private constructor needed for Hibernate 
	 */
	@SuppressWarnings("unused")
	private Election() {
		super();
	}
	
	/**
	 * @return The end time of the Election
	 */
	public Date getEndTime() {
		return new Date(endTime);
	}
}