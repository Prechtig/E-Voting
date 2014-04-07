package org.evoting.common.exceptions;

public class BadValueException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String message;
	private final static String defaultMessage = "One or more required node was missing.";
	
	public BadValueException() {
		message = defaultMessage;
	}
	
	public BadValueException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
