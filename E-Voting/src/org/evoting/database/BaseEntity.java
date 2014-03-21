package org.evoting.database;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
abstract public class BaseEntity {
	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private int id;
	
	public BaseEntity(int id) {
		this.id = id;
	}
	
	protected int getId() {
		return this.id;
	}
}