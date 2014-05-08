package org.evoting.client;

import java.io.Console;

import org.evoting.common.LoginRequest;

public class ConsoleIO
{	
	public static final int CPR_NUMBER_LENGTH = 10;
	
	public static LoginRequest getLoginData() {
		
		Console console = System.console();
		String input = "";
		String userId;
		String password;
		
		while(!isCPRNumber(input)) {
			System.out.println("Enter CPR number:");
			input = console.readLine();
			if(!isCPRNumber(input)) {
				System.out.println("The input is not a CPR number.\n");
			}
		}
		
		userId = input;
		input = "";
		
		while(!isValidPassword(input)) {
			System.out.println("Enter user password:");
			input = console.readLine();
			if(!isValidPassword(input)) {
				System.out.println("The input does not meet the requirements for a password.\n");
			}
		}
		
		password = input;
		
		return new LoginRequest(userId, Hasher.hashPassword(password));
	}
	
	/**
	 * Gets user input data required to create a ballot from console.
	 * @param numberOfElectionOptions the number of electionOptions available for voting.
	 * @return Object containing the input data.
	 */
	public static UserInputData getElectionOptionInput(int numberOfElectionOptions)
	{
		Console console = System.console();
		System.out.println("Number of canditates is: " + numberOfElectionOptions);
		String input = "";
		int electionOptionId;
		UserInputData userData = new UserInputData();
		
		while(!isElectionOptionId(input, numberOfElectionOptions)) {
			System.out.println("Enter the ID of the candidate or party that you want to vote for:");
			input = console.readLine();
			if(!isElectionOptionId(input, numberOfElectionOptions)) {
				System.out.println("The input does not match any legal electionOption IDs.\n");
			}
		}
		
		electionOptionId = Integer.parseInt(input);
		
		userData.setElectionOptionId(electionOptionId);
		return userData;
	}
	
	public static String getCommand() {
		Console console = System.console();
		System.out.println("Would you like to vote or get the list of all votes?");
		System.out.println("Write \"vote\" to vote and \"get\" to get the list of votes");
		while(true) {
			String input = console.readLine().toLowerCase();
			if("vote".equals(input) || "get".equals(input)) {
				return input;
			} else {
				System.out.println("Invalid command");
			}
		}
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
	
	private static boolean isElectionOptionId(String str, int numberOfElectionOptions)
	{
		int electionOptionId;
		if(isNumber(str)) {
			electionOptionId = Integer.parseInt(str);
		} else {
			return false;
		}
		return  0 <= electionOptionId && electionOptionId < numberOfElectionOptions;
	}
}
