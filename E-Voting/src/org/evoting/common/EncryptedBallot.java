package org.evoting.common;

import jolie.runtime.Value;

import org.evoting.common.exceptions.InvalidVoteException;
import org.evoting.security.Security;

/*
 * Contains encrypted data, logic for encryption and logic for creation of value object.
 */
public class EncryptedBallot {
	// All the fields below are ciphertext.
	private byte[] userId;
	private byte[] passwordHash;
	private byte[] timeStamp;
	private byte[] vote;
	private Security security;

	/**
	 * @param userId The userId of the voter
	 * @param passwordHash The passwordHash of the voter
	 * @param timeStamp The timestamp of the election
	 * @param vote The vote of the voter
	 * @throws InvalidVoteException Is thrown if the vote is invalid
	 */
	public EncryptedBallot(int userId, String passwordHash, byte[] timeStamp, boolean[] vote) throws InvalidVoteException {
		this.userId = encryptUserId(userId);
		this.passwordHash = encryptPasswordHash(passwordHash);
		this.timeStamp = timeStamp;
		this.vote = encryptVote(vote);
		security = new Security();
	}

	/**
	 * Encrypts the userId.
	 * @param userId The userId to encrypt
	 * @return The encrypted userId
	 */
	private byte[] encryptUserId(int userId) {
		return security.encryptElGamal(String.valueOf(userId), null); //TODO: Set the key
	}

	/**
	 * Encrypts the password hash.
	 * @param password The passwordHash to encrypt
	 * @return The encrypted passwordHash
	 */
	private byte[] encryptPasswordHash(String passwordHash) {
		return security.encryptElGamal(passwordHash, null); //TODO: Set the key
	}

	/**
	 * Encrypts the vote.
	 * @param vote The vote to encrypt
	 * @return The encrypted vote
	 * @throws InvalidVoteException Is thrown if the vote is invalid
	 */
	private byte[] encryptVote(boolean[] vote) throws InvalidVoteException {
		if(voteIsValid(vote)) {
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < vote.length; i++) {
				if(vote[0]) {
					sb.append("1");
				} else {
					sb.append("0");
				}
			}
			return security.encryptElGamal(sb.toString(), null); //TODO: Set the key
		} else {
			throw new InvalidVoteException("Multiple candidate-votes detected.");
		}
	}
	
	/**
	 * @param vote The vote to check
	 * @return True if the vote is valid
	 */
	private boolean voteIsValid(boolean[] vote) {
		boolean voted = false;
		for(int i = 0; i < vote.length; i++) {
			if(vote[i]) {
				if(voted) {
					return false;
				}
				voted = true;
			}
		}
		return true;
	}

	/**
	 * Creates a value representation of the ballot, which has a tree structure
	 * that is XML convertible.
	 * @return The encrypted ballot converted to a Value, used in Jolie
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
