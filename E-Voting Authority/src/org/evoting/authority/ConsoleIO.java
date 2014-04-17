package org.evoting.authority;

import java.io.IOException;
import java.sql.Timestamp;

import jolie.net.CommMessage;
import jolie.runtime.JavaService;
import jolie.runtime.Value;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.ElectionOptions;
import org.evoting.common.Exporter;
import org.evoting.common.Importer;
import org.evoting.security.Security;

public class ConsoleIO extends JavaService {
	private static String ElGamalPublicKeyFile = "ElGamalPublicKey";
	private static String ElGamalPrivateKeyFile = "ElGamalPrivateKey";

	private static ElGamalPublicKeyParameters elGamalPublicKey;
	private static ElGamalPrivateKeyParameters elGamalPrivateKey;

	private static boolean electionRunning;
	private static Timestamp endTime;

	private String aCommunicationPath = "IAuthorityCommunication";
	private String electionOptionsFile = "ElectionOptions.txt";

	private static ElectionOptions eOptions;

	/**
	 * Used to get the initial information about the election Sets if the election is running// ?and if it is, then what time it will end?
	 */
	public void initialize() {
		CommMessage request = CommMessage.createRequest("getElectionStatus", aCommunicationPath, null); // TODO: null skal være void
		try {
			CommMessage response = sendMessage(request).recvResponseFor(request); // Den skal tage imod en value? som indeholder om election er running og hvad endtime på den er
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO: Handle response
		// set electionRunning and endTime
	}

	public void getUserInput() {
		initialize();

		while (true) {
			System.out.println("Enter commmand: ");
			String input = System.console().readLine().toLowerCase();

			switch (input) {
			case "start": // Start election
				startElection();
				break;
			case "stop": // Stop election
				stopElection();
				break;
			case "load": // Load electionOptions or keys
				userCommandLoad();
				break;
			case "generate": // Generate keys
				generateElGamalKeys();
				break;
			case "send": // send electionOptions or key
				userCommandSend();
				break;
			case "count": // count votes, only if election is over
				countVotes();
				break;
			case "exit": // Terminate program
				return;
			default: // Command not found
				System.out.println("Command not found");
				break;
			}
		}
	}

	private void countVotes() {
		if (electionRunning) {
			CommMessage request = CommMessage.createRequest("", aCommunicationPath, null); // TODO: null skal være void
			try {
				CommMessage response = sendMessage(request).recvResponseFor(request); // Den skal tage imod en value? som indeholder alle votes eller ingen votes hvis valget ikke er igang
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO: Handle response
			// Count votes if there are any or write error message to user
		} else {
			System.out.println("Election is not running");
		}
	}

	private void userCommandLoad() {
		System.out.println("Load keys or electionOption list?");
		String input = System.console().readLine().toLowerCase();

		switch (input) {
		case "keys":
		case "key":
			loadKeys();
			break;
		case "electionOptions":
		case "electionOption":
		case "electionOption list":
		case "electionOptionlist":
			loadElectionOptions();
			break;
		default:
			break;
		}
	}

	private void userCommandSend() {
		System.out.println("Send key or electionOption list?");
		String input = System.console().readLine().toLowerCase();

		switch (input) {
		case "keys":
		case "key":
			sendKey();
			break;
		case "electionOptions":
		case "electionOption":
		case "electionOption list":
		case "electionOptionlist":
			sendElectionoptions();
			break;
		default:
			break;
		}
	}

	/**
	 * Generates a new set of ElGamal keys and saves them to files, only if election is not running
	 */
	private void generateElGamalKeys() {
		if (!electionRunning) {
			Security.generateElGamalKeys();
			elGamalPublicKey = Security.getElgamalPublicKey();
			elGamalPrivateKey = Security.getElgamalPrivateKey();

			// Export keys
			Exporter.exportElGamalPrivateKeyParameters(elGamalPrivateKey, ElGamalPrivateKeyFile);
			Exporter.exportElGamalPublicKeyParameters(elGamalPublicKey, ElGamalPublicKeyFile);
		} else {
			System.out.println("Election is running");
		}
	}

	private void loadKeys() {
		if (!electionRunning) {
			elGamalPublicKey = Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
			elGamalPrivateKey = Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile);
		} else {
			System.out.println("Election is running");
		}
	}

	private void loadElectionOptions() {
		if (!electionRunning) {
			eOptions = Importer.importElectionOptions(electionOptionsFile);
			// TODO: load election options
			// set eOptions
		} else {
			System.out.println("Election is running");
		}
	}

	private void startElection() {
		if (!electionRunning) {
			CommMessage request = CommMessage.createRequest("startElection", aCommunicationPath, null); // TODO: null skal være void
			try {
				CommMessage response = sendMessage(request).recvResponseFor(request); // Den skal tage imod en value? som indeholder confirmation at election er startet og endtime? eller skal det sendes med?
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO: Handle response
			// show error if election is not started
			// ?set endtime? and set electionRunning
		} else {
			System.out.println("Election is not running");
		}
	}

	private void stopElection() {
		if (electionRunning) {
			CommMessage request = CommMessage.createRequest("stopElection", aCommunicationPath, null); // TODO: null skal være void
			try {
				CommMessage response = sendMessage(request).recvResponseFor(request); // Den skal tage imod en value? som indeholder confirmation at election er stoppet
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO: Handle response
			// Set electionRunning
		} else {
			System.out.println("Election is not running");
		}
	}

	private void sendKey() {
		if (!electionRunning) {
			CommMessage request = CommMessage.createRequest("sendPublicKey", aCommunicationPath, null); // TODO: null skal være publick key
			try {
				CommMessage response = sendMessage(request).recvResponseFor(request); // Den skal tage imod en value? som indeholder confirmation
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO: Handle response
			// show error if not confirmed
		} else {
			System.out.println("Election is running");
		}
	}

	private void sendElectionoptions() {
		if (!electionRunning) {
			if (eOptions != null) {
				CommMessage request = CommMessage.createRequest("sendElectionOptionList", aCommunicationPath, null); // TODO: null skal være election options
				try {
					CommMessage response = sendMessage(request).recvResponseFor(request);// Den skal tage imod en value? som indeholder confirmation
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// TODO: Handle response
				// show error if not confirmed
			} else {
				System.out.println("Load election options");
			}
		} else {
			System.out.println("Election is running");
		}
	}
}
