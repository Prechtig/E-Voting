package org.evoting.authority;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jolie.net.CommMessage;
import jolie.runtime.ByteArray;
import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.AllVotesAuthority;
import org.evoting.common.Converter;
import org.evoting.common.ElectionOptions;
import org.evoting.common.Exporter;
import org.evoting.common.Importer;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.security.Security;

public class ConsoleIO extends JavaService {
	private String ElGamalPublicKeyFile = "ElGamalPublicKey";
	private String ElGamalPrivateKeyFile = "ElGamalPrivateKey";
	private String authRsaPrivKeyFilepath = "AuthRsaPriv";
	private String authRsaPubKeyFilepath = "AuthRsaPub";
	private String bbRsaPrivKeyFilepath = "BbRsaPriv";
	private String bbRsaPubKeyFilepath = "BbRsaPub";

	private boolean electionRunning;
	private Date endTime; // TODO: is this used?

	private String aCommunicationPath = "/";
	private String electionOptionsFile = "ElectionOptions.txt";

	private static ElectionOptions eOptions;

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
			case "start":
				userStartElection();
				break;
			// Stop election
			case "stop":
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
		//Send election options
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
		System.out.println("Beginning of updateElectionStatus()");
		CommMessage request = CommMessage.createRequest("getElectionStatus", aCommunicationPath, Value.create());
		System.out.println("After CommMessage.createRequest");
		try {
			System.out.println("Before sendMessage");
			CommMessage response = sendMessage(request).recvResponseFor(request);
			System.out.println("After sendMessage");

			electionRunning = response.value().getFirstChild("running").boolValue();
			long lTime = response.value().getFirstChild("endTime").longValue();
			endTime = new Date(lTime);

			// endTime is -1 if some error happened in bullitinboard
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
	 * Retrieves all votes from bulletinboard and calculates the result
	 */
	private void countVotes() {
		CommMessage request = CommMessage.createRequest("", aCommunicationPath, null);
		try {
			CommMessage response = sendMessage(request).recvResponseFor(request);
			Value listOfVotesValue = response.value();
			AllVotesAuthority allVotes = new AllVotesAuthority(listOfVotesValue);
			
			boolean isNotEdited = Security.authenticate(Converter.convert(allVotes), allVotes.getSignature(), Security.getBulletinBoardRSAPublicKey());
			if(!isNotEdited) {
				throw new CorruptDataException();
			}
			
			

		} catch (IOException e) {
			//TODO something
			e.printStackTrace();
		}
	}

	private void userStartElection() {
		// System.out.println("What time should the election stop? (yyyy-MM-dd HH:mm)");

		// String date = System.console().readLine().toLowerCase();

		try {
			// Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date); //TODO:uncommented because of testing
			Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2014-04-30 20:00");
			startElection(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
					endTime = d;
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
	 * Generates and exports a new set of ElGamal keys and saves them to files, only if election is not running
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

	private void generateRsaKeys() {
		if (!electionRunning) {
			Security.generateRSAKeys();
			try {
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

	private void loadElGamalKeys() {
		if (!electionRunning) {
			ElGamalPublicKeyParameters elGamalPublicKey = Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
			ElGamalPrivateKeyParameters elGamalPrivateKey = Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile);

			Security.setElGamalPrivateKey(elGamalPrivateKey);
			Security.setElGamalPublicKey(elGamalPublicKey);

			System.out.println("Loaded ElGamal keys");
		} else {
			System.out.println("Cannot load new ElGamal keys while election is running");
		}
	}

	private void loadRSAKeys() {
		if (!electionRunning) {
			try {
				PublicKey bbPubKey = Importer.importRsaPublicKey(bbRsaPubKeyFilepath);
				PublicKey authPubKey = Importer.importRsaPublicKey(authRsaPubKeyFilepath);
				PrivateKey authPrivKey = Importer.importRsaPrivateKey(authRsaPrivKeyFilepath);
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

	private void loadElectionOptions() {
		if (!electionRunning) {
			eOptions = Importer.importElectionOptions(electionOptionsFile);
			System.out.println("Imported list of election options");
		} else {
			System.out.println("Cannot load new electionoptions while election is running");
		}
	}

	private void stopElection() {
		if (electionRunning) {
			CommMessage request = CommMessage.createRequest("stopElection", aCommunicationPath, null);
			try {
				CommMessage response = sendMessage(request).recvResponseFor(request);

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

	private Value getNewValidator() {
		String message = nextRandomString();
		byte[] signature = Security.sign(message, Security.getAuthorityRSAPrivateKey()); // TODO: not RSApblickey
		Value val = Value.create();
		val.getNewChild("message").setValue(message);
		val.getNewChild("signature").setValue(new ByteArray(signature));
		return val;
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