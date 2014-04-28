package org.evoting.common;


public class Ballot
{
	private final String sid;
	// The id that identifies the voter among other voters.
	private final int userId;
	private final int electionId;
	// Contains the encrypted vote
	private final byte[][] vote;
	private final byte[] signature;
	
	public Ballot(String sid, int userId, int electionId, byte[][] vote, byte[] signature)
	{
		this.sid = sid;
		this.userId = userId;
		this.electionId = electionId;
		this.vote = vote;
		this.signature = signature;
	}
	
	public String getSid() {
		return sid;
	}

	public int getUserId() {
		return userId;
	}
	
	public int getElectionId() {
		return electionId;
	}

	public byte[][] getVote() {
		return vote;
	}
	
	public byte[] getSignature() {
		return signature;
	}
}