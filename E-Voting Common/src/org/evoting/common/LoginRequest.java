package org.evoting.common;

import org.evoting.common.exceptions.BadValueException;

import jolie.runtime.Value;

public class LoginRequest {
	private String userId;
	private String passwordHash;
	
	public LoginRequest(String userId, String passwordHash) {
		this.userId = userId;
		this.passwordHash = passwordHash;
	}
	
	public LoginRequest(Value value) {
		if(!value.hasChildren(ValueIdentifiers.getUserId()) ||
		   !value.hasChildren(ValueIdentifiers.getPasswordHash())) {
			throw new BadValueException();
		}
			
		int userId = value.getFirstChild(ValueIdentifiers.getUserId()).intValue();
		String passwordHash = value.getFirstChild(ValueIdentifiers.getPasswordHash()).strValue();
	}
	
	public Value getValue() {
		Value result = Value.create();
		result.getNewChild(ValueIdentifiers.getUserId()).setValue(userId);
		result.getNewChild(ValueIdentifiers.getPasswordHash()).setValue(passwordHash);
		
		return result;
	}
	
	public String getUserId() {
		return userId;
	}

	public String getPasswordHash() {
		return passwordHash;
	}
}
