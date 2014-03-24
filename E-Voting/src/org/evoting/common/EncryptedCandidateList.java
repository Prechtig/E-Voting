package org.evoting.common;

import java.util.List;

import jolie.runtime.Value;
import jolie.runtime.ValueVector;

public class EncryptedCandidateList {
	// All the fields below are ciphertext.
	private byte[] timeStamp;
	private byte[] candidates;
	
	public EncryptedCandidateList(List<String> candidates, long timeStamp)
	{
		this.candidates = encryptCandidates(candidates);
		this.timeStamp = encryptTimeStamp(timeStamp);
	}
	
	public EncryptedCandidateList(Value value)
	{
		ValueVector vector = value.getChildren("timestamp");
	}
	
	private Value getValue()
	{
		Value result = Value.create();
		
		for(int i = 0; i < timeStamp.length; i++) {
			result.getNewChild("timestamp").setValue(timeStamp[i]);
		}
		
		for(int i = 0; i < candidates.length; i++) {
			result.getNewChild("candidates").setValue(candidates[i]);
		}
		
		return result;
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
