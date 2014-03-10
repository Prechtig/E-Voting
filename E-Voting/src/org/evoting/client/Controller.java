package org.evoting.client;

import jolie.runtime.JavaService;
 
public class Controller extends JavaService
{
	public static void setCandidateList(String[] candidates)
	{
		Model.setCandidates(candidates);
	}
	
    public Integer getBallot()
    {
    	UserInputData userInputData = ConsoleIO.getUserInput();
    	Ballot ballot = Model.getBallot(userInputData);
    	return 3;
    }
}