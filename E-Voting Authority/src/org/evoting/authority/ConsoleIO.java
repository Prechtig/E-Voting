package org.evoting.authority;

import java.io.IOException;
import java.sql.Timestamp;

import jolie.net.CommMessage;
import jolie.runtime.JavaService;
import jolie.runtime.Value;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
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

	/**
	 * Used to get the initial information about the election
	 * Sets if the election is running// ?and if it is, then what time it will end?
	 */
	public void initialize() {
		CommMessage request = CommMessage.createRequest("getElectionStatus", aCommunicationPath, null); //TODO: null skal være void
		try {
			CommMessage response = sendMessage( request ).recvResponseFor( request );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//TODO: Handle response
	}

	public void getUserInput() {
		initialize();
		
		while(true){
			System.out.println("Enter commmand: ");
			String input = System.console().readLine().toLowerCase();
			
			switch (input) {
			case "start": // Start election
				if(!electionRunning){
					startElection();
				}
				break;
			case "stop": // Stop election
				if(electionRunning){
					stopElection();
				}
				break;
			case "load": // Load electionOptions or keys
				if(!electionRunning){
					userCommandLoad();
				}	
				break;
			case "generate": //Generate keys
				if(!electionRunning){
					generateElGamalKeys();
				}
				break;
			case "send": //send electionOptions or key
				if(!electionRunning){
					userCommandSend();
				}

				break;
			case "count": // count votes, only if election is over
				if (electionRunning) {
					countVotes();
				}
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
		CommMessage request = CommMessage.createRequest("", aCommunicationPath, null); // TODO: null skal være void
		try {
			CommMessage response = sendMessage( request ).recvResponseFor( request );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO: Handle response
		
	}

	private void userCommandLoad(){
		System.out.println("Load keys or electionOption list?");
		String input = System.console().readLine().toLowerCase();

		switch (input) {
		case "keys":
		case "key":
			loadKeys(ElGamalPrivateKeyFile); //TODO: should only be called once and should not take a filepath then
			loadKeys(ElGamalPublicKeyFile);
			break;
		case "electionOptions": case "electionOption": case "electionOption list": case "electionOptionlist":
			loadElectionOptions();
			break;
		default:
			break;
		}
	}

	private void userCommandSend(){
		System.out.println("Send key or electionOption list?");
		String input = System.console().readLine().toLowerCase();

		switch (input) {
		case "keys":
		case "key":
			sendKey();
			break;
		case "electionOptions": case "electionOption": case "electionOption list": case "electionOptionlist":
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
		//TODO: If election is not running
		Security.generateElGamalKeys();
		elGamalPublicKey = Security.getElgamalPublicKey();
		elGamalPrivateKey = Security.getElgamalPrivateKey();

		// Export keys
		elGamalPublicKey = Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
		elGamalPrivateKey = Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile);
	}

	private void loadKeys(String fileName) {
		//TODO: If election is not running
		elGamalPublicKey = Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
		elGamalPrivateKey = Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile);
	}
	
	private void loadElectionOptions(){
		//TODO: load election options
	}
	
	private void startElection(){
		CommMessage request = CommMessage.createRequest("startElection", aCommunicationPath, null); // TODO: null skal være void
		try {
			CommMessage response = sendMessage( request ).recvResponseFor( request );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO: Handle response
	}
	
	private void stopElection() {
		CommMessage request = CommMessage.createRequest("stopElection", aCommunicationPath, null); // TODO: null skal være void
		try {
			CommMessage response = sendMessage( request ).recvResponseFor( request );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO: Handle response
	}
	
	private void sendKey(){
		CommMessage request = CommMessage.createRequest("sendPublicKey", aCommunicationPath, null); // TODO: null skal være publick key
		try {
			CommMessage response = sendMessage( request ).recvResponseFor( request );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO: Handle response
	}
	
	private void sendElectionoptions(){
		CommMessage request = CommMessage.createRequest("sendElectionOptionList", aCommunicationPath, null); // TODO: null skal være election options
		try {
			CommMessage response = sendMessage( request ).recvResponseFor( request );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO: Handle response
	}
}
