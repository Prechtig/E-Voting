package org.evoting.authority;

import org.evoting.security.Security;
import jolie.runtime.JavaService;

public class Controller extends JavaService {
	private static String ElGamalPublicKeyFile = "ElGamalPublicKey";
	private static String ElGamalPrivateKeyFile = "ElGamalPrivateKey";
	
	public static void startElection(){
		
	}
	
	public static void stopElection(){
		
	}
	
	/**
	 * Generates a new set of ElGamal keys and saves them to files, only if election is not running
	 */
	public static void generateElGamalKeys(){
		//If election is not running
		Security.generateElGamalKeys();
		Security.saveElGamalPrivateKey(Security.getElgamalPrivatecKey(), ElGamalPrivateKeyFile);
		Security.saveElGamalPublicKey(Security.getElgamalPublicKey(), ElGamalPublicKeyFile);
	}
	
	public static void loadKeys(String fileName){
		//If election is not running
		Security.loadElGamalPrivateKey(ElGamalPrivateKeyFile);
		Security.loadElGamalPublicKey(ElGamalPublicKeyFile);
	}
	
	public static void loadCandidates(String fileName){
		
	}
	
	public static void sendCandidates(){
		
	}
	
	public static void countVotes(){
		
	}
}
