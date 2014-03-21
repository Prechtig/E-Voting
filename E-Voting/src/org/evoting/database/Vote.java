package org.evoting.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Votes")
public class Vote {
	
	@Id
	@Column(name = "userId", updatable = false, nullable = false)
	private int userId;
	
	@Column(name = "ciphertext", nullable = false)
	private String ciphertext;
	
	public Vote() {
		
	}
	
	public Vote(int userId, String ciphertext) {
		this.userId = userId;
		this.ciphertext = ciphertext;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public String getCiphertext() {
		return this.ciphertext;
	}
	
	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}
}