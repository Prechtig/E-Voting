package org.evoting.common;

import java.util.ArrayList;
import java.util.List;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.database.entities.Candidate;

public class EncryptedCandidateList {
	
	private static final String TIMESTAMP_VALUE_NAME = "timestamp";
	private static final String CANDIDATES_VALUE_NAME = "candidates";
	// All the fields below are ciphertext.
	private byte[] timestamp;
	private byte[] candidates;
	
	public EncryptedCandidateList(List<Candidate> candidates, byte[] timestamp)
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
		this.timestamp = timestamp;
	}
	
	public EncryptedCandidateList(Value value)
	{
		ValueVector vector = value.getChildren(TIMESTAMP_VALUE_NAME);
		timestamp = vector.first().byteArrayValue().getBytes();
		
		vector = value.getChildren(CANDIDATES_VALUE_NAME);
		candidates = vector.first().byteArrayValue().getBytes();		
	}
	
	public CandidateList getCandidateList()
	{
		throw new RuntimeException();
	}
		
	public Value getValue()
	{
		Value result = Value.create();
		
		result.getNewChild(TIMESTAMP_VALUE_NAME).setValue(new ByteArray(timestamp));
		result.getNewChild(CANDIDATES_VALUE_NAME).setValue(new ByteArray(candidates));

		return result;
	}
	
	private byte[] encryptCandidates(List<String> candidates)
	{
		throw new UnsupportedOperationException();
	}
}
