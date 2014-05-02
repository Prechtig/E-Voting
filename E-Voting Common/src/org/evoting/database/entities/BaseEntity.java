package org.evoting.database.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base class for all entities 
 */
@MappedSuperclass
abstract public class BaseEntity {
	@Id @GeneratedValue
	@Column(name = "id", updatable = false, nullable = false)
	private int id;
	
	/**
	 * @return The id of the entity
	 */
	public int getId() {
		return this.id;
	}
}