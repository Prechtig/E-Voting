package org.evoting.common;

import java.util.ArrayList;
import java.util.List;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.client.CandidateList;
import org.evoting.database.entities.Candidate;

public class EncryptedCandidateList {
	
	private static final String TIMESTAMP_VALUE_NAME = "timestamp";
	private static final String CANDIDATES_VALUE_NAME = "candidates";
	// All the fields below are ciphertext.
	private byte[] timeStamp;
	private byte[] candidates;
	
	public EncryptedCandidateList(List<Candidate> candidates, byte[] timeStamp)
	{
		ArrayList<String> candidateNames = new ArrayList<String>(candidates.size());
		Candidate c;
		
		for(int i = 0; i < candidates.size(); i++) {
			c = candidates.get(i);
			if(c.getId() != i) {
				//TODO: throw correct exception.
				throw new RuntimeException();
			}
			candidateNames.add(c.getName());
		}
		
		this.candidates = encryptCandidates(candidateNames);
		this.timeStamp = timeStamp;
	}
	
	public EncryptedCandidateList(Value value)
	{
		ValueVector vector = value.getChildren(TIMESTAMP_VALUE_NAME);
		Value byteArrayValue = vector.first();
		timeStamp = byteArrayValue.byteArrayValue().getBytes();
		
		vector = value.getChildren(CANDIDATES_VALUE_NAME);
		byteArrayValue = vector.first();
		candidates = byteArrayValue.byteArrayValue().getBytes();		
	}
	
	public CandidateList getCandidateList()
	{
		throw new RuntimeException();
	}
		
	public Value getValue()
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
}
