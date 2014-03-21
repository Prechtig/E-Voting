package org.evoting.client;

import java.util.List;

public class EncryptedCandidateList {
	// All the fields below are ciphertext.
	private String timeStamp;
	private String candidates;
	
	public EncryptedCandidateList(List<String> candidates, long timeStamp)
	{
		this.candidates = encryptCandidates(candidates);
		this.timeStamp = encryptTimeStamp(timeStamp);
	}
	
	private String encryptCandidates(List<String> candidates)
	{
		return "";
	}
	
	private String encryptTimeStamp(long timeStamp)
	{
		return "";
	}
}
