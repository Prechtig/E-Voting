package org.evoting.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.evoting.database.entities.Candidate;
import org.evoting.database.entities.Timestamp;
import org.evoting.database.entities.Vote;
import org.evoting.database.repositories.CandidateRepository;
import org.evoting.database.repositories.TimestampRepository;
import org.evoting.database.repositories.VoteRepository;

public class HibernateTest {
	public static void main(String[] args) {
		Vote v1 = new Vote(0, "abc");
		Vote v2 = new Vote(1, "def");
		Candidate c1 = new Candidate(0, "Mikkel Hvilshøj Funch");
		Timestamp t = new Timestamp(0, 123456l);

		EntityManager entMgr = EntityManagerUtil.getEntityManagerFactory()
				.createEntityManager();
		EntityTransaction transaction = entMgr.getTransaction();
		transaction.begin();

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
		
		TimestampRepository tr = new TimestampRepository(entMgr);
		if(tr.findByTime(t.getTime()) == null) {
			entMgr.persist(t);
		}

		transaction.commit();
		entMgr.close();
	}
}