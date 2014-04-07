package org.evoting.authority;

import java.sql.Timestamp;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.Importer;
import org.evoting.security.Security;

public class ConsoleIO {
	private static String ElGamalPublicKeyFile = "ElGamalPublicKey";
	private static String ElGamalPrivateKeyFile = "ElGamalPrivateKey";
	
	private static ElGamalPublicKeyParameters elGamalPublicKey;
	private static ElGamalPrivateKeyParameters elGamalPrivateKey;
	
	private static boolean electionRunning;
	private static Timestamp endTime;
	
	public static void initialize(boolean e, Timestamp t){
		electionRunning = e;
		endTime = t;
	}
	
	public static void getUserInput(int numberOfElectionOptions)
	{
		while(true){
		System.out.println("Enter commmand: ");
		String input = System.console().readLine().toLowerCase();
		
		switch (input) {
		case "start":
			if(!electionRunning){
				//Start Election
			}
			break;
		case "stop":
			if(electionRunning){
				//Stop election
			}
			break;
		case "load":
			if(!electionRunning){
				userCommandLoad();
			}	
			break;
		case "generate":
			//Generate keys
			if(!electionRunning){
				generateElGamalKeys();
			}
			break;
		case "send":
			if(!electionRunning){
				//send electionOptions or key
			}

			break;
		case "count":
			if(electionRunning){
				//count votes, only if election is over
			}
			break;
		case "exit":
			return;
		default:
			//Command not found
			System.out.println("Command not found");
			break;
		}
		}
	}
	
	public static void userCommandLoad(){
		System.out.println("Load keys or electionOption list?");
		String input = System.console().readLine().toLowerCase();
		
		switch (input) {
		case "keys": case "key":
			loadKeys(ElGamalPrivateKeyFile);
			loadKeys(ElGamalPublicKeyFile);
			break;
		case "electionOptions": case "electionOption": case "electionOption list": case "electionOptionlist":
			//load electionOptionlist
			break;
		default:
			break;
		}
	}
	
	public static void userCommandSend(){
		System.out.println("Send key or electionOption list?");
		String input = System.console().readLine().toLowerCase();
		
		switch (input) {
		case "keys": case "key":
			// send key
			break;
		case "electionOptions": case "electionOption": case "electionOption list": case "electionOptionlist":
			//load electionOptionlist
			break;
		default:
			break;
		}
	}
	
	/**
	 * Generates a new set of ElGamal keys and saves them to files, only if election is not running
	 */
	public static void generateElGamalKeys(){
		//If election is not running
		Security.generateElGamalKeys();
		elGamalPublicKey = Security.getElgamalPublicKey();
		elGamalPrivateKey = Security.getElgamalPrivateKey();
		
		//Export keys
		
		
		elGamalPublicKey = Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
		elGamalPrivateKey = Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile);
	}
	
	public static void loadKeys(String fileName){
		//If election is not running
		elGamalPublicKey = Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
		elGamalPrivateKey = Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile);
	}
}
