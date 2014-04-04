package org.evoting.authority;

import java.io.Console;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.Importer;
import org.evoting.security.Security;

public class ConsoleIO {
	private static String ElGamalPublicKeyFile = "ElGamalPublicKey";
	private static String ElGamalPrivateKeyFile = "ElGamalPrivateKey";
	
	private static ElGamalPublicKeyParameters elGamalPublicKey;
	private static ElGamalPrivateKeyParameters elGamalPrivateKey;
	
	public static void getUserInput(int numberOfCandidates)
	{
		System.out.println("Enter commmand: ");
		String input = System.console().readLine().toLowerCase();
		
		switch (input) {
		case "start":
			//Start Election
			break;
		case "stop":
			//Stop election
			break;
		case "load":
			//
			break;
		case "generate":
			//Generate keys
			generateElGamalKeys();
			break;
		case "send":
			//send candidates or key
			break;
		case "count":
			//count votes, only if election is over
			break;
		default:
			//Command not found
			break;
		}
	}
	
	public static void userCommandLoad(){
		System.out.println("Load keys or candidate list?");
		String input = System.console().readLine().toLowerCase();
		
		switch (input) {
		case "keys": case "key":
			//load keys
			break;
		case "candidates": case "candidate": case "candidate list": case "candidatelist":
			//load candidatelist
			break;
		default:
			break;
		}
	}
	
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
