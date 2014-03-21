package org.evoting.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class used to represent a candidate 
 */
@Entity
@Table(name = "Candidates")
public class Candidate extends BaseEntity {
	//The name of the candidate
	@Column(name = "name", updatable = false, nullable = false)
	private String name;
	
	/**
	 * @param candidateId The id of the candidate
	 * @param name The name of the candidate
	 */
	public Candidate(int candidateId, String name) {
		super(candidateId);
		this.name = name;
	}
	
	/**
	 * @return The name of the candidate
	 */
	public String getName() {
		return this.name;
	}
}