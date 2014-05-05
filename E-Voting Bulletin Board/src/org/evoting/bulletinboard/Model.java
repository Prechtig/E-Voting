package org.evoting.bulletinboard;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.bulletinboard.exceptions.InvalidUserInformationException;
import org.evoting.common.AnonymousVoteList;
import org.evoting.common.EncryptedElectionOptions;
import org.evoting.common.ValueIdentifiers;
import org.evoting.database.EntityManagerUtil;
import org.evoting.database.entities.Election;
import org.evoting.database.entities.ElectionOption;
import org.evoting.database.entities.Vote;
import org.evoting.database.repositories.ElectionOptionRepository;
import org.evoting.database.repositories.VoteRepository;
import org.evoting.security.Security;

public class Model {
	
	private static Value publicKeys = null;
	private static Election election;
	
	/**
	 * Saves the vote in the database, overwriting the existing vote, if one is present
	 * @param userId The id of the user
	 * @param encryptedVote The encrypted ballot of the user
	 */
	public static void processVote(String userId, byte[][] encryptedVote) {
		EntityManager entMgr = beginDatabaseSession();
		
		Vote vote = new Vote(userId, encryptedVote, System.currentTimeMillis());
		entMgr.persist(vote);
		
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
	public static AnonymousVoteList getAllVotesAuthority() {
		EntityManager entMgr = beginDatabaseSession();
		
		VoteRepository vRepo = new VoteRepository(entMgr);
		List<Vote> allVotes = vRepo.findAllValid();
		
		endDatabaseSession(entMgr);
		return new AnonymousVoteList(allVotes);
	}
	
	public static AnonymousVoteList getAllVotes() {
		EntityManager entMgr = beginDatabaseSession();
		
		VoteRepository vRepo = new VoteRepository(entMgr);
		List<Vote> allVotes = vRepo.findAll();
		
		endDatabaseSession(entMgr);
		return new AnonymousVoteList(allVotes);
	}
	
	/**
	 * Returns the public keys as the value defined in Types.iol (Jolie)
	 * @return The given public keys as a Jolie value
	 */
	public static Value getPublicKeysValue() {
		if(publicKeys == null) {
			Value keys = Value.create();
			ElGamalPublicKeyParameters elgamalPubKey = Security.getElgamalPublicKey();
			ElGamalParameters elgamalParams = elgamalPubKey.getParameters();
			//Set the children regarding elgamal
			Value elgamalPublicKeyValue = keys.getNewChild(ValueIdentifiers.getElgamalPublicKey());
			elgamalPublicKeyValue.getNewChild(ValueIdentifiers.getY()).setValue(elgamalPubKey.getY().toString());
			Value elgamalParametersValue = elgamalPublicKeyValue.getNewChild(ValueIdentifiers.getParameters());
			elgamalParametersValue.getNewChild(ValueIdentifiers.getP()).setValue(elgamalParams.getP().toString());
			elgamalParametersValue.getNewChild(ValueIdentifiers.getG()).setValue(elgamalParams.getG().toString());
			elgamalParametersValue.getNewChild(ValueIdentifiers.getL()).setValue(elgamalParams.getL());
			
			//Set the children regarding rsa
			keys.getNewChild(ValueIdentifiers.getRsaPublicKey()).setValue(new ByteArray(Security.getBulletinBoardRSAPublicKey().getEncoded()));
			
			publicKeys = keys;
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
	
	public static Date getElectionStartTime() {
		if(Model.election == null) {
			return new Date(0);
		}
		return Model.election.getStartTime();
	}

	public static Date getElectionEndTime() {
		if(Model.election == null) {
			return new Date(0);
		}
		return Model.election.getEndTime();
	}

	public static void createNewElection(Date startDate, Date endDate) {
		EntityManager entMgr = beginDatabaseSession();
		
		Election election = new Election(startDate, endDate);
		Model.election = election;
		
		entMgr.persist(election);
		
		endDatabaseSession(entMgr);
	}

	public static void setElectionOptions(ElectionOption[] electionOptions) {
		EntityManager entMgr = beginDatabaseSession();
		
		for(ElectionOption eo : electionOptions) {
			entMgr.persist(eo);
		}
		
		endDatabaseSession(entMgr);
	}
}