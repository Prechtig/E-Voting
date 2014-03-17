package org.evoting.bulletinboard;

import jolie.runtime.JavaService;
import jolie.runtime.Value;

public class Controller extends JavaService {
	
	public Value processVote(Value encryptedBallot) {
		Value voteRegistered = Value.create(false);
		
		//Get the values from the ballot
		String userInfo = encryptedBallot.getChildren("userInfo").get(0).strValue();
		String votes = encryptedBallot.getChildren("vote").get(0).strValue();
		
		
		return voteRegistered;
	}
	
	public Value getCandidates() {
		Value candidates = Value.create();
		for(int i = 0; i < 10; i++) {
			candidates.getNewChild("candidates").setValue("candidate_" + i);
		}
		return candidates;
	}
}