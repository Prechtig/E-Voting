package org.evoting.bulletinboard;

import java.io.IOException;
import java.util.Date;

import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;
import jolie.runtime.embedding.RequestResponse;

import org.evoting.bulletinboard.exceptions.ElectionNotStartedException;
import org.evoting.common.AnonymousVoteList;
import org.evoting.common.Ballot;
import org.evoting.common.EncryptedBallot;
import org.evoting.common.EncryptedElectionOptions;
import org.evoting.common.Importer;
import org.evoting.common.ValueIdentifiers;
import org.evoting.database.entities.ElectionOption;
import org.evoting.security.Security;

public class Controller extends JavaService {
	static {
		Security.generateRSAKeys();
	}
	
	private static Date electionStartDate;
	private static Date electionEndDate;
	
	private void validate(Value validation) {
		String message = validation.getFirstChild("message").strValue();
		byte[] signature = validation.getFirstChild("signature").byteArrayValue().getBytes();
		String hashedMessage = Security.hash(message);
		if(!hashedMessage.equals(Security.decryptRSA(signature, Security.getAuthorityRSAPublicKey()))) {
			throw new RuntimeException(); //TODO: throw correct exception
		}
	}
	
	@RequestResponse
	public Boolean sendElectionOptionList(Value electionOptions) {
		validate(electionOptions.getFirstChild(ValueIdentifiers.getValidator()));
		
		ValueVector options = electionOptions.getChildren(ValueIdentifiers.getElectionOptions());
		
		ElectionOption[] arr = new ElectionOption[options.size()];
		for(int i = 0; i < options.size(); i++) {
			Value currentOption = options.get(i);
			
			int id = currentOption.getFirstChild(ValueIdentifiers.getId()).intValue();
			String name = currentOption.getFirstChild(ValueIdentifiers.getName()).strValue();
			int partyId = currentOption.getFirstChild(ValueIdentifiers.getPartyId()).intValue();
			
			arr[id] = new ElectionOption(id, name, partyId);
		}
		Model.setElectionOptions(arr);
		
		return Boolean.TRUE;
	}
	
	
	@RequestResponse
	public Boolean startElection(Value value) {
		validate(value.getFirstChild("validator"));
		
		long startTime = value.getFirstChild(ValueIdentifiers.getStartTime()).longValue();
		long endTime = value.getFirstChild(ValueIdentifiers.getEndTime()).longValue();
		
		electionStartDate = new Date(startTime);
		electionEndDate = new Date(endTime);
		Model.createNewElection(electionStartDate, electionEndDate);
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
		if(!electionIsRunning()) {
			throw new ElectionNotStartedException();
		}
		//TODO: Is this implementation correct?
		Ballot ballot = new EncryptedBallot(valueEncryptedBallot).getBallot();
		
		String userId = ballot.getUserId();
		byte[][] encryptedVote = ballot.getVote();
		
		Model.processVote(userId, encryptedVote);
		
		return Boolean.TRUE;
	}
	
	@RequestResponse
	public Boolean login(Value userInformation) {
		int userId = userInformation.getFirstChild(ValueIdentifiers.getUserId()).intValue();
		String passwordHash = userInformation.getFirstChild(ValueIdentifiers.getPasswordHash()).strValue();
		Model.validateUser(userId, passwordHash);
		
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
		if(!electionIsRunning()) {
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
		if(!electionIsRunning()) {
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
		if(!electionIsRunning()) {
			throw new ElectionNotStartedException();
		}
		
		AnonymousVoteList allVotesAuthority = Model.getAllVotes();
		return allVotesAuthority.toValue();
	}
	
	@RequestResponse
	/**
	 * @param validator
	 * @return
	 */
	public Value getAllVotesAuthority(Value validator) {
		if(!electionIsRunning()) {
			throw new ElectionNotStartedException();
		}
		validate(validator);
		
		AnonymousVoteList allVotesAuthority = Model.getAllVotesAuthority();
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
	
	private boolean electionIsRunning() {
		Date currentTime = new Date(System.currentTimeMillis());
		return currentTime.after(electionStartDate) && currentTime.before(electionEndDate);
	}
	
	public static void main(String[] args) {

	}
}