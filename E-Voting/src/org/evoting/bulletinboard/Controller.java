package org.evoting.bulletinboard;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import jolie.runtime.JavaService;
import jolie.runtime.Value;

import org.evoting.client.CandidateList;
import org.evoting.common.EncryptedCandidateList;
import org.evoting.database.EntityManagerUtil;
import org.evoting.database.entities.Candidate;
import org.evoting.database.entities.Timestamp;
import org.evoting.database.repositories.CandidateRepository;
import org.evoting.database.repositories.TimestampRepository;
import org.evoting.security.Security;
import org.hibernate.id.GUIDGenerator;

public class Controller extends JavaService {

	public Value processVote(Value encryptedBallot) {
		Value voteRegistered = Value.create(false);
		/*
		// Get the values from the ballot
		String userInfo = encryptedBallot.getChildren("userInfo").get(0).strValue();
		String votes = encryptedBallot.getChildren("vote").get(0).strValue();

		//TODO: Set the userId to the userId found in the userInfo
		int userId;
		//TODO: Set the passwordHash to the passwordHash found in the userInfo
		String passwordHash;
		
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
			//Update the ciphertext if the user has voted before
			vote.setCiphertext(passwordHash);
		}
		//Commit the transaction
		transaction.commit();
		*/
		return voteRegistered;
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
}