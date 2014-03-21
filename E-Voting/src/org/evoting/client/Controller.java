package org.evoting.client;

import org.evoting.client.exceptions.NoCandidateListException;

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
		//TODO String[] candidates dekrypteres og tjekkes for at være sikker på at det kommer fra BB
		String[] candidates = {"test1","test2"};
		Model.setCandidates(candidates);
	}
	
	/*
	 * Gets the voting ballot from user input.
	 */
    public Value getBallot()
    {
    	UserInputData userInputData = ConsoleIO.getUserInput(Model.getNumberOfCandidates());
    	Ballot ballot;
    	Value result = null;
		try {
			ballot = Model.getBallot(userInputData);
	    	result = Model.getEncryptedBallot(ballot).getValue();
	    	return result;
		} catch (NoCandidateListException e) {
			System.err.println("No candidate list has been retrieved from server.");
		}
		
		return result;
    }
}