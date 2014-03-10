package org.evoting.client;
 
public class Controller {
	
    public static Ballot getBallot(UserInputData userInputData)
    {
    	return Model.getBallot(userInputData);
    }
}