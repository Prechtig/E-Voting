package org.evoting.client;

import java.util.ArrayList;
import java.util.List;
import java.util.BitSet;

import org.evoting.client.Exceptions.NoCandidateListException;

public class Model
{ 
	private static ArrayList<String> candidateList;
	
	public static void setCandidates(String[] candidates)
	{
		candidateList = new ArrayList<String>();
		for(String s : candidates) {
			candidateList.add(s);
		}
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
