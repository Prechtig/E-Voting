package org.evoting.database.exceptions;

public class DatabaseInvariantException extends Exception {
	private static final long serialVersionUID = 7322972494491203214L;

	private final String message;
	
	public DatabaseInvariantException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}