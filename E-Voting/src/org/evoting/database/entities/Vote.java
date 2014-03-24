package org.evoting.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class used to represent a vote
 */
@Entity
@Table(name = "Votes")
public class Vote extends BaseEntity {
	// The encrypted vote
	@Column(name = "encryptedVote", nullable = false)
	private byte[] encryptedVote;
	
	/**
	 * @param userId The id of the user
	 * @param encryptedVote The encrypted vote for the user
	 */
	public Vote(int userId, byte[] encryptedVote) {
		super(userId);
		this.encryptedVote = encryptedVote;
	}
	
	/**
	 * Private constructor needed for Hibernate 
	 */
	@SuppressWarnings("unused")
	private Vote() {
		super();
	}
	
	/**
	 * @return The encrypted vote
	 */
	public byte[] getEncryptedVote() {
		return this.encryptedVote;
	}
	
	/**
	 * @param ciphertext The new encrypted vote
	 */
	public void setEncryptedVote(byte[] encryptedVote) {
		this.encryptedVote = encryptedVote;
	}
}