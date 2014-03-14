package org.evoting.client;

import org.evoting.client.exceptions.NoCandidateListException;

import jolie.runtime.JavaService;
import jolie.runtime.Value;

 
public class Controller extends JavaService
{
	/*
	 * Sets the candidates that can be voted for.
	 */
	public static void setCandidateList(String[] candidates)
	{
		Model.setCandidates(candidates);
	}
	
	/*
	 * Gets the voting ballot from user input.
	 */
    public Value getBallot() throws NoCandidateListException
    {
    	//Test
    	setCandidateList(new String[] {"John", "Hans", "Levi"});
    	
    	UserInputData userInputData = ConsoleIO.getUserInput();
    	Ballot ballot;
		try {
			ballot = Model.getBallot(userInputData);
		} catch (NoCandidateListException e) {
			System.err.println("No candidate list has been retrieved from server.");
			throw e;
		}
    	Value result = ballot.getValue();
    	return result;
    }
}