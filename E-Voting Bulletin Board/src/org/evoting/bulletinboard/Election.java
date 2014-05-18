package org.evoting.bulletinboard;

import java.util.Date;

/**
 * Used to represent a Election
 */
public class Election {
	private Date startTime;
	private Date endTime;
	
	/**
	 * @param id The id of the Election
	 * @param startTime the start time of the Election
	 * @param endTime the end time of the Election
	 */
	public Election(Date startTime, Date endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * @return The start time of the Election
	 */
	public Date getStartTime() {
		return startTime;
	}
	
	/**
	 * @return The end time of the Election
	 */
	public Date getEndTime() {
		return endTime;
	}
}