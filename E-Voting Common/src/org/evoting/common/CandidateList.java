package org.evoting.common;

import java.util.List;

/**
 * Contains names of the candidates available for voting and a time stamp.
 * @author Mark
 *
 */
public class CandidateList
{
	private List<String> candidates;
	private byte[] timestamp;
	
	public CandidateList(List<String> candidates, byte[] timestamp)
	{
		this.candidates = candidates;
		this.timestamp = timestamp;
	}
	
	public byte[] getTimestamp() {
		return timestamp;
	}

	public List<String> getCandidates() {
		return candidates;
	}
}
