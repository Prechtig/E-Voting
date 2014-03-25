package org.evoting.bulletinboard;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import jolie.runtime.JavaService;
import jolie.runtime.Value;

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

public class Controller extends JavaService {

	public Value processVote(Value valueEncryptedBallot) {
		EncryptedBallot encryptedBallot = new EncryptedBallot(valueEncryptedBallot);
		Ballot ballot = encryptedBallot.getBallot();
		
		int userId = ballot.getUserId();
		String passwordHash = ballot.getPasswordHash();
		byte[] encryptedVote = ballot.getVote();
		
		//TODO: Check the user+passwordHash is a legal combination
		
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
		
		return Value.create(true);
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