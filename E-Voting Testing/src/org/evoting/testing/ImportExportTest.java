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
	private String bbRsaPub = "BbRsaPub";
	private String bbRsaPriv = "BbRsaPriv";
	private String authRsaPub = "AuthRsaPub";
	private String authRsaPriv = "AuthRsaPriv";

	@Before
	public void generateKeys() {
		Security.generateElGamalKeys(true);
		Security.generateRSAKeys();
	}
	
	@Test
	public void testPublicExportImport() {
		//Get keys
		PublicKey aKey = Security.getAuthorityRSAPublicKey();
		PublicKey bKey = Security.getBulletinBoardRSAPublicKey();
		try {
			//Export keys
			Exporter.exportRsaKey(authRsaPub, aKey);
			Exporter.exportRsaKey(bbRsaPub, bKey);
			//Import keys
			PublicKey importedAKey = Importer.importRsaPublicKey(authRsaPub);
			PublicKey importedBKey = Importer.importRsaPublicKey(bbRsaPub);
			//Assert they are equal
			Assert.assertEquals(aKey, importedAKey);
			Assert.assertEquals(bKey, importedBKey);
		} catch (IOException e) {
			Assert.fail("Could not find file");
		} catch (InvalidKeySpecException e) {
			Assert.fail("Invalid key file");
		}
	}
	
	@Test
	public void testPrivateExportImport() {
		//Get keys
		PrivateKey aKey = Security.getAuthorityRSAPrivateKey();
		PrivateKey bKey = Security.getBulletinBoardRSAPrivateKey();
		try {
			//Export keys
			Exporter.exportRsaKey(authRsaPriv, aKey);
			Exporter.exportRsaKey(bbRsaPriv, bKey);
			//Import keys
			PrivateKey importedAKey = Importer.importRsaPrivateKey(authRsaPriv);
			PrivateKey importedBKey = Importer.importRsaPrivateKey(bbRsaPriv);
			//Assert they are equal
			Assert.assertEquals(aKey, importedAKey);
			Assert.assertEquals(bKey, importedBKey);
		} catch (IOException e) {
			System.out.println("Could not find file");
			Assert.fail("Could not find file");
		} catch (InvalidKeySpecException e) {
			Assert.fail("Invalid key file");
		}
	}
	
	@Test
	public void testElGamalImportExport() throws IOException {
		//Get keys
		ElGamalPrivateKeyParameters privKey = Security.getElgamalPrivateKey();
		ElGamalPublicKeyParameters pubKey = Security.getElgamalPublicKey();
		//Export keys
		Exporter.exportElGamalPrivateKeyParameters(privKey, Model.getElGamalPrivateKeyFile());
		Exporter.exportElGamalPublicKeyParameters(pubKey, Model.getElGamalPublicKeyFile());
		//Import keys
		ElGamalPrivateKeyParameters importedPrivKey = Importer.importElGamalPrivateKeyParameters(Model.getElGamalPrivateKeyFile());
		ElGamalPublicKeyParameters importedPubKey = Importer.importElGamalPublicKeyParameters(Model.getElGamalPublicKeyFile());
		//Assert they are equal
		Assert.assertEquals(privKey, importedPrivKey);
		Assert.assertEquals(pubKey, importedPubKey);
	}
}