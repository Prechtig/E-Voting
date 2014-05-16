package org.evoting.common.jolie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.common.Converter;
import org.evoting.common.exceptions.BadValueException;
import org.evoting.database.entities.Vote;
import org.evoting.security.Security;


public class AnonymousVoteList {
	private List<AnonymousVote> listOfVotes;
	private byte[] signature;
	
	public AnonymousVoteList(List<Vote> voteList)
	{
		listOfVotes = new ArrayList<AnonymousVote>();
		int index = 0;
		for(Vote v : voteList) {
			listOfVotes.add(new AnonymousVote(v.getEncryptedVote(), index++));
		}
		signature = Security.sign(toByteArray(), Security.getBulletinBoardRSAPrivateKey());
	}
	
	public AnonymousVoteList(Value value) throws BadValueException
	{
		if(!value.hasChildren(ValueIdentifiers.getVotes()) ||
		   !value.hasChildren(ValueIdentifiers.getSignature())) {
					throw new BadValueException();
			}
		
		listOfVotes = new ArrayList<AnonymousVote>();
		ValueVector votesVector = value.getChildren(ValueIdentifiers.getVotes());
		for(Value v : votesVector) {
			if(!v.hasChildren(ValueIdentifiers.getVote()) ||
			   !v.hasChildren(ValueIdentifiers.getIndex())) {
								throw new BadValueException();
			}
			listOfVotes.add(constructVote(v.getChildren(ValueIdentifiers.getVote()), v.getFirstChild(ValueIdentifiers.getIndex())));
		}
		
		Collections.sort(listOfVotes, new Comparator<AnonymousVote>() {
			public int compare(AnonymousVote v1, AnonymousVote v2) {
				return Integer.compare(v1.getIndex(), v2.getIndex());
			}
		});

		signature = value.getFirstChild(ValueIdentifiers.getSignature()).byteArrayValue().getBytes();
	}
	
	public List<AnonymousVote> getListOfVotes() {
		return listOfVotes;
	}

	public byte[] getSignature() {
		return signature;
	}

	private AnonymousVote constructVote(ValueVector voteVector, Value index) 
	{
		byte[][] encryptedVote = new byte[voteVector.size()][];
		
		for(Value v : voteVector) {
			encryptedVote[v.getFirstChild(ValueIdentifiers.getElectionOptionId()).intValue()] = v.getFirstChild(ValueIdentifiers.getEncryptedVote()).byteArrayValue().getBytes();
		}
		return new AnonymousVote(encryptedVote, index.intValue());
	}
	
	/**
	 * Converts this object to a value defined in Types.iol (Jolie)
	 * @return The value representing the list of votes
	 */
	public Value toValue() {
		Value result = Value.create();
		
		int index = 0;
		for(AnonymousVote v : listOfVotes) {
			Value votes = result.getNewChild(ValueIdentifiers.getVotes());
			votes.getNewChild(ValueIdentifiers.getIndex()).setValue(index++);
			for(int i = 0; i < v.getEncryptedVote().length; i++) {
				Value vote = votes.getNewChild(ValueIdentifiers.getVote());
				vote.getNewChild(ValueIdentifiers.getElectionOptionId()).setValue(i);
				vote.getNewChild(ValueIdentifiers.getEncryptedVote()).setValue(new ByteArray(v.getEncryptedVote()[i]));
			}
		}
		result.getNewChild(ValueIdentifiers.getSignature()).setValue(new ByteArray(signature));
		return result;
	}
	
	public byte[] toByteArray()
	{
		ArrayList<byte[]> byteList = new ArrayList<byte[]>();
		for(AnonymousVote v : listOfVotes) {
			for(int i = 0; i < v.getEncryptedVote().length; i++) {
				byteList.add(v.getEncryptedVote()[i]);
				byteList.add(Converter.toByteArray(i));
			}
		}
		byte[][] bytes = new byte[byteList.size()][];
		return Security.concatenateByteArrays(byteList.toArray(bytes));
	}
	
}
