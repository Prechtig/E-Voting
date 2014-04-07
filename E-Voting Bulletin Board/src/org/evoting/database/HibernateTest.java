package org.evoting.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.Converter;
import org.evoting.database.entities.ElectionOption;
import org.evoting.database.entities.Timestamp;
import org.evoting.database.entities.Vote;
import org.evoting.database.repositories.ElectionOptionRepository;
import org.evoting.database.repositories.TimestampRepository;
import org.evoting.database.repositories.VoteRepository;
import org.evoting.security.Security;

public class HibernateTest {
	public static void main(String[] args) throws ParseException {
		//Generate the keys used to encrypt/decrypt
		if(!Security.RSAKeysGenerated()) {
			Security.generateRSAKeys();
		}
		if(!Security.ElGamalKeysGenerated()) {
			Security.generateElGamalKeys();
		}
		
		ElGamalPublicKeyParameters pubKey = Security.getElgamalPublicKey();
		
		Vote v1 = new Vote(0, new byte[][] { Security.encryptElGamal(Converter.toByteArray(1), pubKey), Security.encryptElGamal(Converter.toByteArray(0), pubKey), Security.encryptElGamal(Converter.toByteArray(0), pubKey) });
		Vote v2 = new Vote(1, new byte[][] { Security.encryptElGamal(Converter.toByteArray(0), pubKey), Security.encryptElGamal(Converter.toByteArray(1), pubKey), Security.encryptElGamal(Converter.toByteArray(0), pubKey) });
		ElectionOption c0 = new ElectionOption(0, "Mikkel Hvilshøj Funch");
		ElectionOption c1 = new ElectionOption(1, "Mark Thorhauge");
		ElectionOption c2 = new ElectionOption(2, "Andreas Precht Poulsen");
		ElectionOption c3 = new ElectionOption(3, "Gregers Jensen");
		Date date = new SimpleDateFormat("yyyy-MM-dd kk").parse("2014-06-30 20");

		Timestamp t0 = new Timestamp(0, Security.encryptRSA(date.toString(), Security.getRSAPrivateKey()));

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
		if (cr.findById(c0.getId()) == null) {
			entMgr.persist(c0);
		}
		if (cr.findById(c1.getId()) == null) {
			entMgr.persist(c1);
		}
		if (cr.findById(c2.getId()) == null) {
			entMgr.persist(c2);
		}
		if (cr.findById(c3.getId()) == null) {
			entMgr.persist(c3);
		}
		
		TimestampRepository tr = new TimestampRepository(entMgr);
		if(!tr.timestampExists()) {
			entMgr.persist(t0);
		}
		
		entMgr.getTransaction().commit();
		entMgr.getEntityManagerFactory().close();
	}
}