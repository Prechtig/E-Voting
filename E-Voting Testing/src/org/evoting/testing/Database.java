package org.evoting.testing;

import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityManager;

import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.Converter;
import org.evoting.database.EntityManagerUtil;
import org.evoting.database.entities.Vote;
import org.evoting.database.repositories.VoteRepository;
import org.evoting.security.Security;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Database {
	private static ElGamalPublicKeyParameters pubKey;
	
	@BeforeClass
	public static void before() {
		if (!Security.RSAKeysSat()) {
			Security.generateRSAKeys();
		}
		if (!Security.ElGamalKeysSat()) {
			Security.generateElGamalKeys();
		}
		pubKey = Security.getElgamalPublicKey();
	}

	@Test
	public void testFindValidVotes() {
		// Generate the keys used to encrypt/decrypt

		byte[] zero = Converter.toByteArray(0);
		byte[] one = Converter.toByteArray(1);

		byte[][] vote0 = new byte[][] { Security.encryptElGamal(one, pubKey),
				Security.encryptElGamal(zero, pubKey), Security.encryptElGamal(zero, pubKey) };
		byte[][] vote1 = new byte[][] { Security.encryptElGamal(zero, pubKey),
				Security.encryptElGamal(one, pubKey), Security.encryptElGamal(zero, pubKey) };
		byte[][] vote2 = new byte[][] { Security.encryptElGamal(zero, pubKey),
				Security.encryptElGamal(zero, pubKey), Security.encryptElGamal(one, pubKey) };

		Vote v1 = new Vote("-1", vote0, 1000);
		Vote v2 = new Vote("-1", vote1, 2000);
		Vote v3 = new Vote("-1", vote2, 3000);
		
		EntityManager entMgr = EntityManagerUtil.getEntityManager();
		entMgr.getTransaction().begin();

		entMgr.persist(v1);
		entMgr.persist(v2);
		entMgr.persist(v3);

		VoteRepository vr = new VoteRepository(entMgr);
		
		List<Vote> allVotes = vr.findAll();
		if(!allVotes.contains(v1)) {
			fail("Did not find v1");
		}
		if(!allVotes.contains(v2)) {
			fail("Did not find v2");
		}
		if(!allVotes.contains(v3)) {
			fail("Did not find v3");
		}
		
		List<Vote> validVotes = vr.findAllValid();
		if(validVotes.contains(v1) || validVotes.contains(v2)) {
			fail("Old vote found");
		}
		
		entMgr.getTransaction().commit();
		entMgr.getEntityManagerFactory().close();
	}
	
	@AfterClass
	public static void after() {
		EntityManager entMgr = EntityManagerUtil.getEntityManager();
		entMgr.getTransaction().begin();
		
		VoteRepository vr = new VoteRepository(entMgr);
		
		List<Vote> votes = vr.findByUserId("-1");
		for(Vote v : votes) {
			entMgr.remove(v);
		}
		
		entMgr.getTransaction().commit();
		entMgr.getEntityManagerFactory().close();
	}
}