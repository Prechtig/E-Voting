package org.evoting.common;

import jolie.runtime.Value;

/*
 * Contains encrypted data, logic for encryption and logic for creation of value object.
 */
public class EncryptedBallot {
	// All the fields below are ciphertext.
	private byte[] userId;
	private byte[] passwordHash;
	private byte[] timeStamp;
	private byte[] vote;
	
	public EncryptedBallot(int userId, String passwordHash, byte[] timeStamp, boolean[] vote) {
		this.userId = encryptUserId(userId);
		this.passwordHash = encryptPasswordHash(passwordHash);
		this.timeStamp = timeStamp;
		this.vote = encryptVote(vote);
	}
	
	/*
	 * Encrypts the userId.
	 */
	private byte[] encryptUserId(int userId)
	{
		byte[] result = new byte[0];
		return result;
	}
	
	/*
	 * Encrypts the password hash.
	 */
	private byte[] encryptPasswordHash(String password)
	{
		byte[] result = new byte[0];
		return result;
	}
	
	/*
	 * Encrypts the vote.
	 */
	private byte[] encryptVote(boolean[] vote)
	{
		byte[] result = new byte[0];
		return result;
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

