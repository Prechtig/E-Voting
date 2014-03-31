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
	public static void setCandidateList(Value encryptedCandidates)
	{
		// Interprets the value object as an encrypted candidate list.
		EncryptedCandidateList encryptedCandidateList = new EncryptedCandidateList(encryptedCandidates);
		System.out.println(encryptedCandidateList);
		// Decrypts the candidate list.
		CandidateList candidateList = encryptedCandidateList.getCandidateList();
		Model.setCandidates(candidateList);
	}
	
	public static void setPublicKeys(Value publicKeyValues)
	{
		Model.setPublicKeys(publicKeyValues);
	}
	
	/**
	 * Gets the voting ballot from user input.
	 * @return Value representation of the ballot.
	 */
    public Value getBallot()
    {
    	UserInputData userInputData = ConsoleIO.getUserInput(Model.getNumberOfCandidates());
    	Value result = null;
		try {
	    	result = Model.getEncryptedBallot(userInputData).getValue();
	    	return result;
		} catch (NoCandidateListException e) {
			System.err.println("No candidate list has been retrieved from server.");
		}
		
		return result;
    }
}