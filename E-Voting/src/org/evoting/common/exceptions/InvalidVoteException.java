package org.evoting.common.exceptions;

public class InvalidVoteException extends Exception {
	private static final long serialVersionUID = -3138917252096658590L;
	
	private final String message;
	
	public InvalidVoteException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
