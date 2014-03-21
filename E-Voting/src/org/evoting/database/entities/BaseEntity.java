package org.evoting.database.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base class for all entities 
 */
@MappedSuperclass
abstract public class BaseEntity {
	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private int id;
	
	/**
	 * @param id The id of the entity
	 */
	public BaseEntity(int id) {
		this.id = id;
	}
	
	/**
	 * Private constructor needed for Hibernate 
	 */
	protected BaseEntity() {
		//Do nothing
	}
	
	/**
	 * @return The id of the entity
	 */
	public int getId() {
		return this.id;
	}
}