package org.evoting.bulletinboard.exceptions;

public class InvalidUserInformationException extends RuntimeException {

	private static final long serialVersionUID = -1193608821849213037L;
	private final String message;
	
	public InvalidUserInformationException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}