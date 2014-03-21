package org.evoting.client;

import jolie.runtime.Value;

public class Ballot
{
	// The id that identifies the voter among other voters.
	private int userId;
	// The hashed password.
	private String password;
	// Contains false for candidates that are not voted for and true for candidates that are voted for.
	private boolean[] votes;
	
	public Ballot(int userId, String password, boolean[] votes)
	{
		this.userId = userId;
		this.password = password;
		this.votes = votes;
	}
	
	/*
	 * Creates a value representation of the ballot, which has a tree structure that is XML convertible.
	 */
	public Value getValue() {
		Value result = Value.create();
		result.getNewChild("userId").setValue(userId);
		result.getNewChild("password").setValue(password);
		for(int i = 0; i < votes.length; i++) {
			result.getNewChild("vote").setValue(votes[i]);;
		}
		
		return result;
	}
}