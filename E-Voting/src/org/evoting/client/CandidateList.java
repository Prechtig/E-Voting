package org.evoting.client;

import java.util.List;

public class CandidateList {
	private String timeStamp;
	private List<String> candidates;
	
	public CandidateList(List<String> candidates, String timeStamp)
	{
		this.candidates = candidates;
		this.timeStamp = timeStamp;
	}
	
}
