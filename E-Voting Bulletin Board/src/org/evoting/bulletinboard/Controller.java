package org.evoting.bulletinboard;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.embedding.RequestResponse;

import org.evoting.bulletinboard.exceptions.ElectionNotStartedException;
import org.evoting.common.AllVotesAuthority;
import org.evoting.common.Ballot;
import org.evoting.common.EncryptedBallot;
import org.evoting.common.EncryptedElectionOptions;
import org.evoting.common.Importer;
import org.evoting.common.ValueIdentifiers;
import org.evoting.common.Vote;
import org.evoting.security.Security;

public class Controller extends JavaService {
	static {
		Security.generateRSAKeys();
	}
	
	private static boolean electionRunning = false;
	
	private void validate(Value validation) {
		String message = validation.getFirstChild("message").strValue();
		byte[] signature = validation.getFirstChild("signature").byteArrayValue().getBytes();
		String hashedMessage = Security.hash(message);
		if(!hashedMessage.equals(Security.decryptRSA(signature, Security.getAuthorityRSAPublicKey()))) {//TODO: change to authority public key
			throw new RuntimeException(); //TODO: throw correct exception
		}
	}
	
	public Boolean startElection(Value value) {
		validate(value.getFirstChild("validator"));
		long endTime = value.getFirstChild(ValueIdentifiers.getEndTime()).longValue();
		Date endDate = new Date(endTime);
		Model.createNewElection(endDate);
		electionRunning = true;
		return Boolean.TRUE;
	}
	
	public Boolean stopElection(Value value) {
		validate(value.getFirstChild("validator"));
		electionRunning = false;
		return Boolean.TRUE;
	}
	
	public Boolean sendElectionOptionList(Value value) {
		//TODO: implement
		return Boolean.TRUE;
	}

	@RequestResponse
	/**
	 * Processes a vote, effectively saving it in the persistent storage
	 * if the userId + passwordHash matches, and the ballot is valid
	 * @param valueEncryptedBallot The vote to process, as a value from Jolie
	 * @return true if the vote is registered, otherwise false
	 */
	public Boolean processVote(Value valueEncryptedBallot) {
		if(!electionRunning) {
			throw new ElectionNotStartedException();
		}
		
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
	public Value getElectionStatus() {
		Value status = Value.create();
		
		Date endTime = Model.getElectionEndTime();
		
		boolean running = new Date().before(endTime);
		status.getNewChild("running").setValue(running);
		status.getNewChild("endTime").setValue(endTime.getTime());
		
		return status;
	}

	@RequestResponse
	/**
	 * @return Returns the electionOptionlist as a Value, used in Jolie 
	 */
	public Value getElectionOptions() {
		if(!electionRunning) {
			throw new ElectionNotStartedException();
		}
		
		EncryptedElectionOptions electionOptions = Model.getEncryptedElectionOptions();
		return electionOptions.getValue();
	}
	
	@RequestResponse
	/**
	 * @return Returns the elgamal + rsa public key
	 */
	public Value getPublicKeys() {
		if(!electionRunning) {
			throw new ElectionNotStartedException();
		}
		
		//TODO: Do we need this check?
		if(!Model.keysGenerated()) {
			throw new RuntimeException("The Bulletin Board has not received the keys from the Authority yet");
		}
		return Model.getPublicKeysValue();
	}
	
	@RequestResponse
	/**
	 * @return All votes in the database
	 */
	public Value getAllVotes() {
		if(!electionRunning) {
			throw new ElectionNotStartedException();
		}
		
		AllVotesAuthority allVotesAuthority = Model.getAllVotesAuthority();
		return allVotesAuthority.toValue();
	}
	
	@RequestResponse
	public boolean setKeys(Value publicKeys) {
		//TODO: Check that the public keys comes from the authority
		while(true) {
			try {
				Importer.importRsaPublicKey("");
				break;
			} catch(IOException e) {
				
			}
		}
		
		Value elgamalPublicKey = publicKeys.getFirstChild("elgamalPublicKey"); 
		Value elgamalPublicKeyParameters = elgamalPublicKey.getFirstChild("parameters");
		//Extract elgamal key
		String y = elgamalPublicKey.getFirstChild("y").strValue();
		String p = elgamalPublicKeyParameters.getFirstChild("p").strValue();
		String g = elgamalPublicKeyParameters.getFirstChild("g").strValue();
		int l    = elgamalPublicKeyParameters.getFirstChild("l").intValue();
		//Extract rsa key
		byte[] rsaPublicKey = publicKeys.getFirstChild("rsaPublicKey").byteArrayValue().getBytes();
		
		Model.setKeys(y, p, g, l, rsaPublicKey);
		return Boolean.TRUE;
	}
	
	public boolean loadAuthorityRsaPublicKey(String pathname) {
		
		return Boolean.TRUE;
	}
	
	public static void main(String[] args) {

	}
}