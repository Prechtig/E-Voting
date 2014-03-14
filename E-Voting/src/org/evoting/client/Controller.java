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
    public Value getBallot()
    {
    	//Test
    	setCandidateList(new String[] {"John", "Hans", "Levi"});
    	
    	UserInputData userInputData = ConsoleIO.getUserInput(Model.getNumberOfCandidates());
    	Ballot ballot;
    	Value result = null;
		try {
			ballot = Model.getBallot(userInputData);
	    	result = ballot.getValue();
	    	return result;
		} catch (NoCandidateListException e) {
			System.err.println("No candidate list has been retrieved from server.");
		}
		
		return result;
    }
}