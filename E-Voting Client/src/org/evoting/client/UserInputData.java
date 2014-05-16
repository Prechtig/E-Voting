package org.evoting.client;

import org.evoting.common.jolie.LoginRequest;

/**
 * Contains user input to create a ballot and logic to hash the password.
 * @author Mark
 *
 */
public class UserInputData
{
	private LoginRequest loginRequest;
	private int electionOptionId;
	
	public String getUserId() {
		return loginRequest.getUserId();
	}
	
	public String getPasswordHash() {
		return loginRequest.getPasswordHash();
	}
	
	public void setLoginRequest(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
	}

	public int getElectionOptionId() {
		return electionOptionId;
	}
	public void setElectionOptionId(int electionOptionId) {
		this.electionOptionId = electionOptionId;
	}
}
