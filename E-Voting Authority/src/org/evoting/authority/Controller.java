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
	
	public static void countVotes(){
		
	}
	
	public static void generateElGamalKeys(){
		Security.generateElGamalKeys();
		Security.saveElGamalPrivateKey(Security.getElgamalPrivatecKey(), ElGamalPrivateKeyFile);
		Security.saveElGamalPublicKey(Security.getElgamalPublicKey(), ElGamalPublicKeyFile);
	}
	
	public static void loadKeys(String fileName){
		
	}
	
	public static void loadCandidates(String fileName){
		
	}
	
	public static void sendCandidates(){
		
	}
}
