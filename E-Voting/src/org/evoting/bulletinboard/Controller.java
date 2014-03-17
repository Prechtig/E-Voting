package org.evoting.bulletinboard;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import jolie.runtime.JavaService;
import jolie.runtime.Value;

import org.evoting.database.EntityManagerUtil;
import org.evoting.database.Vote;
import org.evoting.database.VoteRepository;

public class Controller extends JavaService {

	public Value processVote(Value encryptedBallot) {
		Value voteRegistered = Value.create(false);

		// Get the values from the ballot
		String userInfo = encryptedBallot.getChildren("userInfo").get(0).strValue();
		String votes = encryptedBallot.getChildren("vote").get(0).strValue();

		//TODO: Set the userId to the userId found in the userInfo
		int userId;
		//TODO: Set the ciphertext to the ciphertext found in the userInfo
		String ciphertext;
		
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
			vote.setCiphertext(ciphertext);
		}
		//Commit the transaction
		transaction.commit();
		return voteRegistered;
	}

	public Value getCandidates() {
		Value candidates = Value.create();
		for (int i = 0; i < 10; i++) {
			candidates.getNewChild("candidates").setValue("candidate_" + i);
		}
		return candidates;
	}
}