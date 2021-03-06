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
	@Column(name = "electionOptionId", updatable = false, nullable = false)
	private int electionOptionId;
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
		this.electionOptionId = electionOptionId;
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
	
	public int getElectionOptionId() {
		return electionOptionId;
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
	
	public String toString() {
		return name + " with election option id: " + electionOptionId + " and party id: " + partyId;
	}
}