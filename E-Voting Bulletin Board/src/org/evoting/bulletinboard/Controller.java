package org.evoting.bulletinboard;

import java.util.List;

import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.embedding.RequestResponse;

import org.evoting.bulletinboard.exceptions.InvalidUserInformationException;
import org.evoting.common.Ballot;
import org.evoting.common.EncryptedBallot;
import org.evoting.common.EncryptedElectionOptions;
import org.evoting.database.entities.Vote;
import org.evoting.security.Security;

public class Controller extends JavaService {

	@RequestResponse
	public Boolean processVote(Value valueEncryptedBallot) {
		Ballot ballot = new EncryptedBallot(valueEncryptedBallot).getBallot();
		
		//Extract needed information from the ballot
		int userId = ballot.getUserId();
		String passwordHash = ballot.getPasswordHash();
		byte[][] encryptedVote = ballot.getVote();
		
		//Check the userId+passwordHash is a legal combination
		validateUser(userId, passwordHash);
		
		Model.processVote(userId, encryptedVote);
		
		return Boolean.TRUE;
	}

	/**
	 * @return Returns the electionOptionlist as a Value, used in Jolie 
	 */
	public Value getElectionOptions() {
		EncryptedElectionOptions electionOptions = Model.getEncryptedElectionOptions();
		return electionOptions.getValue();
	}
	
	public Value getPublicKeys() {
		if(!Security.keysGenerated()) {
			Security.generateKeys();
		}
		Value keys = Value.create();
		
		Model.setElGamalPublicKey(keys);
		Model.setRSAPublicKey(keys);
		
		return keys;
	}
	
	public Value getAllVotes() {
		List<Vote> allVotes = Model.getAllVotes();
		return Model.toValue(allVotes);
	}
	
	/**
	 * Validates the user
	 * @param userId The id of the user
	 * @param passwordHash The passwordHash of the user
	 * @return true if the userId and passwordHash matches otherwise false
	 */
	private boolean validateUser(int userId, String passwordHash) {
		if(userId < 0) {
			throw new InvalidUserInformationException("userId and passwordHash did not match.");
		}
		return true;
	}
	
	public static void main(String[] args) {
		Model.getAllVotes();
		Model.getAllVotes();
	}
}