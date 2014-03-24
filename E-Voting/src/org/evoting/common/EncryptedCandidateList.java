package org.evoting.common;

import java.util.List;

public class EncryptedCandidateList {
	// All the fields below are ciphertext.
	private byte[] timeStamp;
	private byte[] candidates;
	
	public EncryptedCandidateList(List<String> candidates, long timeStamp)
	{
		this.candidates = encryptCandidates(candidates);
		this.timeStamp = encryptTimeStamp(timeStamp);
	}
	
	private byte[] encryptCandidates(List<String> candidates)
	{
		byte[] result = new byte[0];
		return result;
	}
	
	private byte[] encryptTimeStamp(long timeStamp)
	{
		byte[] result = new byte[0];
		return result;
	}
}
