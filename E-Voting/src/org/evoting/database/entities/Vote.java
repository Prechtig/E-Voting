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
	@Column(name = "ciphertext", nullable = false)
	private String ciphertext;
	
	/**
	 * @param userId The id of the user
	 * @param ciphertext The encrypted vote for the user
	 */
	public Vote(int userId, String ciphertext) {
		super(userId);
		this.ciphertext = ciphertext;
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
	public String getCiphertext() {
		return this.ciphertext;
	}
	
	/**
	 * @param ciphertext The new encrypted vote
	 */
	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}
}