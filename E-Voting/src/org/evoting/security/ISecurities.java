package org.evoting.security;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;

public interface ISecurities {
	//Encrypt username and password from C to BB
	//public String encryptUsernameAndPassword(String u, String p);
	public String[] encryptElGamalToBB(String u, String p, ElGamalPrivateKeyParameters pK); //Used in C
	
	//Encrypt candidate list from BB to C
	//public String encryptCandidateList(String candidates);
	public String encryptSignatureToCC(String candidates); // Used in BB
	
	//Encrypt ballot from C to BB using A's public key
	//public String encryptBallot(String ballot);
	public String encryptElGamalToA(String b, ElGamalPrivateKeyParameters pK); // Used in C
	
	//Encrypt response after receiving ballot from BB to C? Can be set together with encryptCandidateList
	public String encryptResponse(String response); // Used in BB
	//public String encryptSignatureToCC(String response);)
	
}
