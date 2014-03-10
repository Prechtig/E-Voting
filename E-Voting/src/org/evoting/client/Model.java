package org.evoting.client;

import java.util.ArrayList;
import java.util.List;
import java.util.BitSet;

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

	public static Ballot getBallot(UserInputData userInputData)
	{
		BitSet candidateBits = getBitSetFromCandidateId(userInputData.getCandidateId());
		return new Ballot(userInputData.getUserId(), userInputData.getPassword(), candidateBits);
	}
	
	private static BitSet getBitSetFromCandidateId(int candidateId)
	{
		BitSet result = new BitSet();
		return result;
	}
}
