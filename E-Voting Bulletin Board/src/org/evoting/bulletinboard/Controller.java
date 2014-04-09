package org.evoting.bulletinboard;

import java.util.List;

import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.embedding.RequestResponse;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.Ballot;
import org.evoting.common.EncryptedBallot;
import org.evoting.common.EncryptedElectionOptions;
import org.evoting.database.entities.Vote;
import org.evoting.security.Security;

public class Controller extends JavaService {

	@RequestResponse
	/**
	 * Processes a vote, effectively saving it in the persistent storage
	 * if the userId + passwordHash matches, and the ballot is valid
	 * @param valueEncryptedBallot The vote to process, as a value from Jolie
	 * @return true if the vote is registered, otherwise false
	 */
	public Boolean processVote(Value valueEncryptedBallot) {
		Ballot ballot = new EncryptedBallot(valueEncryptedBallot).getBallot();
		
		//Extract needed information from the ballot
		int userId = ballot.getUserId();
		String passwordHash = ballot.getPasswordHash();
		byte[][] encryptedVote = ballot.getVote();
		
		//Check the userId+passwordHash is a legal combination
		Model.validateUser(userId, passwordHash);
		
		Model.processVote(userId, encryptedVote);
		
		return Boolean.TRUE;
	}

	@RequestResponse
	/**
	 * @return Returns the electionOptionlist as a Value, used in Jolie 
	 */
	public Value getElectionOptions() {
		EncryptedElectionOptions electionOptions = Model.getEncryptedElectionOptions();
		return electionOptions.getValue();
	}
	
	@RequestResponse
	/**
	 * @return Returns the elgamal + rsa public key
	 */
	public Value getPublicKeys() {
		if(!Security.keysGenerated()) {
			Security.generateKeys();
		}
		
		ElGamalPublicKeyParameters elgamalPublicKey = Security.getElgamalPublicKey();
		ElGamalParameters elgamalParameters = elgamalPublicKey.getParameters();
		byte[] rsaPublicKey = Security.getRSAPublicKeyBytes();
		
		return Model.toValue(elgamalPublicKey, elgamalParameters, rsaPublicKey);
	}
	
	@RequestResponse
	/**
	 * @return All votes in the database
	 */
	public Value getAllVotes() {
		List<Vote> allVotes = Model.getAllVotes();
		return Model.toValue(allVotes);
	}
	
	public static void main(String[] args) {
		Model.getAllVotes();
		Model.getAllVotes();
	}
}