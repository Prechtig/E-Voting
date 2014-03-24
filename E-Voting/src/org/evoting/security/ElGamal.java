package org.evoting.security;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.ElGamalEngine;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;

public class ElGamal {
	private BigInteger g512 = new BigInteger("153d5d6172adb43045b68ae8e1de1070b6137005686d29d3d73a7749199681ee5b212c9b96bfdcfa5b20cd5e3fd2044895d609cf9b410b7a0f12ca1cb9a428cc", 16);
	private BigInteger p512 = new BigInteger("9494fec095f3b85ee286542b3836fc81a5dd0a0349b4c239dd38744d488cf8e31db8bcb7d33b41abb9e5a33cca9144b1cef332c94bf0573bf047a3aca98cdf3b", 16);
	
	
	public void encryptDecryptTest() throws SecurityException{
		String m = "Test String";
		byte[] bytem = m.getBytes();
		
		BigInteger paramBigInteger1 = g512;
		BigInteger paramBigInteger2 = p512;
		int paramInt2 = 0;
		
		//Elgamal magic
		ElGamalParameters localElGamalParameters = new ElGamalParameters(paramBigInteger2, paramBigInteger1, paramInt2);
		ElGamalKeyGenerationParameters localElGamalKeyGenerationParameters = new ElGamalKeyGenerationParameters(new SecureRandom(), localElGamalParameters);
		ElGamalKeyPairGenerator localElGamalKeyPairGenerator = new ElGamalKeyPairGenerator();
		localElGamalKeyPairGenerator.init(localElGamalKeyGenerationParameters);
		AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = localElGamalKeyPairGenerator.generateKeyPair();
		
		//Get the two keys
		ElGamalPublicKeyParameters localElGamalPublicKeyParameters = (ElGamalPublicKeyParameters) localAsymmetricCipherKeyPair.getPublic();
		ElGamalPrivateKeyParameters localElGamalPrivateKeyParameters = (ElGamalPrivateKeyParameters) localAsymmetricCipherKeyPair.getPrivate();
		
		//What does this do?
		//checkKeySize(paramInt2, localElGamalPrivateKeyParameters);
		
		//ENCRYPT
		ElGamalEngine engine = new ElGamalEngine();
		engine.init(true, localElGamalPublicKeyParameters);
		byte[] encrypted = engine.processBlock(bytem, 0, bytem.length);
		
		//DECRYPT
		engine.init(false, localElGamalPrivateKeyParameters);
		byte[] decrypted = engine.processBlock(encrypted, 0, encrypted.length);
		String result = new String(decrypted);
		
		//Result check
		if (!result.equals(m)){
	    	throw new SecurityException("ElGamal encryption/decryption exception");
	    }
	}
	
	public static byte[] encrypt(String m, ElGamalPublicKeyParameters pK){
		return encrypt(m.getBytes(), pK);
	}
	
	public static byte[] encrypt(byte[] m, ElGamalPublicKeyParameters pK){
		//ENCRYPT
		ElGamalEngine engine = new ElGamalEngine();
		engine.init(true, pK);
		byte[] result = engine.processBlock(m, 0, m.length);
		
		return result;
	}
	
	public static byte[] decrypt(String m, ElGamalPrivateKeyParameters pK){
		return decrypt(m.getBytes(), pK);
	}
	
	public static byte[] decrypt(byte[] m, ElGamalPrivateKeyParameters pK){
		//DECRYPT
		ElGamalEngine engine = new ElGamalEngine();
		engine.init(false, pK);
		byte[] result = engine.processBlock(m, 0, m.length);
		
		return result;
	}
}
