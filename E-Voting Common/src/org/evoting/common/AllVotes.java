package org.evoting.common;

import java.util.ArrayList;

import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.common.exceptions.BadValueException;


public class AllVotes {
	private ArrayList<AnonymousVote> listOfVotes;
	
	public AllVotes(Value value) throws BadValueException
	{
		if(!value.hasChildren(ValueIdentifiers.getVotes()) ||
		   !value.hasChildren(ValueIdentifiers.getSignature())) {
					throw new BadValueException();
			}
		
		listOfVotes = new ArrayList<AnonymousVote>();
		ValueVector votesVector = value.getChildren(ValueIdentifiers.getVotes());
		for(Value v : votesVector) {
			listOfVotes.add(constructVote(v.getChildren(ValueIdentifiers.getVote())));
		}
		
		
	}
	
	private AnonymousVote constructVote(ValueVector voteVector) 
	{
		byte[][] encryptedVote = new byte[voteVector.size()][];
		int i = 0;
		for(Value v : voteVector) {
			encryptedVote[i] = v.byteArrayValue().getBytes();
		}
		
		return new AnonymousVote(encryptedVote);
	}
}
