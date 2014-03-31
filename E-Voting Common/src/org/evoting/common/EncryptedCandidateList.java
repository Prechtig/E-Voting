package org.evoting.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;

import org.evoting.database.entities.Candidate;
import org.evoting.security.Security;

/**
 * This class contains fields and logic for encrypting and decrypting candidate list data.
 * @author Mark
 *
 */
public class EncryptedCandidateList
{
	// Character used for converting a list of strings to a single string. Each string is seperated by this string.
	private static final String SEPERATION_CHARACTER = "|";
	// All the fields below are ciphertext.
	private byte[] timestamp;
	private byte[] candidates;
	
	/**
	 * Encrypts the names in the candidate list and constructs an object containing the encrypted names and the encrypted time stamp.
	 * @param candidates The list of candidates to be encrypted.
	 * @param timestamp The time stamp.
	 */
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
	
	/**
	 * Interprets a value as an encrypted candidate list.
	 * @param value The value object.
	 */
	public EncryptedCandidateList(Value value)
	{
		timestamp = value.getFirstChild(ValueIdentifiers.TIMESTAMP).byteArrayValue().getBytes();
		candidates = value.getFirstChild(ValueIdentifiers.CANDIDATES).byteArrayValue().getBytes();		
	}
	
	/**
	 * Decrypts the list and returns a decrypted object. Does not decrypt the timestamp.
	 * @return The decrypted candidate list.
	 */
	public CandidateList getCandidateList()
	{
		byte[] b = Security.getInstance().decryptRSA(candidates, Security.getInstance().getRSAPublicKey());
		String s = new String(b, Charset.forName("UTF-8"));
		String[] names = s.split(SEPERATION_CHARACTER);
		return new CandidateList(Arrays.asList(names), timestamp);
		
	}
	
	/**
	 * Gets the value representation of this object.
	 * @return The value representation of this object.
	 */
	public Value getValue()
	{
		Value result = Value.create();
		
		result.getNewChild(ValueIdentifiers.TIMESTAMP).setValue(new ByteArray(timestamp));
		result.getNewChild(ValueIdentifiers.CANDIDATES).setValue(new ByteArray(candidates));

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
		return Security.getInstance().encryptRSA(sb.toString(), Security.getInstance().getRSAPrivateKey());
	}
}
