package org.evoting.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Used to get the connection to the persistent storage
 */
public class EntityManagerUtil {

	private static EntityManagerFactory emf;
	
	/**
	 * @return The EntityManagerFactory holding the connection to the persistent storage
	 */
	public static EntityManagerFactory getEntityManagerFactory() {
		if(emf == null) {
			emf = Persistence.createEntityManagerFactory("mysql-db");
		}
		return emf; 
	}
	
	/**
	 * @return A EntityManager with a open connection to the persistent storage
	 */
	public static EntityManager getEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}
}