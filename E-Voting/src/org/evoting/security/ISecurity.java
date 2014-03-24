package org.evoting.security;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;

public interface ISecurity {
	//Encrypt username and password from C to BB using BB's public key
	//public String encryptUsernameAndPassword(String u, String p);
	public byte[] encryptElGamal(String m, ElGamalPublicKeyParameters pK); //Used in C
	public byte[] encryptElGamal(byte[] m, ElGamalPublicKeyParameters pK);
	
	//Encrypt candidate list from BB to C
	//public String encryptCandidateList(String candidates);
	public byte[] encryptRSA(String hash, PrivateKey pK); // Used in BB
	public byte[] encryptRSA(byte[] hash, PrivateKey pK);
	
	//Encrypt ballot from C to BB using A's public key
	//public String encryptBallot(String ballot);
	//public String encryptElGamal(String b, ElGamalPublicKeyParameters pK); // Used in C
	
	//Encrypt response after receiving ballot from BB to C? Can be set together with encryptCandidateList
	//public String encryptRSA(String response); // Used in BB
	//public String encryptSignatureToCC(String response);)
	
	public byte[] decryptElgamal(byte[] m, ElGamalPrivateKeyParameters pK);
	public byte[] decryptRSA(byte[] m, PublicKey pK);
	
	public String hash(String m);
	
	public byte[] sign(String m, PrivateKey pK);
}
