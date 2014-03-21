package org.evoting.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Candidates")
public class Candidate extends IdentifiableEntity {
	@Column(name = "name", updatable = false, nullable = false)
	private String name;
	
	public Candidate(int candidateId, String name) {
		super(candidateId);
		this.name = name;
	}
	
	public int getCandidateId() {
		return super.getId();
	}
	
	public String getName() {
		return this.name;
	}
}