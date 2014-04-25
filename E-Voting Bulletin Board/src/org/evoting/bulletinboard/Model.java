package org.evoting.bulletinboard;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;

import org.evoting.bulletinboard.exceptions.InvalidUserInformationException;
import org.evoting.common.AllVotesAuthority;
import org.evoting.common.EncryptedElectionOptions;
import org.evoting.common.Vote;
import org.evoting.database.EntityManagerUtil;
import org.evoting.database.entities.Election;
import org.evoting.database.entities.ElectionOption;
import org.evoting.database.repositories.ElectionOptionRepository;
import org.evoting.database.repositories.ElectionRepository;
import org.evoting.database.repositories.VoteRepository;

public class Model {
	
	private static byte[] rsaPublicKey = null;
	private static boolean keysGenerated = false, keysValueGenerated = false;
	private static String y = null, p = null, g = null;
	private static int l;
	private static Value publicKeys = null;
	private static Election election;
	
	public static boolean keysGenerated() {
		return keysGenerated;
	}
	
	public static void setKeys(String y, String p, String g, int l, byte[] rsaPublicKey) {
		Model.y = y;
		Model.p = p;
		Model.g = g;
		Model.l = l;
		Model.rsaPublicKey = rsaPublicKey;
		keysGenerated = true;
	}
	
	/**
	 * Saves the vote in the database, overwriting the existing vote, if one is present
	 * @param userId The id of the user
	 * @param encryptedVote The encrypted ballot of the user
	 */
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
	
	/**
	 * @return The election options encrypted with the private key of the BB
	 */
	public static EncryptedElectionOptions getEncryptedElectionOptions() {
		EntityManager entMgr = beginDatabaseSession();
		
		ElectionOptionRepository cRepo = new ElectionOptionRepository(entMgr);
		List<ElectionOption> electionOptionsList = cRepo.findAll();
		
		EncryptedElectionOptions electionOptions = new EncryptedElectionOptions(electionOptionsList, Model.election.getId(), Model.election.getEndTime());
		
		endDatabaseSession(entMgr);
		return electionOptions;
	}
	
	/**
	 * @return All votes in the database
	 */
	public static AllVotesAuthority getAllVotesAuthority() {
		EntityManager entMgr = beginDatabaseSession();
		
		VoteRepository vRepo = new VoteRepository(entMgr);
		List<Vote> allVotes = vRepo.findAll();
		
		endDatabaseSession(entMgr);
		//TODO add date on votes and only add the latest vote.
		return new AllVotesAuthority(allVotes);
	}
	
	/**
	 * Converts a list of votes to a value defined in Types.iol (Jolie)
	 * @param allVotes The list of votes to be converted
	 * @return The value representing the list of votes
	 */
	public static Value toValue(List<Vote> allVotes) {
		Value result = Value.create();
		
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
	
	/**
	 * Returns the public keys as the value defined in Types.iol (Jolie)
	 * @return The given public keys as a Jolie value
	 */
	public static Value getPublicKeysValue() {
		if(!keysGenerated) {
			throw new RuntimeException("The Bulletin Board has not received the keys from the Authority yet");
		}
		if(!keysValueGenerated) {
			Value keys = Value.create();
			
			//Set the children regarding elgamal
			Value elgamalPublicKeyValue = keys.getNewChild("elgamalPublicKey");
			elgamalPublicKeyValue.getNewChild("y").setValue(y);
			Value elgamalParametersValue = elgamalPublicKeyValue.getNewChild("parameters");
			elgamalParametersValue.getNewChild("p").setValue(p);
			elgamalParametersValue.getNewChild("g").setValue(g);
			elgamalParametersValue.getNewChild("l").setValue(l);
			
			//Set the children regarding rsa
			keys.getNewChild("rsaPublicKey").setValue(new ByteArray(rsaPublicKey));
			
			publicKeys = keys;
			keysValueGenerated = true;
			return keys;
		}
		return publicKeys;
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
	
	/**
	 * @return An entitymanager with a open connection with an begun transaction
	 */
	private static EntityManager beginDatabaseSession() {
		EntityManager entMgr = EntityManagerUtil.getEntityManager();
		entMgr.getTransaction().begin();
		return entMgr;
	}
	
	/**
	 * Ends a database session, committing the transaction and closing the entitymanager
	 * @param entMgr The entitymanager holding the connection + transaction
	 */
	private static void endDatabaseSession(EntityManager entMgr) {
		entMgr.getTransaction().commit();
		entMgr.close();
	}

	public static Date getElectionEndTime() {
		if(Model.election == null) {
			return new Date(0);
		}
		return Model.election.getEndTime();
	}

	public static void createNewElection(Date endDate) {
		EntityManager entMgr = beginDatabaseSession();
		
		ElectionRepository er = new ElectionRepository(entMgr);
		int nextId = er.findNextId();
		
		Election election = new Election(nextId, endDate);
		Model.election = election;
		
		entMgr.persist(election);
		
		endDatabaseSession(entMgr);
	}
}
