package org.evoting.client;

import java.util.List;

public class CandidateList {
	private byte[] timeStamp;
	private List<String> candidates;
	
	public CandidateList(List<String> candidates, byte[] timeStamp)
	{
		this.candidates = candidates;
		this.timeStamp = timeStamp;
	}
	
}
