package org.evoting.client;

import org.evoting.client.exceptions.NoCandidateListException;
import org.evoting.common.EncryptedCandidateList;

import jolie.runtime.JavaService;
import jolie.runtime.Value;

/*
 * Contains the methods that the client jolie script calls.
 */
public class Controller extends JavaService
{
	/*
	 * Sets the candidates that can be voted for.
	 */
	public static void setCandidateList(Value encryptedCandidates)
	{
		// Interprets the value object as an encrypted candidate list.
		EncryptedCandidateList encryptedCandidateList = new EncryptedCandidateList(encryptedCandidates);
		// Decrypts the candidate list.
		CandidateList candidateList = encryptedCandidateList.getCandidateList();
		Model.setCandidates(candidateList);
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