package org.evoting.bulletinboard;

import jolie.runtime.JavaService;
import jolie.runtime.Value;

public class Controller extends JavaService {
	
	public Value processVote() {
		Value voteRegistered = Value.create(false);
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