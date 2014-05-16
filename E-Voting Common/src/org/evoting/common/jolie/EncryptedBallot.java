package org.evoting.common.jolie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.common.Converter;
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
	private byte[][] vote;
	private byte[] signature;
	
	/**
	 * @param userId The userId of the voter
	 * @param passwordHash The passwordHash of the voter
	 * @param vote The vote of the voter
	 * @throws InvalidVoteException Is thrown if the vote is invalid
	 */
	public EncryptedBallot(String userId, String sid, long[] vote) throws InvalidVoteException {
		this.userId = encrypt(userId);
		this.sid = sid;
		this.vote = encryptVote(vote);
		//TODO: generate signature
		this.signature = Security.sign(Security.getBulletinBoardRSAPublicKey(), toByteArray());
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
			!encryptedBallot.hasChildren(ValueIdentifiers.getVote())) {
			throw new BadValueException();
		}
		this.userId = encryptedBallot.getFirstChild(ValueIdentifiers.getUserId()).byteArrayValue().getBytes();
		this.sid = encryptedBallot.getFirstChild(ValueIdentifiers.getSid()).strValue();
		
		ValueVector valueVote = encryptedBallot.getChildren(ValueIdentifiers.getVote());
		vote = new byte[valueVote.size()][];
		int i = 0;
		for(Value v : valueVote) {
			vote[i] = v.byteArrayValue().getBytes();
			i++;
		}
		this.signature = encryptedBallot.getFirstChild(ValueIdentifiers.getSignature()).byteArrayValue().getBytes();
	}
	
	private byte[] encrypt(String value) {
		return Security.encryptRSA(value, Security.getBulletinBoardRSAPublicKey());
	}
	
	private String decryptString(byte[] value) {
		return new String(Security.decryptRSA(value, Security.getBulletinBoardRSAPrivateKey()));
	}
	
	/**
	 * Encrypts the vote.
	 * @param vote The vote to encrypt
	 * @return The encrypted vote
	 * @throws InvalidVoteException Is thrown if the vote is invalid
	 */
	private byte[][] encryptVote(long[] vote) {
		byte[][] result = new byte[vote.length][];
		if(voteIsValid(vote)) {
			for(int i = 0; i < vote.length; i++) {
				result[i] = Security.encryptExponentialElgamal(vote[i], Security.getElgamalPublicKey());
			}
			return result;
		} else {
			throw new InvalidVoteException("Multiple election option choices detected.");
		}
	}
	
	/**
	 * @param vote The vote to check
	 * @return True if the vote is valid
	 */
	private boolean voteIsValid(long[] vote) {
		boolean voted = false;
		for(int i = 0; i < vote.length; i++) {
			if(vote[i] == 1) {
				if(voted) {
					return false;
				}
				voted = true;
			} else if(vote[i] != 0) {
				return false;
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
		return new Ballot(sid, decryptString(userId), vote, signature);
	}
	
	private byte[] toByteArray() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stream.write(userId);
			stream.write(sid.getBytes());
			stream.write(Converter.toByteArray(vote));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stream.toByteArray();
	}

}
