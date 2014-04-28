package org.evoting.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.evoting.database.entities.BaseEntity;

/**
 * Class used to represent a vote
 */
@Entity
@Table(name = "Votes")
public class Vote extends BaseEntity {
	// The encrypted vote
	@Column(name = "encryptedVote", columnDefinition = "blob", nullable = false)
	private byte[][] encryptedVote;
	
	@Column(name = "timestamp", nullable = false)
	private long timestamp;
	
	/**
	 * @param userId The id of the user
	 * @param encryptedVote The encrypted vote for the user
	 */
	public Vote(int userId, byte[][] encryptedVote, long timestamp) {
		super(userId);
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
}