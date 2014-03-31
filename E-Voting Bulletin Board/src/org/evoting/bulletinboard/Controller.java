package org.evoting.bulletinboard;

import jolie.runtime.JavaService;
import jolie.runtime.Value;

import org.evoting.bulletinboard.exceptions.InvalidUserInformationException;
import org.evoting.common.Ballot;
import org.evoting.common.EncryptedBallot;
import org.evoting.common.EncryptedCandidateList;
import org.evoting.security.Security;

public class Controller extends JavaService {

	public boolean vote(Value valueEncryptedBallot) {
		Ballot ballot = new EncryptedBallot(valueEncryptedBallot).getBallot();
		
		//Extract needed information from the ballot
		int userId = ballot.getUserId();
		String passwordHash = ballot.getPasswordHash();
		byte[] encryptedVote = ballot.getVote();
		
		//Check the userId+passwordHash is a legal combination
		validateUser(userId, passwordHash);
		
		Model.processVote(userId, encryptedVote);
		
		return true;
	}

	/**
	 * @return Returns the candidatelist as a Value, used in Jolie 
	 */
	public Value getCandidateList() {
		EncryptedCandidateList candidateList = Model.getEncryptedCandidateList();
		return candidateList.getValue();
	}
	
	public Value getPublicKeys() {
		Security security = Security.getInstance();
		if(!Security.keysGenerated()) {
			Security.generateKeys();
		}
		Value keys = Value.create();
		
		Model.setElGamalPublicKey(security, keys);
		Model.setRSAPublicKey(security, keys);
		
		return keys;
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
		Controller controller = new Controller();
		controller.getPublicKeys();
		System.out.println();
	}
}