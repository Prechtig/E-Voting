package org.evoting.testing;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.common.utility.Exporter;
import org.evoting.common.utility.Importer;
import org.evoting.security.RSA;
import org.evoting.security.SHA1;
import org.evoting.security.Security;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SecurityTest {
	private static ElGamalPrivateKeyParameters ElGamalPrivateKey;
	private static ElGamalPublicKeyParameters ElGamalPublicKey;

	private static PrivateKey RSAPrivateKey;
	private static PublicKey RSAPublicKey;

	private static String ElGamalPublicKeyFile = "ElGamalPub";
	private static String ElGamalPrivateKeyFile = "ElGamalPriv";
	
	private final String m = "Test String";
	private final String mHash = "a5103f9c0b7d5ff69ddc38607c74e53d4ac120f2";

	@BeforeClass
	public static void setup() throws FileNotFoundException, CorruptDataException, IOException {
		Security.setElGamalPublicKey(Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile));
		Security.setElGamalPrivateKey(Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile));
		ElGamalPublicKey = Security.getElgamalPublicKey();
		ElGamalPrivateKey = Security.getElgamalPrivateKey();

		RSA.generateAuthKeyPair(false);
		RSAPrivateKey = RSA.getAuthorityPrivateKey();
		RSAPublicKey = RSA.getAuthorityPublicKey();
	}

	@Test
	public void testElGamal() {
		long one = 1l;
		byte[] encrypted = Security.encryptExponentialElgamal(one, ElGamalPublicKey);
		long decrypted = Security.decryptExponentialElgamal(encrypted, ElGamalPrivateKey);

		assertEquals(one, decrypted);
	}

	@Test
	public void testRSA() {
		byte[] encrypted = Security.encryptRSA(m, RSAPrivateKey);
		byte[] decrypted = Security.decryptRSA(encrypted, RSAPublicKey);

		String result = new String(decrypted);

		assertEquals(m, result);
	}

	@Test
	public void testSHA1() {
		String hash = Security.hash(m);

		assertEquals(hash, mHash);
	}

	@Test
	public void testSign() {
		byte[] signed = Security.sign(m, RSAPrivateKey);
		byte[] hash = Security.decryptRSA(signed, RSAPublicKey);

		String result = new String(hash);
		assertEquals(mHash, result);
	}

	@Test
	public void saveElgamalKeyPublic() {
		ElGamalPublicKeyParameters savedParams = null;
		try {
			Exporter.exportElGamalPublicKeyParameters(ElGamalPublicKey, ElGamalPublicKeyFile);

			savedParams = Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
		} catch (CorruptDataException | IOException e) {
			Assert.fail();
		}
		Assert.assertEquals(savedParams, ElGamalPublicKey);
	}

	@Test
	public void saveElgamalKeyPrivate() {
		ElGamalPrivateKeyParameters savedParams = null;
		try {
			Exporter.exportElGamalPrivateKeyParameters(ElGamalPrivateKey, ElGamalPrivateKeyFile);

			savedParams = Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile);
		} catch (CorruptDataException | IOException e) {
			Assert.fail();
		}
		Assert.assertEquals(savedParams, ElGamalPrivateKey);
	}
	
	@Test
	public void testHash() {
		String result = "";
		byte[] hashed = null;
		
		MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("SHA-1");
	        
	        hashed = md.digest(m.getBytes());
	    }
	    catch(NoSuchAlgorithmException e) {
	    	Assert.fail();
	    } 
	    result = SHA1.byteToHex(hashed);
	    Assert.assertEquals(mHash, result);
	}
	
	@Test
	public void testAuthentication() {
		byte[] bytes = {123, -12, 88, 41};
		Security.generateRSAKeys();
		byte[] signature = Security.sign(bytes, Security.getBulletinBoardRSAPrivateKey());
		Assert.assertEquals(true, Security.authenticate(bytes, signature, Security.getBulletinBoardRSAPublicKey()));
	}
}