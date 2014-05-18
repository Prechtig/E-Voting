package org.evoting.common.jolie;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.evoting.common.exceptions.BadValueException;
import org.evoting.security.Security;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;

public class LoginResponse {
	private Boolean success;
	private String message;
	private byte[] signature;
	
	public LoginResponse(Boolean success) {
		this.success = success;
		this.message = nextRandomString();
		this.signature = generateSignature();
	}
	
	public LoginResponse(Value value) {
		this.success = value.getFirstChild("success").boolValue();
		Value validator = value.getFirstChild(ValueIdentifiers.getValidator());
		this.message = validator.getFirstChild(ValueIdentifiers.getMessage()).strValue();
		this.signature = validator.getFirstChild(ValueIdentifiers.getSignature()).byteArrayValue().getBytes();
		validate();
	}
	
	public Value getValue() {
		Value value = Value.create();
		value.getNewChild("success").setValue(success);
		Value validator = value.getNewChild(ValueIdentifiers.getValidator());
		validator.getNewChild(ValueIdentifiers.getMessage()).setValue(message);
		validator.getNewChild(ValueIdentifiers.getSignature()).setValue(new ByteArray(signature));
		return value;
	}
	
	private void validate() {
		String hash = Security.hash(getSignatureMessage());
		String decryptedHash = new String(Security.decryptRSA(signature, Security.getBulletinBoardRSAPublicKey()));
		if(!hash.equals(decryptedHash)) {
			throw new BadValueException();
		}
	}
	
	private byte[] generateSignature() {
		return Security.sign(getSignatureMessage(), Security.getBulletinBoardRSAPrivateKey());
	}
	
	private String getSignatureMessage() {
		String successString = (success ? "true" : "false");
		return successString + message;
	}
	
	private String nextRandomString() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
}