package org.evoting.testing;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.authority.Model;
import org.evoting.common.utility.Exporter;
import org.evoting.common.utility.Importer;
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
	
	@Test
	public void testElGamalImportExport() throws IOException {
		ElGamalPrivateKeyParameters privKey = Security.getElgamalPrivateKey();
		ElGamalPublicKeyParameters pubKey = Security.getElgamalPublicKey();
		Exporter.exportElGamalPrivateKeyParameters(privKey, Model.getElGamalPrivateKeyFile());
		Exporter.exportElGamalPublicKeyParameters(pubKey, Model.getElGamalPublicKeyFile());
		ElGamalPrivateKeyParameters importedPrivKey = Importer.importElGamalPrivateKeyParameters(Model.getElGamalPrivateKeyFile());
		ElGamalPublicKeyParameters importedPubKey = Importer.importElGamalPublicKeyParameters(Model.getElGamalPublicKeyFile());
		Assert.assertEquals(privKey, importedPrivKey);
		Assert.assertEquals(pubKey, importedPubKey);
	}
}