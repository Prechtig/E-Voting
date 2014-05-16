package org.evoting.testing;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jolie.runtime.Value;
import jolie.runtime.ValueVector;

import org.evoting.authority.Model;
import org.evoting.bulletinboard.Controller;
import org.evoting.common.Importer;
import org.evoting.common.jolie.ValueIdentifiers;
import org.evoting.security.Security;
import org.junit.BeforeClass;
import org.junit.Test;

public class BulletinBoardTest {

	@BeforeClass
	public static void beforeClass() throws IOException {
		try {
			PublicKey authRsaPub = Importer.importRsaPublicKey("..//E-Voting Jolie//Authority//AuthRsaPub");
			Security.setAuthorityRSAPublicKey(authRsaPub);
			PrivateKey AuthRsaPriv = Importer.importRsaPrivateKey("..//E-Voting Jolie//Authority//AuthRsaPriv");
			Security.setAuthorityRSAPrivateKey(AuthRsaPriv);
		} catch (IOException e) {
			System.out.println("Could not find file");
			assert false;
		} catch (InvalidKeySpecException e) {
			System.out.println("Invalid key file");
			assert false;
		}
	}
	
	@Test
	public void testStartElection() throws ParseException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2014-04-30 20:00");
		Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2014-05-30 20:00");

		Value result = Value.create();
		result.getNewChild(ValueIdentifiers.getStartTime()).setValue(startDate.getTime());
		result.getNewChild(ValueIdentifiers.getEndTime()).setValue(endDate.getTime());

		Value validator = Model.getNewValidator();
		System.out.println("Key = " + Security.getAuthorityRSAPublicKey().toString());
		ValueVector children = result.getChildren(ValueIdentifiers.getValidator());
		children.set(0, validator);

		Controller c = new Controller();
		c.startElection(result);
	}

}
