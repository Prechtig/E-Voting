package org.evoting.database;

import javax.persistence.EntityManager;

import org.evoting.database.entities.Candidate;
import org.evoting.database.entities.Vote;
import org.evoting.database.repositories.CandidateRepository;
import org.evoting.database.repositories.VoteRepository;

public class HibernateTest {
	public static void main(String[] args) {
		Vote v1 = new Vote(0, new byte[] { -0x80, 0x01});
		Vote v2 = new Vote(1, new byte[] { 0x7f, 0x02});
		Candidate c1 = new Candidate(0, "First Candidate");

		EntityManager entMgr = EntityManagerUtil.getEntityManager();
		entMgr.getTransaction().begin();

		VoteRepository vr = new VoteRepository(entMgr);
		if (vr.findById(v1.getId()) == null) {
			entMgr.persist(v1);
		}
		if (vr.findById(v2.getId()) == null) {
			entMgr.persist(v2);
		}

		CandidateRepository cr = new CandidateRepository(entMgr);
		if (cr.findById(c1.getId()) == null) {
			entMgr.persist(c1);
		}

		entMgr.getTransaction().commit();
		entMgr.getEntityManagerFactory().close();
	}
}