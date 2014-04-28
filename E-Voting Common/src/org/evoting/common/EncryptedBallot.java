package org.evoting.common;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.common.exceptions.BadValueException;
import org.evoting.common.exceptions.InvalidVoteException;
import org.evoting.security.Security;

/*
 * Contains encrypted data, logic for encryption and logic for creation of value object.
 */
public class EncryptedBallot {
	private String sid;
	// All the fields below are ciphertext.
	private byte[] userId;
	private byte[] electionId;
	private byte[][] vote;
	private byte[] signature;
	
	/**
	 * @param userId The userId of the voter
	 * @param passwordHash The passwordHash of the voter
	 * @param timestamp The timestamp of the election
	 * @param vote The vote of the voter
	 * @throws InvalidVoteException Is thrown if the vote is invalid
	 */
	public EncryptedBallot(int userId, String sid, int electionId, int[] vote) throws InvalidVoteException {
		this.userId = encryptInteger(userId);
		this.sid = sid;
		this.electionId = encryptInteger(electionId);
		this.vote = encryptVote(vote);
		//TODO: generate signature
		//this.signature = Security.sign(Security.getBulletinBoardRSAPublicKey(), this.userId, this.passwordHash, this.electionId);
	}
	
	/**
	 * 
	 * @param encryptedBallot
	 * @throws InvalidVoteException
	 */
	public EncryptedBallot(Value encryptedBallot) throws InvalidVoteException {
		// Checks whether the value object has the required fields.
		if(!encryptedBallot.hasChildren(ValueIdentifiers.getUserId()) ||
			!encryptedBallot.hasChildren(ValueIdentifiers.getSid()) ||
			!encryptedBallot.hasChildren(ValueIdentifiers.getElectionId()) ||
			!encryptedBallot.hasChildren(ValueIdentifiers.getVote())) {
			throw new BadValueException();
		}
		this.userId = encryptedBallot.getFirstChild(ValueIdentifiers.getUserId()).byteArrayValue().getBytes();
		this.sid = encryptedBallot.getFirstChild(ValueIdentifiers.getSid()).strValue();
		this.electionId = encryptedBallot.getFirstChild(ValueIdentifiers.getElectionId()).byteArrayValue().getBytes();
		
		ValueVector valueVote = encryptedBallot.getChildren(ValueIdentifiers.getVote());
		vote = new byte[valueVote.size()][];
		int i = 0;
		for(Value v : valueVote) {
			vote[i] = v.byteArrayValue().getBytes();
			i++;
		}
		this.signature = encryptedBallot.getFirstChild(ValueIdentifiers.getSignature()).byteArrayValue().getBytes();
	}

	/**
	 * Encrypts the userId.
	 * @param userId The userId to encrypt
	 * @return The encrypted userId
	 */
	private byte[] encryptInteger(int value) {
		byte[] bytes = Converter.toByteArray(value);
		return Security.encryptRSA(bytes, Security.getBulletinBoardRSAPublicKey());
	}
	
	private int decryptInteger(byte[] value) {
		byte[] decryptedValue = Security.decryptRSA(value, Security.getBulletinBoardRSAPrivateKey());
		return Converter.toInt(decryptedValue);
	}
	
	/**
	 * Encrypts the vote.
	 * @param vote The vote to encrypt
	 * @return The encrypted vote
	 * @throws InvalidVoteException Is thrown if the vote is invalid
	 */
	private byte[][] encryptVote(int[] vote) {
		byte[][] result = new byte[vote.length][];
		if(voteIsValid(vote)) {
			for(int i = 0; i < vote.length; i++) {
				byte[] message = Group.getInstance().raiseGenerator((long)vote[i]).toByteArray();
				result[i] = Security.encryptElGamal(message, Security.getElgamalPublicKey());
			}
			return result;
		} else {
			throw new InvalidVoteException("Multiple electionOption-votes detected.");
		}
	}
	
	/**
	 * @param vote The vote to check
	 * @return True if the vote is valid
	 */
	private boolean voteIsValid(int[] vote) {
		boolean voted = false;
		for(int i = 0; i < vote.length; i++) {
			if(vote[i] == 1) {
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
		result.getNewChild(ValueIdentifiers.getUserId()).setValue(new ByteArray(userId));
		result.getNewChild(ValueIdentifiers.getSid()).setValue(sid);
		result.getNewChild(ValueIdentifiers.getElectionId()).setValue(new ByteArray(electionId));
		for(int i = 0; i < vote.length; i++) {
			result.getNewChild(ValueIdentifiers.getVote()).setValue(new ByteArray(vote[i]));
		}
		result.getNewChild(ValueIdentifiers.getSignature()).setValue(new ByteArray(signature));

		return result;
	}
	
	/**
	 * @return The decrypted version of the decryptedBallot
	 */
	public Ballot getBallot() {
		return new Ballot(sid, decryptInteger(userId), decryptInteger(electionId), vote, signature);
	}

}
