package org.evoting.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class HibernateTest {
	public static void main(String[] args) {
		Vote v1 = new Vote(0, "abc");
		Vote v2 = new Vote(1, "def");
		Candidate c1 = new Candidate(0, "Mikkel Hvilshøj Funch");

		EntityManager entMgr = EntityManagerUtil.getEntityManagerFactory()
				.createEntityManager();
		EntityTransaction transaction = entMgr.getTransaction();
		transaction.begin();

		VoteRepository vr = new VoteRepository(entMgr);
		if (vr.findById(v1.getUserId()) == null) {
			entMgr.persist(v1);
		}
		if (vr.findById(v2.getUserId()) == null) {
			entMgr.persist(v2);
		}

		CandidateRepository cr = new CandidateRepository(entMgr);
		if (cr.findById(c1.getCandidateId()) == null) {
			entMgr.persist(c1);
		}

		transaction.commit();
		entMgr.close();
	}
}