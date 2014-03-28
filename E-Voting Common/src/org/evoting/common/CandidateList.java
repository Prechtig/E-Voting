package org.evoting.common;

import java.util.List;

public class CandidateList
{
	private byte[] timeStamp;
	private List<String> candidates;
	
	public CandidateList(List<String> candidates, byte[] timestamp)
	{
		this.candidates = candidates;
		this.timeStamp = timeStamp;
	}
	
	public byte[] getTimeStamp() {
		return timeStamp;
	}

	public List<String> getCandidates() {
		return candidates;
	}
}
