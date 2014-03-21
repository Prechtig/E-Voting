package org.evoting.security;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.ElGamalEngine;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;

public class ElGamalHandler {
	private BigInteger g512 = new BigInteger("153d5d6172adb43045b68ae8e1de1070b6137005686d29d3d73a7749199681ee5b212c9b96bfdcfa5b20cd5e3fd2044895d609cf9b410b7a0f12ca1cb9a428cc", 16);
	private BigInteger p512 = new BigInteger("9494fec095f3b85ee286542b3836fc81a5dd0a0349b4c239dd38744d488cf8e31db8bcb7d33b41abb9e5a33cca9144b1cef332c94bf0573bf047a3aca98cdf3b", 16);
	
	
	public String elgamalEncrypt(String m){
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
		ElGamalPublicKeyParameters localElGamalPublicKeyParameters = (ElGamalPublicKeyParameters) localAsymmetricCipherKeyPair.getPublic();
		ElGamalPrivateKeyParameters localElGamalPrivateKeyParameters = (ElGamalPrivateKeyParameters) localAsymmetricCipherKeyPair.getPrivate();
		//checkKeySize(paramInt2, localElGamalPrivateKeyParameters); //What does check key size do?
		
		ElGamalEngine engine = new ElGamalEngine();
		engine.init(true, localElGamalPublicKeyParameters);
		byte[] encrypted = engine.processBlock(bytem, 0, bytem.length);
		byte[] encrypted4 = engine.processBlock(bytem, 0, bytem.length);
		engine.init(true, localElGamalPublicKeyParameters);
		byte[] encrypted2 = engine.processBlock(bytem, 0, bytem.length);
		engine.init(true, localElGamalPublicKeyParameters);
		byte[] encrypted3 = engine.processBlock(bytem, 0, bytem.length);
		
		
		//String a = bytes2String(encrypted);
		
		//byte[] b = a.getBytes();
		
		
		
		engine.init(false, localElGamalPrivateKeyParameters);
		byte[] decrypted = engine.processBlock(encrypted, 0, encrypted.length);
		
		String result = new String(decrypted);
		
		//testGeneration(258);
		
		
		
		return result;
	}
	
	
	
	   private static String bytes2String(byte[] bytes) {
		    StringBuilder string = new StringBuilder();
		    for (byte b : bytes) {
		        String hexString = Integer.toHexString(0x00FF & b);
		        string.append(hexString.length() == 1 ? "0" + hexString : hexString);
		    }
		    return string.toString();
		}
	
	
	
	
	
	
	
	
	
	private void testGeneration(int paramInt) {
		ElGamalParametersGenerator localElGamalParametersGenerator = new ElGamalParametersGenerator();
		localElGamalParametersGenerator.init(paramInt, 10, new SecureRandom());
		ElGamalParameters localElGamalParameters = localElGamalParametersGenerator.generateParameters();
		if (localElGamalParameters.getL() != 0) {
			System.out.println("Fail 1");//fail("ElGamalParametersGenerator failed to set L to 0 in generated ElGamalParameters");
		}
		ElGamalKeyGenerationParameters localElGamalKeyGenerationParameters = new ElGamalKeyGenerationParameters(new SecureRandom(), localElGamalParameters);
		ElGamalKeyPairGenerator localElGamalKeyPairGenerator = new ElGamalKeyPairGenerator();
		localElGamalKeyPairGenerator.init(localElGamalKeyGenerationParameters);
		AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = localElGamalKeyPairGenerator.generateKeyPair();
		ElGamalPublicKeyParameters localElGamalPublicKeyParameters = (ElGamalPublicKeyParameters) localAsymmetricCipherKeyPair.getPublic();
		ElGamalPrivateKeyParameters localElGamalPrivateKeyParameters = (ElGamalPrivateKeyParameters) localAsymmetricCipherKeyPair.getPrivate();
		ElGamalEngine localElGamalEngine = new ElGamalEngine();
		localElGamalEngine.init(true, new ParametersWithRandom(localElGamalPublicKeyParameters, new SecureRandom()));
		byte[] arrayOfByte1 = Hex.decode("5468697320697320612074657374");
		byte[] arrayOfByte2 = arrayOfByte1;
		byte[] arrayOfByte3 = localElGamalEngine.processBlock(arrayOfByte2, 0, arrayOfByte2.length);
		localElGamalEngine.init(false, localElGamalPrivateKeyParameters);
		arrayOfByte2 = localElGamalEngine.processBlock(arrayOfByte3, 0, arrayOfByte3.length);
		if (!Arrays.areEqual(arrayOfByte1, arrayOfByte2)) {
			System.out.println("Fail 2");//fail("generation test failed");
		}
		
		localElGamalEngine.init(false, localElGamalPublicKeyParameters);
		localElGamalEngine.init(true, localElGamalPrivateKeyParameters);
		arrayOfByte2 = localElGamalEngine.processBlock(arrayOfByte3, 0, arrayOfByte3.length);
		
		
		
		System.out.println();
		
	}
}
