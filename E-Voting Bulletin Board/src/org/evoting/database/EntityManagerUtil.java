package org.evoting.database;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Used to get the connection to the persistent storage 
 */
public class EntityManagerUtil {
	
	/**
	 * @return The EntityManager holding the connection to the persistent storage
	 */
	public static EntityManagerFactory getEntityManagerFactory()
	   {
	      EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql-db");
	      return emf;
	   }
}