package org.evoting.client;

import java.io.Console;

public class ConsoleIO
{	
	public static final int CPR_NUMBER_LENGTH = 10;
	/**
	 * Gets user input data required to create a ballot from console.
	 * @param numberOfCandidates the number of candidates available for voting.
	 * @return Object containing the input data.
	 */
	public static UserInputData getUserInput(int numberOfCandidates)
	{
		Console console = System.console();
		System.out.println("Number of canditates is: " + numberOfCandidates);
		String input = "";
		int userId, candidateId;
		String password;
		UserInputData userData = new UserInputData();
		
		while(!isCPRNumber(input)) {
			System.out.println("Enter CPR number:");
			input = console.readLine();
			if(!isCPRNumber(input)) {
				System.out.println("The input is not a CPR number.\n");
			}
		}
		
		userId = Integer.parseInt(input);
		input = "";
		
		while(!isValidPassword(input)) {
			System.out.println("Enter user password:");
			input = console.readLine();
			if(!isValidPassword(input)) {
				System.out.println("The input does not meet the requirements for a password.\n");
			}
		}
		
		password = input;
		input = "";
		
		while(!isCandidateId(input, numberOfCandidates)) {
			System.out.println("Enter the ID of the candidate that you want to vote for:");
			input = console.readLine();
			if(!isCandidateId(input, numberOfCandidates)) {
				System.out.println("The input does not match any legal candidate IDs.\n");
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
		if(CPR_NUMBER_LENGTH < str.length()) {
			return false;
		}
		return isNumber(str);
	}
	
	private static boolean isValidPassword(String str)
	{
		if(str.equals("")) {
			return false;
		}
		return true;
	}
	
	private static boolean isCandidateId(String str, int numberOfCandidates)
	{
		int candidateId;
		if(isNumber(str)) {
			candidateId = Integer.parseInt(str);
		} else {
			return false;
		}
		return  0 <= candidateId && candidateId < numberOfCandidates;
	}
}
