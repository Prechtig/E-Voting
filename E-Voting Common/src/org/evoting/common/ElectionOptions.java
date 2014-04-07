package org.evoting.common;

import java.util.List;

/**
 * Contains names of the electionOptions available for voting and a time stamp.
 * @author Mark
 *
 */
public class ElectionOptions
{
	private List<String> electionOptions;
	private byte[] timestamp;
	
	public ElectionOptions(List<String> electionOptions, byte[] timestamp)
	{
		this.electionOptions = electionOptions;
		this.timestamp = timestamp;
	}
	
	public byte[] getTimestamp() {
		return timestamp;
	}

	public List<String> getElectionOptions() {
		return electionOptions;
	}
}
