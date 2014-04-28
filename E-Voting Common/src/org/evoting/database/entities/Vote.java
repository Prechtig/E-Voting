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
	@Column(name = "userId", nullable = false)
	private String userId;
	
	// The encrypted vote
	@Column(name = "encryptedVote", columnDefinition = "blob", nullable = false)
	private byte[][] encryptedVote;
	
	@Column(name = "timestamp", nullable = false)
	private long timestamp;
	
	/**
	 * @param userId The id of the user
	 * @param encryptedVote The encrypted vote for the user
	 */
	public Vote(String userId, byte[][] encryptedVote, long timestamp) {
		super();
		this.userId = userId;
		this.encryptedVote = encryptedVote;
		this.timestamp = timestamp;
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
	public byte[][] getEncryptedVote() {
		return this.encryptedVote;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
}