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
	//The name of the electionOption
	@Column(name = "name", updatable = false, nullable = false)
	private String name;
	
	/**
	 * @param electionOptionId The id of the electionOption
	 * @param name The name of the electionOption
	 */
	public ElectionOption(int electionOptionId, String name) {
		super(electionOptionId);
		this.name = name;
	}
	
	/**
	 * Private constructor needed for Hibernate 
	 */
	@SuppressWarnings("unused")
	private ElectionOption() {
		super();
	}
	
	/**
	 * @return The name of the electionOption
	 */
	public String getName() {
		return this.name;
	}
}