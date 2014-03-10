package org.evoting.client;

import java.io.Console;

import jolie.runtime.JavaService;

public class ConsoleIO extends JavaService
{
	
	public static Ballot getBallot()
	{
		UserInputData userInputData = getUserInput();
		Ballot ballot = Controller.getBallot(userInputData);
		return ballot;
	}
	
	private static UserInputData getUserInput()
	{
		Console console = System.console();
		String input = "";
		int userId, candidateId;
		String password;
		UserInputData userData = new UserInputData();
		
		while(!isCPRNumber(input)) {
			System.out.println("Enter CPR number:");
			input = console.readLine();
			if(!isCPRNumber(input)) {
				System.out.println("The input is not a CPR number.");
				System.out.println("");
			}
		}
		
		userId = Integer.parseInt(input);
		input = "";
		
		while(!isPassword(input)) {
			System.out.println("Enter user password:");
			input = console.readLine();
			if(!isPassword(input)) {
				System.out.println("The input does not match the requirements for a password");
				System.out.println("");
			}
		}
		
		password = input;
		input = "";
		
		while(!isCandidateId(input)) {
			System.out.println("Enter the ID of the candidate that you want to vote for:");
			input = console.readLine();
			if(!isCandidateId(input)) {
				System.out.println("The input does not match any legal candidate IDs");
				System.out.println("");
			}
		}
		
		candidateId = Integer.parseInt(input);
		
		userData.setUserId(userId);
		userData.setPassword(password);
		userData.setCandidateId(candidateId);
		return userData;
	}
	
	private static boolean isNumber(String str)
	{
		if(str.equals("")) {
			return false;
		}
	    for(char c : str.toCharArray()) {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	private static boolean isCPRNumber(String str) {
		return isNumber(str);
	}
	
	private static boolean isPassword(String str)
	{
		if(str.equals("")) {
			return false;
		}
		return true;
	}
	
	private static boolean isCandidateId(String str)
	{
		return isNumber(str);
	}
	
}
