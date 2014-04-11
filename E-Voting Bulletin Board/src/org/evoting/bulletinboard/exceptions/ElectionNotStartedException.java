package org.evoting.bulletinboard.exceptions;

public class ElectionNotStartedException extends RuntimeException {
	private static final long serialVersionUID = 8734530964064129186L;
	private final String message = "The election has not yet started";
	
	@Override
	public String getMessage() {
		return message;
	}
}