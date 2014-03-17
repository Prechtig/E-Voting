package org.evoting.database;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class EntityManagerUtil {
	public static EntityManagerFactory getEntityManagerFactory()
	   {
	      EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql-db");
	      return emf;
	   }
}