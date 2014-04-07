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
	private static BigInteger g512 = new BigInteger("153d5d6172adb43045b68ae8e1de1070b6137005686d29d3d73a7749199681ee5b212c9b96bfdcfa5b20cd5e3fd2044895d609cf9b410b7a0f12ca1cb9a428cc", 16);
	private static BigInteger p512 = new BigInteger("9494fec095f3b85ee286542b3836fc81a5dd0a0349b4c239dd38744d488cf8e31db8bcb7d33b41abb9e5a33cca9144b1cef332c94bf0573bf047a3aca98cdf3b", 16);

	private static ElGamalPublicKeyParameters pubK;
	private static ElGamalPrivateKeyParameters privK;

	public static void generateKeyPair(boolean overwrite) {
		if ((pubK == null && privK == null) || overwrite == true) {
			BigInteger paramBigInteger1 = g512;
			BigInteger paramBigInteger2 = p512;
			int paramInt2 = 0;

			// Elgamal magic
			ElGamalParameters localElGamalParameters = new ElGamalParameters(paramBigInteger2, paramBigInteger1, paramInt2);
			ElGamalKeyGenerationParameters localElGamalKeyGenerationParameters = new ElGamalKeyGenerationParameters(new SecureRandom(), localElGamalParameters);
			ElGamalKeyPairGenerator localElGamalKeyPairGenerator = new ElGamalKeyPairGenerator();
			localElGamalKeyPairGenerator.init(localElGamalKeyGenerationParameters);
			AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = localElGamalKeyPairGenerator.generateKeyPair();

			// Get the two keys
			ElGamalPublicKeyParameters localElGamalPublicKeyParameters = (ElGamalPublicKeyParameters) localAsymmetricCipherKeyPair.getPublic();
			ElGamalPrivateKeyParameters localElGamalPrivateKeyParameters = (ElGamalPrivateKeyParameters) localAsymmetricCipherKeyPair.getPrivate();

			pubK = localElGamalPublicKeyParameters;
			privK = localElGamalPrivateKeyParameters;
		}
	}

	public static ElGamalPublicKeyParameters getPublicKey() {
		return pubK;
	}

	public static ElGamalPrivateKeyParameters getPrivateKey() {
		return privK;
	}

	public static byte[] encrypt(String m, ElGamalPublicKeyParameters pK) {
		return encrypt(m.getBytes(), pK);
	}

	public static byte[] encrypt(byte[] m, ElGamalPublicKeyParameters pK) {
		// ENCRYPT
		ElGamalEngine engine = new ElGamalEngine();
		engine.init(true, pK);
		byte[] result = engine.processBlock(m, 0, m.length);

		return result;
	}

	public static byte[] decrypt(String m, ElGamalPrivateKeyParameters pK) {
		return decrypt(m.getBytes(), pK);
	}

	public static byte[] decrypt(byte[] m, ElGamalPrivateKeyParameters pK) {
		// DECRYPT
		ElGamalEngine engine = new ElGamalEngine();
		engine.init(false, pK);
		byte[] result = engine.processBlock(m, 0, m.length);

		return result;
	}

	public static void setPublicKey(ElGamalPublicKeyParameters pubK2) {
		// TODO Auto-generated method stub
		pubK = pubK2;
	}

	public static void setPrivateKey(ElGamalPrivateKeyParameters privK2) {
		// TODO Auto-generated method stub
		privK = privK2;
	}

	public static byte[] encrypt(int m, ElGamalPublicKeyParameters pK) {
		// TODO Auto-generated method stub
		return null;
	}
}
