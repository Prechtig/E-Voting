package org.evoting.common;

public class ValueIdentifiers {
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String PARTY_ID = "partyId";
	private static final String USER_ID = "userId";
	private static final String PASSWORD_HASH = "passwordHash";
	private static final String ELECTION_ID = "electionId";
	private static final String VOTE = "vote";
	// Name of the electionOptions cipher text in the value object.
	private static final String ELECTION_OPTIONS = "electionOptions";
	private static final String RSA_PUBLIC_KEY = "rsaPublicKey";
	//Names of the public keys and parameters in the public keys value object.
	private static final String ELGAMAL_PUBLIC_KEY = "elgamalPublicKey";
	private static final String PARAMETERS = "parameters";
	private static final String Y = "y";
	private static final String P = "p";
	private static final String G = "g";
	private static final String L = "l";
	private static final String START_TIME = "startTime";
	private static final String END_TIME = "endTime";
	private static final String SIGNATURE = "signature";
	private static final String VOTES = "votes";
	
	public static String getVotes() {
		return VOTES;
	}
	public static String getId() {
		return ID;
	}
	public static String getName() {
		return NAME;
	}
	public static String getPartyId() {
		return PARTY_ID;
	}
	public static String getUserId() {
		return USER_ID;
	}
	public static String getPasswordHash() {
		return PASSWORD_HASH;
	}
	public static String getElectionId() {
		return ELECTION_ID;
	}
	public static String getVote() {
		return VOTE;
	}
	public static String getElectionOptions() {
		return ELECTION_OPTIONS;
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
	public static String getStartTime() {
		return START_TIME;
	}
	public static String getEndTime() {
		return END_TIME;
	}
	public static String getSignature() {
		return SIGNATURE;
	}
}