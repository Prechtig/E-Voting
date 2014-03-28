package org.evoting.bulletinboard;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import jolie.runtime.ByteArray;
import jolie.runtime.JavaService;
import jolie.runtime.Value;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.bulletinboard.exceptions.InvalidUserInformationException;
import org.evoting.common.Ballot;
import org.evoting.common.EncryptedBallot;
import org.evoting.common.EncryptedCandidateList;
import org.evoting.database.EntityManagerUtil;
import org.evoting.database.entities.Candidate;
import org.evoting.database.entities.Timestamp;
import org.evoting.database.entities.Vote;
import org.evoting.database.repositories.CandidateRepository;
import org.evoting.database.repositories.TimestampRepository;
import org.evoting.database.repositories.VoteRepository;
import org.evoting.security.Security;

public class Controller extends JavaService {

	public boolean processVote(Value valueEncryptedBallot) {
		Ballot ballot = new EncryptedBallot(valueEncryptedBallot).getBallot();
		
		//Extract needed information from the ballot
		int userId = ballot.getUserId();
		String passwordHash = ballot.getPasswordHash();
		byte[] encryptedVote = ballot.getVote();
		
		//Check the userId+passwordHash is a legal combination
		if(!validateUser(userId, passwordHash)) {
			throw new InvalidUserInformationException("userId and passwordHash did not match.");
		}
		
		EntityManager entMgr = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
		VoteRepository vr = new VoteRepository(entMgr);
		//Make a transaction
		EntityTransaction transaction = entMgr.getTransaction();
		//Begin the transaction
		transaction.begin();
		
		//See if the user has already voted
		Vote vote = vr.findById(userId);
		
		if(vote == null) {
			//Persist the vote if the user hasn't voted yet
			entMgr.persist(vote);
		} else {
			//Update the vote if the user has voted before
			vote.setEncryptedVote(encryptedVote);
		}
		//Commit the transaction
		transaction.commit();
		
		return true;
	}

	/**
	 * @return Returns the candidatelist as a Value, used in Jolie 
	 */
	public Value getCandidateList() {
		EntityManager entMgr = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
		EntityTransaction transaction = entMgr.getTransaction();
		transaction.begin();
		
		CandidateRepository cRepo = new CandidateRepository(entMgr);
		List<Candidate> candidates = cRepo.findAll();
		
		TimestampRepository tRepo = new TimestampRepository(entMgr);
		Timestamp timestamp = tRepo.findTime();
		
		EncryptedCandidateList candidateList = new EncryptedCandidateList(candidates, timestamp.getTime());
		
		//Close the connection to the persistant storage
		transaction.commit();
		entMgr.close();

		return candidateList.getValue();
	}
	
	public Value getPublicKeys() {
		Security security = new Security();
		if(!Security.keysGenerated()) {
			Security.generateKeys();
		}
		ElGamalPublicKeyParameters elgamalPublicKey = security.getElgamalPublicKey();
		ElGamalParameters elgamalParameters = elgamalPublicKey.getParameters();
		
		Value keys = Value.create();
		
		Value elgamalPublicKeyValue = keys.getNewChild("elgamalPublicKey");
		elgamalPublicKeyValue.getNewChild("y").setValue(elgamalPublicKey.getY().toString());
		Value elgamalParametersValue = elgamalPublicKeyValue.getNewChild("parameters");
		elgamalParametersValue.getNewChild("p").setValue(elgamalParameters.getP().toString());
		elgamalParametersValue.getNewChild("g").setValue(elgamalParameters.getG().toString());
		elgamalParametersValue.getNewChild("l").setValue(elgamalParameters.getL());
		
		byte[] rsaPublicKey = security.getRSAPublicKeyBytes();
		keys.getNewChild("rsaPublicKey").setValue(new ByteArray(rsaPublicKey));
		
		return keys;
	}
	
	/**
	 * Validates the user
	 * @param userId The id of the user
	 * @param passwordHash The passwordHash of the user
	 * @return true if the userId and passwordHash matches otherwise false
	 */
	private boolean validateUser(int userId, String passwordHash) {
		return true;
	}
	
	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.getPublicKeys();
		System.out.println();
	}
}