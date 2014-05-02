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

	//private static BigInteger g512 = new BigInteger("A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507FD6406CFF14266D31266FEA1E5C41564B777E690F5504F213160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28AD662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24855E6EEB22B3B2E5", 16);
	//private static BigInteger p512 = new BigInteger("B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C69A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C013ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD7098488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708DF1FB2BC2E4A4371", 16);
	
	private static ElGamalPublicKeyParameters pubK;
	private static ElGamalPrivateKeyParameters privK;

	public static void generateKeyPair(boolean overwrite) {
		if ((pubK == null && privK == null) || overwrite == true) {
			BigInteger paramBigInteger1 = g512;
			BigInteger paramBigInteger2 = p512;
			int paramInt2 = 0;

			System.out.println("length of keys " + g512.bitLength() + " " + p512.bitLength());
				
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
	
	public static int getSizeOfElgamalCipher() {
		return 128;
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
