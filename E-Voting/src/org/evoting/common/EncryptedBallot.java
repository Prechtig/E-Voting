package org.evoting.common;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;

import org.evoting.client.Ballot;
import org.evoting.common.exceptions.InvalidVoteException;
import org.evoting.security.Security;

/*
 * Contains encrypted data, logic for encryption and logic for creation of value object.
 */
public class EncryptedBallot {
	// All the fields below are ciphertext.
	private byte[] userId;
	private byte[] passwordHash;
	private byte[] timestamp;
	private byte[] vote;
	
	private final String valueUserId = "userId";
	private final String valuePasswordHash = "passwordHash";
	private final String valueTimestamp = "timestamp";
	private final String valueVote = "vote";
	
	private Security security;

	/**
	 * @param userId The userId of the voter
	 * @param passwordHash The passwordHash of the voter
	 * @param timestamp The timestamp of the election
	 * @param vote The vote of the voter
	 * @throws InvalidVoteException Is thrown if the vote is invalid
	 */
	public EncryptedBallot(int userId, String passwordHash, byte[] timeStamp, boolean[] vote) throws InvalidVoteException {
		this.userId = encryptUserId(userId);
		this.passwordHash = encryptPasswordHash(passwordHash);
		this.timestamp = timeStamp;
		this.vote = encryptVote(vote);
		security = new Security();
	}
	
	/**
	 * 
	 * @param encryptedBallot
	 * @throws InvalidVoteException
	 */
	public EncryptedBallot(Value encryptedBallot) throws InvalidVoteException {
		if(!encryptedBallot.hasChildren(valueUserId) ||
			!encryptedBallot.hasChildren(valuePasswordHash) ||
			!encryptedBallot.hasChildren(valueTimestamp) ||
			!encryptedBallot.hasChildren(valueVote)) {
			throw new InvalidVoteException("A required children was missing");
		}
		this.userId = encryptedBallot.getFirstChild(valueUserId).byteArrayValue().getBytes();
		this.passwordHash = encryptedBallot.getFirstChild(valuePasswordHash).byteArrayValue().getBytes();
		this.timestamp = encryptedBallot.getFirstChild(valueTimestamp).byteArrayValue().getBytes();
		this.vote = encryptedBallot.getFirstChild(valueVote).byteArrayValue().getBytes();
	}

	/**
	 * Encrypts the userId.
	 * @param userId The userId to encrypt
	 * @return The encrypted userId
	 */
	private byte[] encryptUserId(int userId) {
		byte[] value = Converter.toByteArray(userId);
		return security.encryptElGamal(value, null); //TODO: Set the key
	}
	
	/**
	 * @return The decrypted userId
	 */
	private int decryptUserId() {
		byte[] value = security.decryptElgamal(userId, null); //TODO: Set the key 
		return Converter.toInt(value);
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
	 * @return The decrypted passwordHash
	 */
	private String decryptPasswordHash() {
		byte[] value = security.decryptElgamal(passwordHash, null); //TODO: Set the key
		return new String(value);
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
		result.getNewChild(valueUserId).setValue(new ByteArray(userId));
		result.getNewChild(valuePasswordHash).setValue(new ByteArray(passwordHash));
		result.getNewChild(valueTimestamp).setValue(new ByteArray(timestamp));
		result.getNewChild(valueVote).setValue(new ByteArray(vote));

		return result;
	}
	
	/**
	 * @return The decrypted version of the decryptedBallot
	 */
	public Ballot getBallot() {
		return new Ballot(decryptUserId(), decryptPasswordHash(), vote);
	}
}