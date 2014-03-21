package org.evoting.database;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract public class IdentifiableEntity {
	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private int id;
	
	public IdentifiableEntity(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
}