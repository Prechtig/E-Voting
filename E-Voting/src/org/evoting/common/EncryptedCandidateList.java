package org.evoting.common;

import java.util.List;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

public class EncryptedCandidateList {
	// All the fields below are ciphertext.
	private byte[] timeStamp;
	private byte[] candidates;
	private static final String TIMESTAMP_VALUE_NAME = "timestamp";
	private static final String CANDIDATES_VALUE_NAME = "candidates";
	
	public EncryptedCandidateList(List<String> candidates, long timeStamp)
	{
		this.candidates = encryptCandidates(candidates);
		this.timeStamp = encryptTimeStamp(timeStamp);
	}
	
	public EncryptedCandidateList(Value value)
	{
		ValueVector vector = value.getChildren(TIMESTAMP_VALUE_NAME);
	}
	
	private Value getValue()
	{
		Value result = Value.create();
		
		Value timeStampByteArray = Value.create(new ByteArray(timeStamp));
		result.getNewChild(TIMESTAMP_VALUE_NAME).assignValue(timeStampByteArray);
		
		Value candidatesByteArray = Value.create(new ByteArray(candidates));
		result.getNewChild(CANDIDATES_VALUE_NAME).assignValue(candidatesByteArray);

		return result;
	}
	
	private byte[] encryptCandidates(List<String> candidates)
	{
		throw new UnsupportedOperationException();
	}
	
	private byte[] encryptTimeStamp(long timeStamp)
	{
		throw new UnsupportedOperationException();
	}
}
