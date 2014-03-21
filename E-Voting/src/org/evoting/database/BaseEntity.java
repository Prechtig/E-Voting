package org.evoting.database;

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
	 * @return The id of the entity
	 */
	public int getId() {
		return this.id;
	}
}