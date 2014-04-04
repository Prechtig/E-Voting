package org.evoting.client;

import jolie.runtime.JavaService;
import jolie.runtime.Value;

import org.evoting.client.exceptions.NoCandidateListException;
import org.evoting.common.CandidateList;
import org.evoting.common.EncryptedCandidateList;

/**
 * Contains the methods that the client jolie script calls.
 * @author Mark
 *
 */
public class Controller extends JavaService
{
	/**
	 * Sets the list of candidates that can be voted for.
	 * @param encryptedCandidates The value representation of the candidate object.
	 */
	public void setCandidateList(Value encryptedCandidates)
	{
		// Interprets the value object as an encrypted candidate list.
		EncryptedCandidateList encryptedCandidateList = new EncryptedCandidateList(encryptedCandidates);
		// Decrypts the candidate list.
		CandidateList candidateList = encryptedCandidateList.getCandidateList();
		Model.setCandidates(candidateList);
	}
	
	/**
	 * Sets the public keys for encryption and decryption.
	 * @param publicKeyValues The value representation of the public keys.
	 */
	public void setPublicKeys(Value publicKeyValues)
	{
		Model.setPublicKeys(publicKeyValues);
	}
	
	/**
	 * Gets the voting ballot from user input.
	 * @return Value representation of the ballot.
	 */
    public Value getBallot()
    {
    	//UserInputData userInputData = ConsoleIO.getUserInput(Model.getNumberOfCandidates());
    	UserInputData userInputData = new UserInputData();
    	userInputData.setCandidateId(1);
    	userInputData.setPassword("123");
    	userInputData.setUserId(123);
    	
		try {
	    	return Model.getEncryptedBallot(userInputData).getValue();
		} catch (NoCandidateListException e) {
			System.out.println("No candidate list has been retrieved from server.");
		}
		return null;
    }
    
    public Value setCandidateListAndGetBallot(Value encryptedCandidates) {
    	setCandidateList(encryptedCandidates);
    	return getBallot();
    }
}