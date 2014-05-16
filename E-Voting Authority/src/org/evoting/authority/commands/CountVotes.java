package org.evoting.authority.commands;

import java.io.IOException;
import java.util.List;

import jolie.net.CommMessage;
import jolie.runtime.JavaService;
import jolie.runtime.Value;

import org.evoting.authority.Model;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.common.jolie.AnonymousVote;
import org.evoting.common.jolie.AnonymousVoteList;
import org.evoting.database.entities.ElectionOption;
import org.evoting.security.Security;


public class CountVotes extends Command
{
	public static final String KEYWORD = "count";
	private static String invalidArguments = "Invalid arguments.";
	
	public CountVotes(String[] args) {
		super(args);
	}
	
	public String execute(JavaService js)
	{
		if(Model.getElectionOptions() == null) {
			return "Cannot count before options has been loaded.";
		}
		
		if (args.length < 2) {
			return invalidArguments;
		}

		long[] votes;
		switch (args[1]) {
		// adds the personal votes to the party votes
		case "sum":
			votes = countVotes(js);
			votes = addCandidateVotesToParties(votes, Model.getElectionOptions());
			return electionResultToString(votes, Model.getElectionOptions());
			// count the votes only
		case "clean":
			votes = countVotes(js);
			return electionResultToString(votes, Model.getElectionOptions());
			// Generate both RSA and ElGamal key pair
		case "both":
			votes = countVotes(js);
			StringBuilder sb = new StringBuilder();
			sb.append("Clean: \n");
			sb.append(electionResultToString(votes, Model.getElectionOptions()));
			sb.append("\n");
			sb.append("Sum: \n");
			votes = addCandidateVotesToParties(votes, Model.getElectionOptions());
			sb.append(electionResultToString(votes, Model.getElectionOptions()));
			return sb.toString();
		default:
			return invalidArguments;
		}
	}
	
	/**
	 * Retrieves all votes from bulletinboard and calculates the result.
	 * @return The result of the election as long array with index representing election option id.
	 */
	private long[] countVotes(JavaService js) {
		CommMessage request = CommMessage.createRequest("getAllVotesAuthority", Model.getaCommunicationPath(), Model.getNewValidator());
		try {
			CommMessage response = js.sendMessage(request).recvResponseFor(request);
			Value listOfVotesValue = response.value();
			AnonymousVoteList allVotes = new AnonymousVoteList(listOfVotesValue);
			
			boolean notCorrupted = Security.authenticate(allVotes.toByteArray(), allVotes.getSignature(), Security.getBulletinBoardRSAPublicKey());
			if(!notCorrupted) {
				throw new CorruptDataException();
			}
			
			byte[][] voteProducts = new byte[Model.getElectionOptions().size()][];
			boolean firstIteration = true;
			for(AnonymousVote v : allVotes.getListOfVotes()) {
				if(firstIteration) {
					for(int i = 0; i < voteProducts.length; i++) {
						voteProducts[i] = v.getEncryptedVote()[i];
						firstIteration = false;
					}
				} else {
					for (int i = 0; i < voteProducts.length; i++) {
						voteProducts[i] = Security.multiplyElGamalCiphers(voteProducts[i], v.getEncryptedVote()[i]);
					}
				}
			}
			
			long[] result = new long[Model.getElectionOptions().size()];
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
	
	private String electionResultToString(long[] electionResult, List<ElectionOption> electionOptions) {
		String[] electionOptionsOrdered = new String[electionOptions.size()];
		for(ElectionOption e : electionOptions) {
			if(e.getElectionOptionId() == e.getPartyId()) {
				electionOptionsOrdered[e.getElectionOptionId()] = "Party " + e.getName();
			} else if(e.getPartyId() != -1) {
				electionOptionsOrdered[e.getElectionOptionId()] = e.getName() + " from " + getPartyName(e.getPartyId(), electionOptions);
			} else {
				electionOptionsOrdered[e.getElectionOptionId()] = e.getName();
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < electionOptionsOrdered.length; i++) {
			sb.append(electionOptionsOrdered[i]);
			sb.append(" with ");
			sb.append(electionResult[i]);
			sb.append(" votes.\n");
		}
		
		return sb.toString();
	}
	
	private long[] addCandidateVotesToParties(long[] electionResult, List<ElectionOption> electionOptions) {
		long[] result = new long[electionOptions.size()];
		
		for(ElectionOption e : electionOptions) {
			if(e.getElectionOptionId() != e.getPartyId() && e.getPartyId() != -1) {
				result[e.getPartyId()] += electionResult[e.getElectionOptionId()];
			}
		}
		
		for (int i = 0; i < result.length; i++) {
			result[i] += electionResult[i];
		}
		
		return result;
	}
	
	private String getPartyName(int partyId, List<ElectionOption> electionOptions) {
		for(ElectionOption e : electionOptions) {
			if(e.getElectionOptionId() == partyId) {
				return e.getName();
			}
		}
		
		return "No party";
	}
}
