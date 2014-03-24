package org.evoting.client;

/*
 * Contains user input to create a ballot and logic to hash the password.
 */
public class UserInputData
{
	private int userId;
	private String password;
	private int candidateId;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = Security.hashPassword(password);
	}
	public int getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(int candidateId) {
		this.candidateId = candidateId;
	}
}
