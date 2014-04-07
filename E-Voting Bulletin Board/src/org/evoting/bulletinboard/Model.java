package org.evoting.bulletinboard;

import java.util.List;

import javax.persistence.EntityManager;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.EncryptedCandidateList;
import org.evoting.database.EntityManagerUtil;
import org.evoting.database.entities.Candidate;
import org.evoting.database.entities.Timestamp;
import org.evoting.database.entities.Vote;
import org.evoting.database.repositories.CandidateRepository;
import org.evoting.database.repositories.TimestampRepository;
import org.evoting.database.repositories.VoteRepository;
import org.evoting.security.Security;

public class Model {
	
	public static void processVote(int userId, byte[][] encryptedVote) {
		EntityManager entMgr = EntityManagerUtil.getEntityManager();
		VoteRepository vr = new VoteRepository(entMgr);
		
		//Begin a transaction
		entMgr.getTransaction().begin();
		
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
		//Close the connection to the persistent storage
		entMgr.getTransaction().commit();
		entMgr.close();
	}
	
	public static EncryptedCandidateList getEncryptedCandidateList() {
		EntityManager entMgr = EntityManagerUtil.getEntityManager();
		entMgr.getTransaction().begin();
		
		CandidateRepository cRepo = new CandidateRepository(entMgr);
		List<Candidate> candidates = cRepo.findAll();
		
		TimestampRepository tRepo = new TimestampRepository(entMgr);
		Timestamp timestamp = tRepo.findTime();
		
		EncryptedCandidateList candidateList = new EncryptedCandidateList(candidates, timestamp.getTime());
		
		//Close the connection to the persistent storage
		entMgr.getTransaction().commit();
		entMgr.close();
		return candidateList;
	}
	
	public static List<Vote> getAllVotes() {
		EntityManager entMgr = EntityManagerUtil.getEntityManager();
		entMgr.getTransaction().begin();
		
		VoteRepository vRepo = new VoteRepository(entMgr);
		List<Vote> allVotes = vRepo.findAll();
		
		entMgr.getTransaction().commit();
		entMgr.close();
		
		return allVotes;
	}
	
	public static Value toValue(List<Vote> allVotes) {
		Value result = Value.create();
		
		for(Vote v : allVotes) {
			Value votes = result.getNewChild("votes");
			for(int i = 0; i < v.getEncryptedVote().length; i++) {
				Value vote = votes.getNewChild("vote");
				vote.getNewChild("candidateId").setValue(i);
				vote.getNewChild("encryptedVote").setValue(new ByteArray(v.getEncryptedVote()[i]));
			}
		}
		return result;
	}
	
	public static void setElGamalPublicKey(Value root) {
		ElGamalPublicKeyParameters elgamalPublicKey = Security.getElgamalPublicKey();
		ElGamalParameters elgamalParameters = elgamalPublicKey.getParameters();
		
		Value elgamalPublicKeyValue = root.getNewChild("elgamalPublicKey");
		elgamalPublicKeyValue.getNewChild("y").setValue(elgamalPublicKey.getY().toString());
		Value elgamalParametersValue = elgamalPublicKeyValue.getNewChild("parameters");
		elgamalParametersValue.getNewChild("p").setValue(elgamalParameters.getP().toString());
		elgamalParametersValue.getNewChild("g").setValue(elgamalParameters.getG().toString());
		elgamalParametersValue.getNewChild("l").setValue(elgamalParameters.getL());
	}
	
	public static void setRSAPublicKey(Value root) {
		byte[] rsaPublicKey = Security.getRSAPublicKeyBytes();
		root.getNewChild("rsaPublicKey").setValue(new ByteArray(rsaPublicKey));
	}
}
