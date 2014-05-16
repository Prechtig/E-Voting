package org.evoting.common.jolie;


public class Ballot
{
	private final String sid;
	// The id that identifies the voter among other voters.
	private final String userId;
	// Contains the encrypted vote
	private final byte[][] vote;
	private final byte[] signature;
	
	public Ballot(String sid, String userId, byte[][] vote, byte[] signature)
	{
		this.sid = sid;
		this.userId = userId;
		this.vote = vote;
		this.signature = signature;
	}
	
	public String getSid() {
		return sid;
	}

	public String getUserId() {
		return userId;
	}

	public byte[][] getVote() {
		return vote;
	}
	
	public byte[] getSignature() {
		return signature;
	}
}