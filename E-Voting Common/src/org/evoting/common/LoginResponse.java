package org.evoting.common;

import jolie.runtime.Value;

public class LoginResponse {
	private Boolean success;
	private String sid;
	
	public LoginResponse(Boolean success, String sid) {
		this.success = success;
		this.sid = sid;
	}
	
	public LoginResponse(Value value) {
		this.success = value.getFirstChild("success").boolValue();
		this.sid = value.getFirstChild("sid").strValue();
	}
	
	public Value getValue() {
		Value value = Value.create();
		value.getNewChild("success").setValue(success);
		value.getNewChild("sid").setValue(sid);
		return value;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	public String getSid() {
		return sid;
	}
}