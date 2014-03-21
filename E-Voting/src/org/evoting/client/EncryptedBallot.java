package org.evoting.client;

import jolie.runtime.Value;

/*
 * Contains encrypted data, logic for encryption and logic for creation of value object.
 */
public class EncryptedBallot {
	// All the fields below are ciphertext.
	private String userId;
	private String passwordHash;
	private String timeStamp;
	private String vote;
	
	public EncryptedBallot(int userId, String passwordHash, String timeStamp, boolean[] vote) {
		this.userId = encryptUserId(userId);
		this.passwordHash = encryptPasswordHash(passwordHash);
		this.timeStamp = timeStamp;
		this.vote = encryptVote(vote);
	}
	
	/*
	 * Encrypts the userId.
	 */
	private String encryptUserId(int userId)
	{
		return "";
	}
	
	/*
	 * Encrypts the password hash.
	 */
	private String encryptPasswordHash(String password)
	{
		return "";
	}
	
	/*
	 * Encrypts the vote.
	 */
	private String encryptVote(boolean[] vote)
	{
		return "";
	}
	
	/*
	 * Creates a value representation of the ballot, which has a tree structure that is XML convertible.
	 */
	public Value getValue() {
		Value result = Value.create();
		result.getNewChild("userId").setValue(userId);
		result.getNewChild("passwordHash").setValue(passwordHash);
		result.getNewChild("timeStamp").setValue(timeStamp);
		result.getNewChild("vote").setValue(vote);
		
		return result;
	}
}
