package org.evoting.common.jolie;

import java.util.Date;

import jolie.runtime.Value;

import org.evoting.common.exceptions.BadValueException;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.security.Security;

public class ElectionStart {
	
	private Date startTime, endTime;
	
	public ElectionStart(Value value) {
		if(!value.hasChildren(ValueIdentifiers.getValidator()) ||
		   !value.hasChildren(ValueIdentifiers.getStartTime()) ||
		   !value.hasChildren(ValueIdentifiers.getEndTime())) {
					throw new BadValueException();
				}
		validate(value.getFirstChild(ValueIdentifiers.getValidator()));

		long start = value.getFirstChild(ValueIdentifiers.getStartTime()).longValue();
		long end = value.getFirstChild(ValueIdentifiers.getEndTime()).longValue();

		this.startTime = new Date(start);
		this.endTime = new Date(end);
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	private void validate(Value validation) {
		String message = validation.getFirstChild(ValueIdentifiers.getMessage()).strValue();
		byte[] signature = validation.getFirstChild(ValueIdentifiers.getSignature()).byteArrayValue().getBytes();
		String hashedMessage = Security.hash(message);
		if (!hashedMessage.equals(new String(Security.decryptRSA(signature, Security.getAuthorityRSAPublicKey())))) {
			throw new CorruptDataException("Validation error");
		}
	}
}