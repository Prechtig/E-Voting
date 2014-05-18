package org.evoting.testing;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.common.utility.Importer;
import org.evoting.security.RSA;
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
	private final String mHash = "30c6ff7a44f7035af933babaea771bf177fc38f06482ad06434cbcc04de7ac14";

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
	public void testSHA2() {
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
	public void testAuthentication() {
		byte[] bytes = {123, -12, 88, 41};
		Security.generateRSAKeys();
		byte[] signature = Security.sign(bytes, Security.getBulletinBoardRSAPrivateKey());
		Assert.assertEquals(true, Security.authenticate(bytes, signature, Security.getBulletinBoardRSAPublicKey()));
	}
}