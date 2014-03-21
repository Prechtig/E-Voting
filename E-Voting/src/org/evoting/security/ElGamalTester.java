package org.evoting.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;



public class ElGamalTester {
	private static BigInteger g512 = new BigInteger("153d5d6172adb43045b68ae8e1de1070b6137005686d29d3d73a7749199681ee5b212c9b96bfdcfa5b20cd5e3fd2044895d609cf9b410b7a0f12ca1cb9a428cc", 16);
	private static BigInteger p512 = new BigInteger("9494fec095f3b85ee286542b3836fc81a5dd0a0349b4c239dd38744d488cf8e31db8bcb7d33b41abb9e5a33cca9144b1cef332c94bf0573bf047a3aca98cdf3b", 16);
	
	
	public static void main(String[] paramArrayOfString) {
		/*ElGamalHandler egh = new ElGamalHandler();
		System.out.println(egh.elgamalEncrypt("Hejsa"));*/
		//egh.enrypt("Hejsa");
		
		/*ElGamalHandler eh = new ElGamalHandler();
		eh.elgamalEncrypt("hejsa");*/
		
		//
		BigInteger paramBigInteger1 = g512;
		BigInteger paramBigInteger2 = p512;
		int paramInt2 = 0;
		
		
		//Elgamal magic
		ElGamalParameters localElGamalParameters = new ElGamalParameters(paramBigInteger2, paramBigInteger1, paramInt2);
		ElGamalKeyGenerationParameters localElGamalKeyGenerationParameters = new ElGamalKeyGenerationParameters(new SecureRandom(), localElGamalParameters);
		ElGamalKeyPairGenerator localElGamalKeyPairGenerator = new ElGamalKeyPairGenerator();
		localElGamalKeyPairGenerator.init(localElGamalKeyGenerationParameters);
		AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = localElGamalKeyPairGenerator.generateKeyPair();
		ElGamalPublicKeyParameters localElGamalPublicKeyParameters = (ElGamalPublicKeyParameters) localAsymmetricCipherKeyPair.getPublic();
		ElGamalPrivateKeyParameters localElGamalPrivateKeyParameters = (ElGamalPrivateKeyParameters) localAsymmetricCipherKeyPair.getPrivate();
		//
		
		Security s = new Security();
		s.encryptElGamalToBB("username", "password", localElGamalPublicKeyParameters);
		
		
		/*RSA rsa = new RSA(20);
		
		try {
			rsa.rsaTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		*/
		
		RSATest rsa = new RSATest();
		rsa.generateKeys();
		String encrypted = rsa.encrypt("Hejsa");
		try {
			byte[] bytes = encrypted.getBytes("UTF-8");
		String decrypted = rsa.decrypt(encrypted.getBytes("UTF-8"));

		String a = new String(encrypted);
		String b = rsa.decrypt(a.getBytes());
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
