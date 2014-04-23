package org.evoting.authority;

import java.io.IOException;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private PublicKey RASpublicKey;
	
	private String ElGamalPublicKeyFile = "ElGamalPublicKey";
	private String ElGamalPrivateKeyFile = "ElGamalPrivateKey";

	private ElGamalPublicKeyParameters elGamalPublicKey;
	private ElGamalPrivateKeyParameters elGamalPrivateKey;

	private boolean electionRunning;
	private Long endTime; //TODO: is this used?

	private String aCommunicationPath = "IAuthorityCommunication";
	private String electionOptionsFile = "ElectionOptions.txt";

	private static ElectionOptions eOptions;

	/**
	 * Used to get the initial information about the election. Sets if the election is running and, if it is, then what time it will end
	 */
	public void initialize() {
		CommMessage request = CommMessage.createRequest("getElectionStatus", aCommunicationPath, null);
		try {
			CommMessage response = sendMessage(request).recvResponseFor(request);
			
			electionRunning = response.value().getFirstChild("running").boolValue();
			endTime = response.value().getFirstChild("endTime").longValue();
			
			//endTime is -1 if some error happenden in bullitinboard
			if(endTime > -1){
				System.out.println("Election running: " + electionRunning);
			} else {
				System.out.println("Error in bullitinboard when trying to update election details");
			}
		} catch (IOException e) {
			System.out.println("Error communicating with bullitinboard");
			e.printStackTrace();
		}
	}

	public void getUserInput() {
		System.out.println("Initializing election details");
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
			case "update": // update the election status
				update();
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
			CommMessage request = CommMessage.createRequest("", aCommunicationPath, null);
			try {
				CommMessage response = sendMessage(request).recvResponseFor(request); // Den skal tage imod en value? som indeholder alle votes eller ingen votes hvis valget ikke er igang
				
				// TODO: Handle response
				// Count votes if there are any or write error message to user
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	private void userStartElection(){
		System.out.println("What time should the election stop? (kk:mm)");
		String input = System.console().readLine().toLowerCase();
		
		DateFormat df = new SimpleDateFormat();
		try {
			Date d = df.parse(input);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			
			System.out.println("Generated and exported new ElGamal keys");
		} else {
			System.out.println("Cannot generate new ElGamal keys while election is running");
		}
	}

	private void loadKeys() {
		if (!electionRunning) {
			elGamalPublicKey = Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
			elGamalPrivateKey = Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile);
			
			System.out.println("Loaded ElGamal keys");
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

	private void startElection() {
		if (!electionRunning) {
			//TODO:Should it send an endtime?
			CommMessage request = CommMessage.createRequest("startElection", aCommunicationPath, null);
			try {
				CommMessage response = sendMessage(request).recvResponseFor(request);

				if (response.value().getFirstChild("Confirmation").boolValue()) {
					electionRunning = true;
					System.out.println("Election has started");
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

	private void stopElection() {
		if (electionRunning) {
			CommMessage request = CommMessage.createRequest("stopElection", aCommunicationPath, null);
			try {
				CommMessage response = sendMessage(request).recvResponseFor(request);

				if (response.value().getFirstChild("Confirmation").boolValue()) {
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

	private void sendKey() {
		if (!electionRunning) {
			if (elGamalPublicKey != null && elGamalPrivateKey != null) {
				Value key = getPublicKeyValue();

				CommMessage request = CommMessage.createRequest("sendPublicKey", aCommunicationPath, key);
				try {
					CommMessage response = sendMessage(request).recvResponseFor(request);

					if (response.value().getFirstChild("Confirmation").boolValue()) {
						electionRunning = false;
						System.out.println("ElGamal public Key successfully sent");
					} else {
						System.out.println("Error in bullitinboard when trying to send ElGamal public key");
					}
				} catch (IOException e) {
					System.out.println("Error communicating with bullitinboard");
					e.printStackTrace();
				}
			} else{
				System.out.println("No ElGamal keys loaded");
			}
		} else {
			System.out.println("Cannot send ElGamal key while election is running");
		}
	}

	private void sendElectionoptions() {
		if (!electionRunning) {
			if (eOptions != null) {
				//TODO:Create value containing all the electionoptions
				CommMessage request = CommMessage.createRequest("sendElectionOptionList", aCommunicationPath, null); // TODO: null skal være election options
				try {
					CommMessage response = sendMessage(request).recvResponseFor(request);// Den skal tage imod en value? som indeholder confirmation
					if(response.value().boolValue()){
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
	 * Used to update election status
	 */
	private void update() {
		initialize();
	}

	private Value getPublicKeyValue() {
		Value result = Value.create();
		if (elGamalPublicKey != null) {
			String y = elGamalPublicKey.getY().toString();
			String p = elGamalPublicKey.getParameters().getP().toString();
			String g = elGamalPublicKey.getParameters().getG().toString();
			int l = elGamalPublicKey.getParameters().getL();

			result.getNewChild("y").setValue(y);
			Value elgamalParametersValue = result.getNewChild("parameters");
			elgamalParametersValue.getNewChild("p").setValue(p);
			elgamalParametersValue.getNewChild("g").setValue(g);
			elgamalParametersValue.getNewChild("l").setValue(l);
		}
		return result;
	}
	
	
	//TODO: should it downlaod the list of election options if the election is running?
}
