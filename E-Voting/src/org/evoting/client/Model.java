package org.evoting.client;

import java.util.ArrayList;
import java.util.List;

import org.evoting.client.exceptions.NoCandidateListException;
import org.evoting.common.EncryptedBallot;
import org.evoting.database.entities.Candidate;

/*
 * Contains data about the candidates and logic that supports ballot creation.
 */
public class Model
{ 
	// List of candidate names. The index is equal to the candidate id.
	private static List<String> candidateNames;
	// The time stamp that marks the candidate list.
	private static byte[] candidateListTime;
	// The number of candidates contained in the candidateList.
	private static int numberOfCandidates = 0;
	
	/*
	 * Sets the list of available candidates.
	 */
	public static void setCandidates(CandidateList candidates)
	{
		candidateNames = candidates.getCandidates();
		numberOfCandidates = candidateNames.size();
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
		if(candidateNames == null) {
			throw new NoCandidateListException();
		}
		boolean[] votes = getBooleanArrayFromCandidateId(userInputData.getCandidateId());
		return new Ballot(userInputData.getUserId(), userInputData.getPassword(), votes);
	}
	
	/*
	 * Encrypts the ballot and adds the time stamp of the candidate list.
	 */
	public static EncryptedBallot getEncryptedBallot(Ballot ballot)
	{
		return new EncryptedBallot(ballot.getUserId(), ballot.getPassword(), candidateListTime, ballot.getVotes());
	}
	
	/*
	 * Returns a boolean array with indexes representing each candidate id and value representing whether or not he/she is voted for.
	 */
	private static boolean[] getBooleanArrayFromCandidateId(int candidateId)
	{
		boolean[] result = new boolean[candidateNames.size()];
		result[candidateId] = true;
		return result;
	}
}
