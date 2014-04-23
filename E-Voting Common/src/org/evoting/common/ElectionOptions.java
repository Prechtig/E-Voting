package org.evoting.common;

import java.util.Date;
import java.util.List;

import org.evoting.database.entities.ElectionOption;

/**
 * Contains names of the electionOptions available for voting and a time stamp.
 * @author Mark
 *
 */
public class ElectionOptions
{
	private List<ElectionOption> electionOptions;
	private int electionId;
	private Date endTime;
	
	public ElectionOptions(List<ElectionOption> electionOptions, int electionId, Date endTime)
	{
		this.electionOptions = electionOptions;
		this.electionId = electionId;
		this.endTime = endTime;
	}
	
	public int getElectionId() {
		return electionId;
	}

	public List<ElectionOption> getElectionOptions() {
		return electionOptions;
	}
	
	public Date getEndTime() {
		return endTime;
	}
}
