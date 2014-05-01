package org.evoting.authority.commands;

import java.io.IOException;
import java.util.List;

import jolie.net.CommMessage;
import jolie.runtime.JavaService;
import jolie.runtime.Value;

import org.evoting.authority.Model;
import org.evoting.common.AnonymousVote;
import org.evoting.common.AnonymousVoteList;
import org.evoting.common.Converter;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.database.entities.ElectionOption;
import org.evoting.security.Security;


public class CountVotes extends Command
{
	public static final String KEYWORD = "count";
	
	public CountVotes(String[] args) {
		super(args);
	}
	
	public String execute(JavaService js)
	{
		long[] votes = countVotes(js);
		votes = addCandidateVotesToParties(votes, Model.geteOptions());
		return electionResultToString(votes, Model.geteOptions());
	}
	
	/**
	 * Retrieves all votes from bulletinboard and calculates the result.
	 * @return The result of the election as long array with index representing election option id.
	 */
	private long[] countVotes(JavaService js) {
		CommMessage request = CommMessage.createRequest("", Model.getaCommunicationPath(), null);
		try {
			CommMessage response = js.sendMessage(request).recvResponseFor(request);
			Value listOfVotesValue = response.value();
			AnonymousVoteList allVotes = new AnonymousVoteList(listOfVotesValue);
			
			boolean isCorruptEdited = Security.authenticate(Converter.convert(allVotes), allVotes.getSignature(), Security.getBulletinBoardRSAPublicKey());
			if(!isCorruptEdited) {
				throw new CorruptDataException();
			}
			
			byte[][] voteProducts = new byte[Model.geteOptions().size()][];
			boolean firstIteration = true;
			for(AnonymousVote v : allVotes.getListOfVotes()) {
				if(firstIteration) {
					for(int i = 0; i < voteProducts.length; i++) {
						voteProducts[i] = v.getEncryptedVote()[i];
					}
				} else {
					for (int i = 0; i < voteProducts.length; i++) {
						voteProducts[i] = Security.multiplyElGamalCiphers(voteProducts[i], v.getEncryptedVote()[i]);
					}
				}
			}
			
			long[] result = new long[Model.geteOptions().size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = Security.decryptExponentialElgamal(voteProducts[i], Security.getElgamalPrivateKey());
			}
			
			return result;
		} catch (IOException e) {
			//TODO something
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String electionResultToString(long[] result, List<ElectionOption> electionOptions) {
		String[] electionOptionsOrdered = new String[electionOptions.size()];
		for(ElectionOption e : electionOptions) {
			if(e.getPartyId() != -1) {
				electionOptionsOrdered[e.getId()] = e.getName() + " from " + getPartyName(e.getPartyId(), electionOptions);
			} else {
				electionOptionsOrdered[e.getId()] = e.getName();
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < electionOptionsOrdered.length; i++) {
			sb.append(electionOptionsOrdered[i]);
			sb.append(" with ");
			sb.append(result[i]);
			sb.append(" votes.\n");
		}
		
		return sb.toString();
	}
	
	private long[] addCandidateVotesToParties(long[] electionResult, List<ElectionOption> electionOptions) {
		long[] result = new long[electionOptions.size()];
		
		for(ElectionOption e : electionOptions) {
			if(e.getId() != e.getPartyId() && e.getPartyId() != -1) {
				result[e.getPartyId()] = result[e.getPartyId()] + electionResult[e.getId()];
			}
		}
		
		return result;
	}
	
	private String getPartyName(int partyId, List<ElectionOption> electionOptions) {
		for(ElectionOption e : electionOptions) {
			if(e.getId() == partyId) {
				return e.getName();
			}
		}
		
		return "No party";
	}
}
