package org.evoting.client;

import java.util.ArrayList;
import org.evoting.client.exceptions.NoCandidateListException;

public class Model
{ 
	private static ArrayList<String> candidateList;
	private static int numberOfCandidates = 0;
	
	public static void setCandidates(String[] candidates)
	{
		candidateList = new ArrayList<String>();
		numberOfCandidates = candidates.length;
		for(String s : candidates) {
			candidateList.add(s);
		}
	}
	
	public static int getNumberOfCandidates()
	{
		return numberOfCandidates;
	}

	public static Ballot getBallot(UserInputData userInputData) throws NoCandidateListException
	{
		if(candidateList == null) {
			throw new NoCandidateListException();
		}
		boolean[] votes = getBooleanArrayFromCandidateId(userInputData.getCandidateId());
		return new Ballot(userInputData.getUserId(), userInputData.getPassword(), votes);
	}
	
	private static boolean[] getBooleanArrayFromCandidateId(int candidateId)
	{
		boolean[] result = new boolean[candidateList.size()];
		result[candidateId] = true;
		return result;
	}
}
