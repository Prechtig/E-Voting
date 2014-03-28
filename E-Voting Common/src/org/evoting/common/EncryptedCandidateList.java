package org.evoting.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.database.entities.Candidate;
import org.evoting.security.Security;

public class EncryptedCandidateList
{
	private static final String TIMESTAMP_VALUE_NAME = "timestamp";
	private static final String CANDIDATES_VALUE_NAME = "candidates";
	private static final String SEPERATION_CHARACTER = "|";
	private Security security = new Security();
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
		byte[] b = security.decryptElgamal(candidates, null);
		String s = new String(b, Charset.forName("UTF-8"));
		String[] names = s.split(SEPERATION_CHARACTER);
		return new CandidateList(Arrays.asList(names), timestamp);
		
	}
		
	public Value getValue()
	{
		Value result = Value.create();
		
		result.getNewChild(TIMESTAMP_VALUE_NAME).setValue(new ByteArray(timestamp));
		result.getNewChild(CANDIDATES_VALUE_NAME).setValue(new ByteArray(candidates));

		return result;
	}
	
	/**
	 * Encrypts the candidates as a string seperated by the seperation character constant. 
	 * @param The candidates that the list is created with.
	 * @return The encrypted byte sequence.
	 */
	private byte[] encryptCandidates(List<String> candidates)
	{
		StringBuilder sb = new StringBuilder();
		for(String s : candidates) {
			sb.append(s);
			sb.append(SEPERATION_CHARACTER);
		}
		return security.encryptElGamal(sb.toString(), null);
	}
}
