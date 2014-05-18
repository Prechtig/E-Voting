package org.evoting.common.jolie;

import org.evoting.common.exceptions.BadValueException;
import org.evoting.security.Security;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;

public class LoginRequest {
	private byte[] userId;
	private byte[] passwordHash;
	
	public LoginRequest(String userId, String passwordHash) {
		this.userId = Security.encryptRSA(userId, Security.getBulletinBoardRSAPublicKey());
		this.passwordHash = Security.encryptRSA(passwordHash, Security.getBulletinBoardRSAPublicKey());
	}
	
	public LoginRequest(Value value) {
		if(!value.hasChildren(ValueIdentifiers.getUserId()) ||
		   !value.hasChildren(ValueIdentifiers.getPasswordHash())) {
			throw new BadValueException();
		}
		this.userId = value.getFirstChild(ValueIdentifiers.getUserId()).byteArrayValue().getBytes();
		this.passwordHash = value.getFirstChild(ValueIdentifiers.getPasswordHash()).byteArrayValue().getBytes();
	}
	
	public Value getValue() {
		Value result = Value.create();
		result.getNewChild(ValueIdentifiers.getUserId()).setValue(new ByteArray(userId));
		result.getNewChild(ValueIdentifiers.getPasswordHash()).setValue(new ByteArray(passwordHash));
		result.getNewChild(ValueIdentifiers.getSid()).setValue("");
		
		return result;
	}
	
	public byte[] getUserId() {
		return userId;
	}

	public byte[] getPasswordHash() {
		return passwordHash;
	}
}
