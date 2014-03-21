package org.evoting.database;

import javax.persistence.EntityManager;

public class HibernateTest {
	public static void main(String[] args) {
		Vote v1 = new Vote(0, "abc");
		Vote v2 = new Vote(1, "def");
		
		EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
		
		VoteRepository vr = new VoteRepository(em);
		Vote v = vr.findById(0);
		v.getCiphertext();
		v.setCiphertext("ghi");
		//vr.persist(v1);
		//vr.persist(v2);
		
		em.close();
	}
}