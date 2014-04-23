package org.evoting.security;

import static org.junit.Assert.*;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.common.Converter;
import org.evoting.common.Exporter;
import org.evoting.common.Importer;
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
	public static void setup(){		
		ElGamal.generateKeyPair(true);
		ElGamalPrivateKey = ElGamal.getPrivateKey();
		ElGamalPublicKey = ElGamal.getPublicKey();
		
		RSA.generateKeyPair(true);
		RSAPrivateKey = RSA.getPrivateKey();
		RSAPublicKey = RSA.getPublicKey();
		
	}

	@Test
	public void testElGamalString() {
		String m = "Test String";

		byte[] encrypted = Security.encryptElGamal(m, ElGamalPublicKey);
		byte[] decrypted = Security.decryptElgamal(encrypted, ElGamalPrivateKey);

		String result = new String(decrypted);

		if (!m.equals(result)) {
			fail("ElGamal encryption and decryption failed");
		}
	}
	
	
	@Test
	public void testElGamalInt() {
		int m = 1;
		
		byte[] n = Converter.toByteArray(m);

		byte[] encrypted = Security.encryptElGamal(n, ElGamalPublicKey);
		byte[] decrypted = Security.decryptElgamal(encrypted, ElGamalPrivateKey);

		int i = Converter.toInt(decrypted);
		
		if (m != i) {
			fail("ElGamal encryption and decryption failed");
		}
	}
	
	@Test
	public void testRSA() {
		String m = "Test String";

		byte[] encrypted = Security.encryptRSA(m, RSAPrivateKey);
		byte[] decrypted = Security.decryptRSA(encrypted, RSAPublicKey);

		String result = new String(decrypted);

		if (!m.equals(result)) {
			fail("RSA encryption and decryption failed");
		}
	}

	@Test
	public void testSHA1() {
		String m = "Test String";
		String trueHash = "a5103f9c0b7d5ff69ddc38607c74e53d4ac120f2";

		String hash = Security.hash(m);
		if (!hash.equals(trueHash)) {
			fail("SHA1 hashing failed");
		}
	}
	
	@Test
	public void testSign(){
		String m = "Test String";
		String trueHash = "a5103f9c0b7d5ff69ddc38607c74e53d4ac120f2";
		byte[] signed = Security.sign(m, RSAPrivateKey);
		byte[] hash = Security.decryptRSA(signed, RSAPublicKey);
		
		String result = new String(hash);
		assertEquals(trueHash, result);
	}
	
	@Test
	public void saveElgamalKeyPublic(){
		Exporter.exportElGamalPublicKeyParameters(ElGamalPublicKey, ElGamalPublicKeyFile);
		ElGamalPublicKeyParameters savedParams =  Importer.importElGamalPublicKeyParameters(ElGamalPublicKeyFile);
		assert(savedParams.equals(ElGamalPublicKey));
	}
	
	@Test
	public void saveElgamalKeyPrivate(){
		Exporter.exportElGamalPrivateKeyParameters(ElGamalPrivateKey, ElGamalPrivateKeyFile);
		ElGamalPrivateKeyParameters savedParams = Importer.importElGamalPrivateKeyParameters(ElGamalPrivateKeyFile);
		assert(savedParams.equals(ElGamalPrivateKey));
	}
	
	@Test
	public void testIntegerElGamal(){
		
	}
}
