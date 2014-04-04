package org.evoting.common;

public class ValueIdentifiers {
	private static final String userId = "userId";
	private static final String passwordHash = "passwordHash";
	// Name of the time stamp cipher text in the value object.
	private static final String timestamp = "timestamp";
	private static final String vote = "vote";
	// Name of the candidates cipher text in the value object.
	private static final String candidates = "candidates";
	private static final String rsaPublicKey = "rsaPublicKey";
	
	public static String getUserId() {
		return userId;
	}
	public static String getPasswordHash() {
		return passwordHash;
	}
	public static String getTimestamp() {
		return timestamp;
	}
	public static String getVote() {
		return vote;
	}
	public static String getCandidates() {
		return candidates;
	}
	public static String getRsaPublicKey() {
		return rsaPublicKey;
	}
}