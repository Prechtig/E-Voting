package org.evoting.common;

public class ValueIdentifiers {
	private static final String USER_ID = "userId";
	private static final String PASSWORD_HASH = "passwordHash";
	// Name of the time stamp cipher text in the value object.
	private static final String TIMESTAMP = "timestamp";
	private static final String VOTE = "vote";
	// Name of the candidates cipher text in the value object.
	private static final String CANDIDATES = "candidates";
	private static final String RSA_PUBLIC_KEY = "rsaPublicKey";
	//Names of the public keys and parameters in the public keys value object.
	private static final String ELGAMAL_PUBLIC_KEY = "elgamalPublicKey";
	private static final String PARAMETERS = "parameters";
	private static final String Y = "y";
	private static final String P = "p";
	private static final String G = "g";
	private static final String L = "l";
	
	public static String getUserId() {
		return USER_ID;
	}
	public static String getPasswordHash() {
		return PASSWORD_HASH;
	}
	public static String getTimestamp() {
		return TIMESTAMP;
	}
	public static String getVote() {
		return VOTE;
	}
	public static String getCandidates() {
		return CANDIDATES;
	}
	public static String getRsaPublicKey() {
		return RSA_PUBLIC_KEY;
	}
	public static String getElGamalPublicKey() {
		return ELGAMAL_PUBLIC_KEY;
	}
	public static String getParameters() {
		return PARAMETERS;
	}
	public static String getY() {
		return Y;
	}
	public static String getP() {
		return P;
	}
	public static String getG() {
		return G;
	}
	public static String getL() {
		return L;
	}
}