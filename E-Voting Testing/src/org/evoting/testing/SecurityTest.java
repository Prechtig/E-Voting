package org.evoting.testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.common.utility.Converter;
import org.evoting.common.utility.Exporter;
import org.evoting.common.utility.Importer;
import org.evoting.security.ElGamal;
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

	private static String ElGamalPublicKeyFile = "ElGamalPublicKey";
	private static String ElGamalPrivateKeyFile = "ElGamalPrivateKey";

	@BeforeClass
	public static void setup() {
		ElGamal.generateKeyPair(false);
		ElGamalPrivateKey = ElGamal.getPrivateKey();
		ElGamalPublicKey = ElGamal.getPublicKey();

		RSA.generateAuthKeyPair(false);
		RSAPrivateKey = RSA.getAuthorityPrivateKey();
		RSAPublicKey = RSA.getAuthorityPublicKey();
	}

	@Test
	public void testElGamalString() {
		String m = "Test String";

		byte[] encrypted = Security.encryptElGamal(m, ElGamalPublicKey);
		byte[] decrypted = Security.decryptElgamal(encrypted, ElGamalPrivateKey);

		String result = new String(decrypted);

		assertEquals(m, result);
	}

	@Test
	public void testElGamalInt() {
		int m = 1;

		byte[] n = Converter.toByteArray(m);

		byte[] encrypted = Security.encryptElGamal(n, ElGamalPublicKey);
		byte[] decrypted = Security.decryptElgamal(encrypted, ElGamalPrivateKey);

		int i = Converter.toInt(decrypted);

		assertEquals(m, i);
	}

	@Test
	public void testRSA() {
		String m = "Test String";

		byte[] encrypted = Security.encryptRSA(m, RSAPrivateKey);
		byte[] decrypted = Security.decryptRSA(encrypted, RSAPublicKey);

		String result = new String(decrypted);

		assertEquals(m, result);
	}

	@Test
	public void testSHA1() {
		String m = "Test String";
		String trueHash = "a5103f9c0b7d5ff69ddc38607c74e53d4ac120f2";

		String hash = Security.hash(m);

		assertEquals(hash, trueHash);
	}

	@Test
	public void testSign() {
		String m = "Test String";
		String trueHash = "a5103f9c0b7d5ff69ddc38607c74e53d4ac120f2";
		byte[] signed = Security.sign(m, RSAPrivateKey);
		byte[] hash = Security.decryptRSA(signed, RSAPublicKey);

		String result = new String(hash);
		assertEquals(trueHash, result);
	}

	@Test
	public void saveElgamalKeyPublic() {
		ElGamalPublicKeyParameters savedParams = null;
		try {
			Exporter.exportElGamalPublicKeyParameters(ElGamalPublicKey, ElGamalPublicKeyFile);

			savedParams = Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
		} catch (CorruptDataException | IOException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
		Assert.assertEquals(savedParams, ElGamalPrivateKey);
	}
	
	@Test
	public void testHash() {
		String m = "Test String";
		String trueResult = "a5103f9c0b7d5ff69ddc38607c74e53d4ac120f2";
		String result = "";
		byte[] hashed = null;
		
		MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("SHA-1");
	        
	        hashed = md.digest(m.getBytes());
	    }
	    catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } 
	    result = SHA1.byteToHex(hashed);
	    Assert.assertEquals(trueResult, result);
	}
	
	@Test
	public void testAuthentication() {
		byte[] bytes = {123, -12, 88, 41};
		Security.generateRSAKeys();
		byte[] signature = Security.sign(bytes, Security.getBulletinBoardRSAPrivateKey());
		Assert.assertEquals(true, Security.authenticate(bytes, signature, Security.getBulletinBoardRSAPublicKey()));
	}
}