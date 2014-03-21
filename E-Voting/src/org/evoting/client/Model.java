package org.evoting.client;

import java.util.ArrayList;
import org.evoting.client.exceptions.NoCandidateListException;

/*
 * Contains data about the candidates and logic that supports ballot creation.
 */
public class Model
{ 
	// List of candidate names. The index is equal to the candidate id.
	private static ArrayList<String> candidateList;
	// The time stamp that marks the candidate list.
	private static long candidateListTime;
	// The number of candidates contained in the candidateList.
	private static int numberOfCandidates = 0;
	
	/*
	 * Sets the list of available candidates.
	 */
	public static void setCandidates(String[] candidates)
	{
		candidateList = new ArrayList<String>();
		for(String s : candidates) {
			candidateList.add(s);
		}
		numberOfCandidates = candidateList.size();
		//Test
		System.out.println("Number of candidates in model: " + numberOfCandidates);
	}
	
	/*
	 * Returns the number of candidates available for voting. Returns zero if setCandidates has not been called.
	 */
	public static int getNumberOfCandidates()
	{
		return numberOfCandidates;
	}
	
	/*
	 * Creates an encrypted ballot from the user input. Fails if no legal candidates exist.
	 */
	public static Ballot getBallot(UserInputData userInputData) throws NoCandidateListException
	{
		//TODO Encrypt the ballot
		if(candidateList == null) {
			throw new NoCandidateListException();
		}
		boolean[] votes = getBooleanArrayFromCandidateId(userInputData.getCandidateId());
		return new Ballot(userInputData.getUserId(), userInputData.getPassword(), votes);
	}
	
	/*
	 * Returns a boolean array with indexes representing each candidate id and value representing whether or not he/she is voted for.
	 */
	private static boolean[] getBooleanArrayFromCandidateId(int candidateId)
	{
		boolean[] result = new boolean[candidateList.size()];
		result[candidateId] = true;
		return result;
	}
}
