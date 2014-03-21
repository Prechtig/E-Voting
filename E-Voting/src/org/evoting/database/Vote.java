package org.evoting.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Votes")
public class Vote extends BaseEntity {
	@Column(name = "ciphertext", nullable = false)
	private String ciphertext;
	
	public Vote(int userId, String ciphertext) {
		super(userId);
		this.ciphertext = ciphertext;
	}
	
	public String getCiphertext() {
		return this.ciphertext;
	}
	
	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}
}