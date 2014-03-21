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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean[] getVotes() {
		return votes;
	}

	public void setVotes(boolean[] votes) {
		this.votes = votes;
	}
}