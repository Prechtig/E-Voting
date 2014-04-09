package org.evoting.bulletinboard;

import java.util.List;

import javax.persistence.EntityManager;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.bulletinboard.exceptions.InvalidUserInformationException;
import org.evoting.common.EncryptedElectionOptions;
import org.evoting.database.EntityManagerUtil;
import org.evoting.database.entities.ElectionOption;
import org.evoting.database.entities.Timestamp;
import org.evoting.database.entities.Vote;
import org.evoting.database.repositories.ElectionOptionRepository;
import org.evoting.database.repositories.TimestampRepository;
import org.evoting.database.repositories.VoteRepository;

public class Model {
	
	public static void processVote(int userId, byte[][] encryptedVote) {
		EntityManager entMgr = beginDatabaseSession();

		VoteRepository vr = new VoteRepository(entMgr);
		//See if the user has already voted
		Vote vote = vr.findById(userId);
		
		if(vote == null) {
			//Persist the vote if the user hasn't voted yet
			vote = new Vote(userId, encryptedVote);
			entMgr.persist(vote);
		} else {
			//Update the vote if the user has voted before
			vote.setEncryptedVote(encryptedVote);
		}
		endDatabaseSession(entMgr);
	}
	
	public static EncryptedElectionOptions getEncryptedElectionOptions() {
		EntityManager entMgr = beginDatabaseSession();
		
		ElectionOptionRepository cRepo = new ElectionOptionRepository(entMgr);
		List<ElectionOption> electionOptionsList = cRepo.findAll();
		
		TimestampRepository tRepo = new TimestampRepository(entMgr);
		Timestamp timestamp = tRepo.findTime();
		
		EncryptedElectionOptions electionOptions = new EncryptedElectionOptions(electionOptionsList, timestamp.getTime());
		
		endDatabaseSession(entMgr);
		return electionOptions;
	}
	
	public static List<Vote> getAllVotes() {
		EntityManager entMgr = beginDatabaseSession();
		
		VoteRepository vRepo = new VoteRepository(entMgr);
		List<Vote> allVotes = vRepo.findAll();
		
		endDatabaseSession(entMgr);
		
		return allVotes;
	}
	
	public static Value toValue(List<Vote> allVotes) {
		Value result = Value.create();
		if(allVotes.size() > 0) {
			result.getNewChild("numberOfElectionOptions").setValue(allVotes.get(0).getEncryptedVote().length);
		}
		
		for(Vote v : allVotes) {
			Value votes = result.getNewChild("votes");
			for(int i = 0; i < v.getEncryptedVote().length; i++) {
				Value vote = votes.getNewChild("vote");
				vote.getNewChild("electionOptionId").setValue(i);
				vote.getNewChild("encryptedVote").setValue(new ByteArray(v.getEncryptedVote()[i]));
			}
		}
		return result;
	}
	
	public static Value toValue(ElGamalPublicKeyParameters elgamalPublicKey, ElGamalParameters elgamalParameters, byte[] rsaPublicKey) {
		Value keys = Value.create();
		
		//Set the children regarding elgamal
		Value elgamalPublicKeyValue = keys.getNewChild("elgamalPublicKey");
		elgamalPublicKeyValue.getNewChild("y").setValue(elgamalPublicKey.getY().toString());
		Value elgamalParametersValue = elgamalPublicKeyValue.getNewChild("parameters");
		elgamalParametersValue.getNewChild("p").setValue(elgamalParameters.getP().toString());
		elgamalParametersValue.getNewChild("g").setValue(elgamalParameters.getG().toString());
		elgamalParametersValue.getNewChild("l").setValue(elgamalParameters.getL());
		
		//Set the children regarding rsa
		keys.getNewChild("rsaPublicKey").setValue(new ByteArray(rsaPublicKey));
		
		return keys;
	}
	
	/**
	 * Validates the user
	 * @param userId The id of the user
	 * @param passwordHash The passwordHash of the user
	 * @return true if the userId and passwordHash matches otherwise false
	 */
	public static boolean validateUser(int userId, String passwordHash) {
		if(userId < 0) {
			throw new InvalidUserInformationException("userId and passwordHash did not match.");
		}
		return true;
	}
	
	private static EntityManager beginDatabaseSession() {
		EntityManager entMgr = EntityManagerUtil.getEntityManager();
		entMgr.getTransaction().begin();
		return entMgr;
	}
	
	private static void endDatabaseSession(EntityManager entMgr) {
		entMgr.getTransaction().commit();
		entMgr.close();
	}
}
