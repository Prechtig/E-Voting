package org.evoting.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;

import org.evoting.common.exceptions.BadValueException;
import org.evoting.database.entities.ElectionOption;
import org.evoting.security.Security;

/**
 * This class contains fields and logic for encrypting and decrypting electionOption list data.
 * @author Mark
 *
 */
public class EncryptedElectionOptions
{
	// Character used for converting a list of strings to a single string. Each string is separated by this string.
	private static final String SEPERATION_CHARACTER = "@";
	// All the fields below are ciphertext.
	private byte[] timestamp;
	private byte[] electionOptions;
	
	/**
	 * Encrypts the names in the electionOption list and constructs an object containing the encrypted names and the encrypted time stamp.
	 * @param electionOptions The list of electionOptions to be encrypted.
	 * @param timestamp The time stamp.
	 */
	public EncryptedElectionOptions(List<ElectionOption> electionOptions, byte[] timestamp)
	{
		ArrayList<String> electionOptionNames = new ArrayList<String>(electionOptions.size());
		ElectionOption c;
		
		for(int i = 0; i < electionOptions.size(); i++) {
			c = electionOptions.get(i);
			if(c.getId() != i) {
				//TODO: throw correct exception.
				throw new RuntimeException();
			}
			electionOptionNames.add(c.getName());
		}
		
		this.electionOptions = encryptElectionOptions(electionOptionNames);
		this.timestamp = timestamp;
	}
	
	/**
	 * Interprets a value as an encrypted electionOption list.
	 * @param value The value object.
	 */
	public EncryptedElectionOptions(Value encryptedElectionOptions)
	{
		// Checks whether the value object has the required fields.
		if(!encryptedElectionOptions.hasChildren(ValueIdentifiers.getTimestamp()) ||
			!encryptedElectionOptions.hasChildren(ValueIdentifiers.getElectionOptions())) {
				throw new BadValueException();
		}
		
		timestamp = encryptedElectionOptions.getFirstChild(ValueIdentifiers.getTimestamp()).byteArrayValue().getBytes();
		electionOptions = encryptedElectionOptions.getFirstChild(ValueIdentifiers.getElectionOptions()).byteArrayValue().getBytes();		
	}
	
	/**
	 * Decrypts the list and returns a decrypted object. Does not decrypt the timestamp.
	 * @return The decrypted electionOption list.
	 */
	public ElectionOptions getElectionOptions()
	{
		byte[] b = Security.decryptRSA(electionOptions, Security.getRSAPublicKey());
		String s = new String(b, Charset.forName("UTF-8"));
		String[] names = s.split(SEPERATION_CHARACTER);
		return new ElectionOptions(Arrays.asList(names), timestamp);
		
	}
	
	/**
	 * Gets the value representation of this object.
	 * @return The value representation of this object.
	 */
	public Value getValue()
	{
		Value result = Value.create();
		
		result.getNewChild(ValueIdentifiers.getTimestamp()).setValue(new ByteArray(timestamp));
		result.getNewChild(ValueIdentifiers.getElectionOptions()).setValue(new ByteArray(electionOptions));

		return result;
	}
	
	/**
	 * Encrypts the electionOptions as a string separated by the separation character constant. 
	 * @param The electionOptions that the list is created with.
	 * @return The encrypted byte sequence.
	 */
	private byte[] encryptElectionOptions(List<String> electionOptions)
	{
		StringBuilder sb = new StringBuilder();
		for(String s : electionOptions) {
			sb.append(s);
			sb.append(SEPERATION_CHARACTER);
		}
		return Security.sign(sb.toString(), Security.getRSAPrivateKey());
		//return Security.encryptRSA(sb.toString(), Security.getRSAPrivateKey());
	}
	
	public String toString()
	{
		return "ElectionOption ciphertext: " + electionOptions.toString() + "\nTimestamp ciphertext: " + timestamp.toString(); 
	}
}
