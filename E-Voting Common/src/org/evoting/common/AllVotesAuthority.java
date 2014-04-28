package org.evoting.common;

import java.util.ArrayList;
import java.util.List;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.common.exceptions.BadValueException;


public class AllVotesAuthority {
	private List<AnonymousVote> listOfVotes;
	private byte[] signature;
	
	public AllVotesAuthority(List<Vote> voteList)
	{
		listOfVotes = new ArrayList<AnonymousVote>();
		for(Vote v : voteList) {
			listOfVotes.add(new AnonymousVote(v.getEncryptedVote()));
		}
	}
	
	public AllVotesAuthority(Value value) throws BadValueException
	{
		if(!value.hasChildren(ValueIdentifiers.getVotes()) ||
		   !value.hasChildren(ValueIdentifiers.getSignature())) {
					throw new BadValueException();
			}
		
		listOfVotes = new ArrayList<AnonymousVote>();
		ValueVector votesVector = value.getChildren(ValueIdentifiers.getVotes());
		for(Value v : votesVector) {
			if(!value.hasChildren(ValueIdentifiers.getVote())) {
								throw new BadValueException();
			}
			listOfVotes.add(constructVote(v.getChildren(ValueIdentifiers.getVote())));
		}
		
		signature = value.getFirstChild(ValueIdentifiers.getSignature()).byteArrayValue().getBytes();
	}
	
	public List<AnonymousVote> getListOfVotes() {
		return listOfVotes;
	}

	public byte[] getSignature() {
		return signature;
	}

	private AnonymousVote constructVote(ValueVector voteVector) 
	{
		byte[][] encryptedVote = new byte[voteVector.size()][];
		
		//TODO: What is this "i" for?
		@SuppressWarnings("unused")
		int i = 0;
		for(Value v : voteVector) {
			encryptedVote[v.getFirstChild(ValueIdentifiers.getElectionOptionId()).intValue()] = v.byteArrayValue().getBytes();
		}
		return new AnonymousVote(encryptedVote);
	}
	
	/**
	 * Converts this object to a value defined in Types.iol (Jolie)
	 * @return The value representing the list of votes
	 */
	public Value toValue() {
		Value result = Value.create();
		
		for(AnonymousVote v : listOfVotes) {
			Value votes = result.getNewChild("votes");
			for(int i = 0; i < v.getEncryptedVote().length; i++) {
				Value vote = votes.getNewChild("vote");
				vote.getNewChild("electionOptionId").setValue(i);
				vote.getNewChild("encryptedVote").setValue(new ByteArray(v.getEncryptedVote()[i]));
			}
		}
		return result;
	}
}
