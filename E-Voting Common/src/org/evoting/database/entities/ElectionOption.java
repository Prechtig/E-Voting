package org.evoting.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class used to represent a electionOption 
 */
@Entity
@Table(name = "ElectionOptions")
public class ElectionOption extends BaseEntity {
	@Column(name = "electionId", updatable = false, nullable = false)
	private int electionId;
	//The name of the electionOption
	@Column(name = "name", updatable = false, nullable = false)
	private String name;
	@Column(name = "partyId", updatable = false, nullable = false)
	private int partyId;
	
	/**
	 * @param electionOptionId The id of the electionOption
	 * @param name The name of the electionOption
	 */
	public ElectionOption(int electionOptionId, String name, int partyId) {
		super();
		this.electionId = electionOptionId;
		this.name = name;
		this.partyId = partyId;
	}
	
	/**
	 * Private constructor needed for Hibernate 
	 */
	@SuppressWarnings("unused")
	private ElectionOption() {
		super();
	}
	
	public int getElectionId() {
		return electionId;
	}
	
	/**
	 * @return The name of the electionOption
	 */
	public String getName() {
		return this.name;
	}
	
	public int getPartyId() {
		return this.partyId;
	}
}