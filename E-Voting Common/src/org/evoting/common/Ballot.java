package org.evoting.common;


public class Ballot
{
	// The id that identifies the voter among other voters.
	private int userId;
	// The hashed password.
	private String passwordHash;
	// Contains the encrypted vote
	private byte[][] vote;
	
	public Ballot(int userId, String passwordHash, byte[][] vote)
	{
		this.userId = userId;
		this.passwordHash = passwordHash;
		this.vote = vote;
	}

	public int getUserId() {
		return userId;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public byte[][] getVote() {
		return vote;
	}
}