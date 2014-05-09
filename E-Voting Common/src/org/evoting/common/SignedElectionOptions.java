package org.evoting.common;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.common.exceptions.BadValueException;
import org.evoting.database.entities.ElectionOption;
import org.evoting.security.Security;

/**
 * This class contains fields and logic for encrypting and decrypting electionOption list data.
 * @author Mark
 *
 */
public class SignedElectionOptions
{
	private int electionId;
	private List<ElectionOption> electionOptions;
	// All the fields below are ciphertext.
	private byte[] signature;
	
	/**
	 * Encrypts the names in the electionOption list and constructs an object containing the encrypted names and the encrypted time stamp.
	 * @param electionOptions The list of electionOptions to be encrypted.
	 * @param signedElectionId The time stamp.
	 */
	public SignedElectionOptions(List<ElectionOption> electionOptions, int electionId, Date endTime)
	{
		for(int i = 0; i < electionOptions.size(); i++) {
			if(electionOptions.get(i).getElectionId() != i) {
				//TODO: throw correct exception.
				throw new RuntimeException();
			}
		}
		
		this.electionId = electionId;
		this.electionOptions = electionOptions;
		this.signature = Security.sign(Security.getBulletinBoardRSAPrivateKey(), Converter.toByteArray(electionId), Converter.toByteArray(endTime.getTime()));
	}
	
	/**
	 * Interprets a value as an encrypted electionOption list.
	 * @param value The value object.
	 */
	public SignedElectionOptions(Value encryptedElectionOptionsValue)
	{
		// Checks whether the value object has the required fields.
		if(!encryptedElectionOptionsValue.hasChildren(ValueIdentifiers.getElectionId()) ||
			!encryptedElectionOptionsValue.hasChildren(ValueIdentifiers.getElectionOptions()) ||
			!encryptedElectionOptionsValue.hasChildren(ValueIdentifiers.getSignature())) {
				throw new BadValueException();
		}
		
		electionId = encryptedElectionOptionsValue.getFirstChild(ValueIdentifiers.getElectionId()).intValue();

		//Get the election options children
		ValueVector electionOptionsValueVector = encryptedElectionOptionsValue.getChildren(ValueIdentifiers.getElectionOptions());
		//Get the iterator to be able to iterate over the children
		Iterator<Value> electionOptionsIterator = electionOptionsValueVector.iterator();
		ElectionOption[] options = new ElectionOption[electionOptionsValueVector.size()];
		while(electionOptionsIterator.hasNext()) {
			Value current = electionOptionsIterator.next();
			int id = current.getFirstChild(ValueIdentifiers.getId()).intValue();
			String name = current.getFirstChild(ValueIdentifiers.getName()).strValue();
			int partyId = current.getFirstChild(ValueIdentifiers.getPartyId()).intValue();
			//Create an election option with the found parameters
			options[id] = new ElectionOption(id, name, partyId);
		}
		electionOptions = Arrays.asList(options);
		
		signature = encryptedElectionOptionsValue.getFirstChild(ValueIdentifiers.getSignature()).byteArrayValue().getBytes();
	}
	
	/**
	 * Decrypts the list and returns a decrypted object. Does not decrypt the timestamp.
	 * @return The decrypted electionOption list.
	 */
	public ElectionOptions getElectionOptions()
	{
		return new ElectionOptions(electionOptions, electionId);
	}
	
	/**
	 * Gets the value representation of this object.
	 * @return The value representation of this object.
	 */
	public Value getValue()
	{
		Value result = Value.create();
		
		result.getNewChild(ValueIdentifiers.getElectionId()).setValue(electionId);
		for(ElectionOption e : electionOptions) {
			Value electionOptions = result.getNewChild(ValueIdentifiers.getElectionOptions());
			electionOptions.getNewChild(ValueIdentifiers.getId()).setValue(e.getElectionId());
			electionOptions.getNewChild(ValueIdentifiers.getName()).setValue(e.getName());
			electionOptions.getNewChild(ValueIdentifiers.getPartyId()).setValue(e.getPartyId());
		}
		result.getNewChild(ValueIdentifiers.getSignature()).setValue(new ByteArray(signature));

		return result;
	}
}