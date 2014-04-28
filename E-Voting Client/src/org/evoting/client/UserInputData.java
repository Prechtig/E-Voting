package org.evoting.client;

/**
 * Contains user input to create a ballot and logic to hash the password.
 * @author Mark
 *
 */
public class UserInputData
{
	private String userId;
	private String password;
	private int electionOptionId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = Hasher.hashPassword(password);
	}
	public int getElectionOptionId() {
		return electionOptionId;
	}
	public void setElectionOptionId(int electionOptionId) {
		this.electionOptionId = electionOptionId;
	}
}
