package org.evoting.security;

import static org.junit.Assert.*;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.junit.BeforeClass;
import org.junit.Test;

public class SecurityTest {
	private static ISecurity s;
	
	private static ElGamalPrivateKeyParameters ElGamalPrivateKey;
	private static ElGamalPublicKeyParameters ElGamalPublicKey;
	
	private static PrivateKey RSAPrivateKey;
	private static PublicKey RSAPublicKey;
	
	@BeforeClass
	public static void setup(){
		s = new Security();
		
		ElGamal.generateKeyPair(true);
		ElGamalPrivateKey = ElGamal.getPrivateKey();
		ElGamalPublicKey = ElGamal.getPublicKey();
		
		RSA.generateKeyPair(true);
		RSAPrivateKey = RSA.getPrivateKey();
		RSAPublicKey = RSA.getPublicKey();
	}

	@Test
	public void testElGamal() {
		String m = "Test String";

		byte[] encrypted = s.encryptElGamal(m, ElGamalPublicKey);
		byte[] decrypted = s.decryptElgamal(encrypted, ElGamalPrivateKey);

		String result = new String(decrypted);

		if (!m.equals(result)) {
			fail("ElGamal encryption and decryption failed");
		}
	}

	@Test
	public void testRSA() {
		String m = "Test String";

		byte[] encrypted = s.encryptRSA(m, RSAPrivateKey);
		byte[] decrypted = s.decryptRSA(encrypted, RSAPublicKey);

		String result = new String(decrypted);

		if (!m.equals(result)) {
			fail("RSA encryption and decryption failed");
		}
	}

	@Test
	public void testSHA1() {
		String m = "Test String";
		String trueHash = "a5103f9c0b7d5ff69ddc38607c74e53d4ac120f2";

		Security s = new Security();
		String hash = s.hash(m);
		if (!hash.equals(trueHash)) {
			fail("SHA1 hashing failed");
		}
	}
}
