package org.evoting.common.exceptions;

public class CorruptDataException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String message;
	private final static String defaultMessage = "The data did not pass the checksum test.";
	
	public CorruptDataException() {
		message = defaultMessage;
	}
	
	public CorruptDataException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
