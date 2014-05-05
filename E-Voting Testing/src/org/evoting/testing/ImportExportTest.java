package org.evoting.testing;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.evoting.common.Exporter;
import org.evoting.common.Importer;
import org.evoting.security.Security;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ImportExportTest {
	private String bbPubRsaKey = "bbPubRsaKey";
	private String bbPrivRsaKey = "bbPrivRsaKey";

	@Before
	public void generateKeys() {
		Security.generateKeys();
	}
	
	@Test
	public void testPublicExportImport() {
		PublicKey key = Security.getBulletinBoardRSAPublicKey();
		try {
			Exporter.exportRsaKey(bbPubRsaKey, key);
			PublicKey importedKey = Importer.importRsaPublicKey(bbPubRsaKey);
			Assert.assertEquals(key, importedKey);
		} catch (IOException e) {
			System.out.println("Could not find file");
			assert false;
		} catch (InvalidKeySpecException e) {
			System.out.println("Invalid key file");
			assert false;
		}
	}
	
	@Test
	public void testPrivateExportImport() {
		PrivateKey key = Security.getBulletinBoardRSAPrivateKey();
		try {
			Exporter.exportRsaKey(bbPrivRsaKey, key);
			PrivateKey importedKey = Importer.importRsaPrivateKey(bbPrivRsaKey);
			Assert.assertEquals(key, importedKey);
		} catch (IOException e) {
			System.out.println("Could not find file");
			assert false;
		} catch (InvalidKeySpecException e) {
			System.out.println("Invalid key file");
			assert false;
		}
	}
}