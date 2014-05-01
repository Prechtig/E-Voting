package org.evoting.authority;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jolie.net.CommMessage;
import jolie.runtime.ByteArray;
import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
//import org.evoting.authority.commands.Command; //TODO: Does not exist
import org.evoting.common.AnonymousVoteList;
import org.evoting.common.AnonymousVote;
import org.evoting.common.Converter;
import org.evoting.common.Exporter;
import org.evoting.common.Importer;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.database.entities.ElectionOption;
import org.evoting.security.Security;

public class ConsoleIO extends JavaService {
	private String ElGamalPublicKeyFile = "ElGamalPublicKey";
	private String ElGamalPrivateKeyFile = "ElGamalPrivateKey";
	private String authRsaPrivKeyFilepath = "AuthRsaPriv";
	private String authRsaPubKeyFilepath = "AuthRsaPub";
	private String bbRsaPrivKeyFilepath = "BbRsaPriv";
	private String bbRsaPubKeyFilepath = "BbRsaPub";

	private String aCommunicationPath = "/";
	private String electionOptionsFile = "ElectionOptions.txt";

	private boolean electionRunning;
	
	private static List<ElectionOption> eOptions;

	private SecureRandom random = new SecureRandom();

	/**
	 * Main method. Used to get users input
	 */
	public void getUserInput() {
		System.out.println("Initializing election details");
		// Initialize the current election status
		updateElectionStatus();

		// Program loop
		while (true) {
			System.out.println("Enter commmand: ");
			String input = System.console().readLine().toLowerCase();

			switch (input) {
			// Sart election
			case "start"://TODO: Jolie needs
				userStartElection();
				break;
			// Stop election
			case "stop"://TODO: 
				stopElection();
				break;
			// Load electionOptions or keys
			case "load":
				userCommandLoad();
				break;
			// Generate keys
			case "generate":
				userCommandGenerate();
				break;
			// Send electionOptions
			case "send":
				userCommandSend();
				break;
			// Count votes
			case "count":
				countVotes();
				break;
			// Update the election status
			case "update":
				updateElectionStatus();
				break;
			// Terminate program
			case "exit":
				return;
			default:
				System.out.println("Command not found");
				break;
			}
		}
	}

	/**
	 * Handles when user has entered the "generate" command. Allows to generate and export new ElGamal, RSA or both key pairs
	 */
	private void userCommandGenerate() {
		System.out.println("Generate ElGamal, RSA or both?");
		String input = System.console().readLine().toLowerCase();

		switch (input) {
		// Generate RSA keypair
		case "rsa":
			generateRsaKeys();
			break;
		// Generate ElGamal keypair
		case "elgamal":
			generateElGamalKeys();
			break;
		// Generate both RSA and ElGamal key pair
		case "both":
			generateRsaKeys();
			generateElGamalKeys();
			break;
		default:
			System.out.println("Command not found");
			break;
		}
	}

	/**
	 * Handles when the user has entered the "load" command. Allows the user to load ElGamal or RSA key pairs or electionOptions
	 */
	private void userCommandLoad() {
		System.out.println("Load keys or electionOption list?");
		String input = System.console().readLine().toLowerCase();

		switch (input) {
		// Load keys
		case "keys":
		case "key":
			userCommandLoadKeys();
			break;
		// Load election options
		case "electionOptions":
		case "electionOption":
		case "electionOption list":
		case "electionOptionlist":
			loadElectionOptions();
			break;
		default:
			System.out.println("Command not found");
			break;
		}
	}

	/**
	 * Handles when the user has chosen to load keys. Allows the user to load ElGamal or RSA key pairs
	 */
	private void userCommandLoadKeys() {
		System.out.println("Load ElGamal or RSA?");
		String input = System.console().readLine().toLowerCase();

		switch (input) {
		// Load RSA key pairs
		case "rsa":
			loadRSAKeys();
			break;
		// Load ElGamal key pair
		case "elgamal":
			loadElGamalKeys();
			break;
		}
	}

	/**
	 * Handles when the user want to send the electionOptions list to the bulletin board
	 */
	private void userCommandSend() {
		System.out.println("Send electionOption list?");
		String input = System.console().readLine().toLowerCase();

		switch (input) {
		// Send election options
		case "electionoptions":
		case "electionoption":
		case "electionoption list":
		case "electionoptionlist":
			sendElectionoptions();
			break;
		default:
			System.out.println("Command not found");
			break;
		}
	}

	/**
	 * Used to get the initial information about the election. Sets if the election is running and, if it is, then what time it will end
	 */
	public void updateElectionStatus() {
		// Create request to jolie
		CommMessage request = CommMessage.createRequest("getElectionStatus", aCommunicationPath, Value.create());
		try {
			// Send jolie request
			CommMessage response = sendMessage(request).recvResponseFor(request);

			// Set information
			electionRunning = response.value().getFirstChild("running").boolValue();
			long lTime = response.value().getFirstChild("endTime").longValue();

			// if endTime is -1 if some error happened in bullitinboard
			if (lTime > -1) {
				System.out.println("Election running: " + electionRunning);
			} else {
				System.out.println("Error in bullitinboard when trying to update election details");
			}
		} catch (IOException e) {
			System.out.println("Error communicating with bullitinboard");
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves all votes from bulletinboard and calculates the result.
	 * @return The result of the election.
	 */
	 private long[] countVotes() {
	 
		CommMessage request = CommMessage.createRequest("", aCommunicationPath, null);
		try {
			CommMessage response = sendMessage(request).recvResponseFor(request);
			Value listOfVotesValue = response.value();
			AnonymousVoteList allVotes = new AnonymousVoteList(listOfVotesValue);
			
			boolean isCorruptEdited = Security.authenticate(Converter.convert(allVotes), allVotes.getSignature(), Security.getBulletinBoardRSAPublicKey());
			if(!isCorruptEdited) {
				throw new CorruptDataException();
			}
			
			byte[][] voteProducts = new byte[eOptions.size()][];
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
			
			long[] result = new long[eOptions.size()];
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

	/**
	 * Handles when user has entered the "start" command. It start the election and asks the user to enter an end time and date for it
	 */
	private void userStartElection() {
		// System.out.println("What time should the election stop? (yyyy-MM-dd HH:mm)");

		// String date = System.console().readLine().toLowerCase();

		try {
			// Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date); //TODO:uncommented because of testing
			Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2014-04-30 20:00");
			startElection(d);
		} catch (ParseException e) {
			System.out.println("Could not parse the entered date and time");
			e.printStackTrace();
		}
	}

	/**
	 * Used to start the election.
	 * 
	 * @param d
	 *            The time where the election should end
	 */
	private void startElection(Date d) {
		if (!electionRunning) {
			// Create value with endtime and signed endtime
			Value result = Value.create();
			result.getNewChild("endTime").setValue(d.getTime());
			Value validator = getNewValidator();
			ValueVector children = result.getChildren("validator");
			children.set(0, validator);

			CommMessage request = CommMessage.createRequest("startElection", aCommunicationPath, result);
			try {
				CommMessage response = sendMessage(request).recvResponseFor(request);

				if (response.value().boolValue()) {
					System.out.println("Election has started");
					electionRunning = true;
				} else {
					System.out.println("Error in bullitinboard when trying to start election");
				}
			} catch (IOException e) {
				System.out.println("Error communicating with bullitinboard");
				e.printStackTrace();
			}
		} else {
			System.out.println("Cannot start election while it is running");
		}
	}

	/**
	 * Generates and exports a new set of ElGamal keys and exports them, only if election is not running
	 */
	private void generateElGamalKeys() {
		if (!electionRunning) {
			// Export keys
			Exporter.exportElGamalPrivateKeyParameters(Security.getElgamalPrivateKey(), ElGamalPrivateKeyFile);
			Exporter.exportElGamalPublicKeyParameters(Security.getElgamalPublicKey(), ElGamalPublicKeyFile);

			System.out.println("Generated and exported new ElGamal keys");
		} else {
			System.out.println("Cannot generate new ElGamal keys while election is running");
		}
	}

	/**
	 * Generates and exports two new set of RAS keys. Only if the election is not running
	 */
	private void generateRsaKeys() {
		if (!electionRunning) {
			// Generate RSA keys
			Security.generateRSAKeys();
			try {
				// Export the keys
				Exporter.exportRsaKey(authRsaPrivKeyFilepath, Security.getAuthorityRSAPrivateKey());
				Exporter.exportRsaKey(authRsaPubKeyFilepath, Security.getAuthorityRSAPublicKey());
				Exporter.exportRsaKey(bbRsaPrivKeyFilepath, Security.getBulletinBoardRSAPrivateKey());
				Exporter.exportRsaKey(bbRsaPubKeyFilepath, Security.getBulletinBoardRSAPublicKey());
			} catch (IOException e) {
				System.out.println("Something went wrong. Try again");
				e.printStackTrace();
			}
		} else {
			System.out.println("Cannot generate new RSA keys while election is running");
		}
	}

	/**
	 * Load ElGamal key pair into the system. Only if election is not running
	 */
	private void loadElGamalKeys() {
		if (!electionRunning) {
			// Import the keys
			ElGamalPublicKeyParameters elGamalPublicKey = Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
			ElGamalPrivateKeyParameters elGamalPrivateKey = Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile);
			// Set the keys
			Security.setElGamalPrivateKey(elGamalPrivateKey);
			Security.setElGamalPublicKey(elGamalPublicKey);

			System.out.println("Loaded ElGamal keys");
		} else {
			System.out.println("Cannot load new ElGamal keys while election is running");
		}
	}

	/**
	 * Load RSA key pairs into the system. Only if election is not running
	 */
	private void loadRSAKeys() {
		if (!electionRunning) {
			try {
				// Import the keys
				PublicKey bbPubKey = Importer.importRsaPublicKey(bbRsaPubKeyFilepath);
				PublicKey authPubKey = Importer.importRsaPublicKey(authRsaPubKeyFilepath);
				PrivateKey authPrivKey = Importer.importRsaPrivateKey(authRsaPrivKeyFilepath);
				// Set the keys
				Security.setAuthorityRSAPrivateKey(authPrivKey);
				Security.setAuthorityRSAPublicKey(authPubKey);
				Security.setBulletinBoardRSAPublicKey(bbPubKey);
				System.out.println("Loaded RSA keys");
			} catch (IOException e) {
				System.out.println("Something went wrong");
				e.printStackTrace();
			}
		} else {
			System.out.println("Cannot load new ElGamal keys while election is running");
		}
	}

	/**
	 * Loads the election options into the system. Only when election is not running
	 */
	private void loadElectionOptions() {
		if (!electionRunning) {
			// Load the election options
			eOptions = Importer.importElectionOptions(electionOptionsFile);
			System.out.println("Imported list of election options");
		} else {
			System.out.println("Cannot load new electionoptions while election is running");
		}
	}

	/**
	 * Stops the elcetion. Only when election is running
	 */
	private void stopElection() {
		if (electionRunning) {
			// Create request to stop election
			CommMessage request = CommMessage.createRequest("stopElection", aCommunicationPath, null);
			try {
				// Send request
				CommMessage response = sendMessage(request).recvResponseFor(request);

				// Check response to see if it was successful
				if (response.value().boolValue()) {
					electionRunning = false;
					System.out.println("Election has stopped");
				} else {
					System.out.println("Error in bullitinboard when trying to stop election");
				}
			} catch (IOException e) {
				System.out.println("Error communicating with bullitinboard");
				e.printStackTrace();
			}
		} else {
			System.out.println("Cannot stop election while it is not running");
		}
	}

	/**
	 * Sends the electionOptions list to the bulletinboard. Only when election is not running
	 */
	private void sendElectionoptions() {
		if (!electionRunning) {
			if (eOptions != null) {
				// TODO:Create value containing all the electionoptions
				CommMessage request = CommMessage.createRequest("sendElectionOptionList", aCommunicationPath, null); // TODO:
																														// null
																														// skal
																														// være
																														// election
																														// options
				try {
					CommMessage response = sendMessage(request).recvResponseFor(request);// Den skal tage imod en
																							// value? som indeholder
																							// confirmation
					if (response.value().boolValue()) {
						System.out.println("Successfully sent list of election options");
					} else {
						System.out.println("Error in bullitinboard when trying to send list of election options");
					}
				} catch (IOException e) {
					System.out.println("Error communicating with bullitinboard");
					e.printStackTrace();
				}

				// TODO: Handle response
				// show error if not confirmed
			} else {
				System.out.println("Load election options");
			}
		} else {
			System.out.println("Cannot send list of election options while election is running");
		}
	}

	/**
	 * Creates a value containing a string and the string signed using the private key of the authority
	 * 
	 * @return A Value containing the informaiton for a validator as defined in Types.ol
	 */
	private Value getNewValidator() {
		// Get random string
		String message = nextRandomString();
		if (Security.RSAKeysSat()) {
			// Sign the random message
			byte[] signature = Security.sign(message, Security.getAuthorityRSAPrivateKey());
			// Create new value and set children
			Value val = Value.create();
			val.getNewChild("message").setValue(message);
			val.getNewChild("signature").setValue(new ByteArray(signature));
			return val;
		} else {
			System.out.println("Cannot create validator without RSA keys");
		}
		return null;
	}
	
	private String nextRandomString() {
		return new BigInteger(130, random).toString(32);
	}
	

	public static void main(String[] args) throws ParseException {
		ConsoleIO io = new ConsoleIO();
		io.loadRSAKeys();
		io.startElection(new SimpleDateFormat("HH").parse("20"));
	}
}