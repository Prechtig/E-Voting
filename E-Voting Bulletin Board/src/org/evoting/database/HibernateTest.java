package org.evoting.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.Converter;
import org.evoting.database.entities.ElectionOption;
import org.evoting.database.entities.Election;
import org.evoting.database.entities.Vote;
import org.evoting.database.repositories.ElectionOptionRepository;
import org.evoting.database.repositories.ElectionRepository;
import org.evoting.database.repositories.VoteRepository;
import org.evoting.security.Security;

public class HibernateTest {
	public static void main(String[] args) throws ParseException {
		//Generate the keys used to encrypt/decrypt
		if(!Security.RSAKeysSat()) {
			Security.generateRSAKeys();
		}
		if(!Security.ElGamalKeysSat()) {
			Security.generateElGamalKeys(true);
		}
		
		ElGamalPublicKeyParameters pubKey = Security.getElgamalPublicKey();
		
		Vote v1 = new Vote("310725-9348", new byte[][] { Security.encryptElGamal(Converter.toByteArray(1), pubKey), Security.encryptElGamal(Converter.toByteArray(0), pubKey), Security.encryptElGamal(Converter.toByteArray(0), pubKey) }, System.currentTimeMillis());
		Vote v2 = new Vote("150199-7946", new byte[][] { Security.encryptElGamal(Converter.toByteArray(0), pubKey), Security.encryptElGamal(Converter.toByteArray(1), pubKey), Security.encryptElGamal(Converter.toByteArray(0), pubKey) }, System.currentTimeMillis());
		ElectionOption p0 = new ElectionOption(0, "Vores Parti", 0);
		ElectionOption c0 = new ElectionOption(1, "Mikkel Hvilshøj Funch", 0);
		ElectionOption c1 = new ElectionOption(2, "Mark Thorhauge", 0);
		ElectionOption c2 = new ElectionOption(3, "Andreas Precht Poulsen", 0);
		ElectionOption c3 = new ElectionOption(4, "Gregers Jensen", 0);
		Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2014-06-30 08:00");
		Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2014-06-31 20:00");

		Election t0 = new Election(startDate, endDate);

		EntityManager entMgr = EntityManagerUtil.getEntityManager();
		entMgr.getTransaction().begin();

		VoteRepository vr = new VoteRepository(entMgr);
		if (vr.findById(v1.getId()) == null) {
			entMgr.persist(v1);
		}
		if (vr.findById(v2.getId()) == null) {
			entMgr.persist(v2);
		}

		ElectionOptionRepository cr = new ElectionOptionRepository(entMgr);
		if (cr.findByElectionOptionId(p0.getId()) == null) {
			entMgr.persist(p0);
		}
		if (cr.findByElectionOptionId(c0.getId()) == null) {
			entMgr.persist(c0);
		}
		if (cr.findByElectionOptionId(c1.getId()) == null) {
			entMgr.persist(c1);
		}
		if (cr.findByElectionOptionId(c2.getId()) == null) {
			entMgr.persist(c2);
		}
		if (cr.findByElectionOptionId(c3.getId()) == null) {
			entMgr.persist(c3);
		}
		
		ElectionRepository tr = new ElectionRepository(entMgr);
		if(!tr.timestampExists()) {
			entMgr.persist(t0);
		}
		
		entMgr.getTransaction().commit();
		entMgr.getEntityManagerFactory().close();
	}
}