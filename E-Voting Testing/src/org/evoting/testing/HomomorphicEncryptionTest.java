package org.evoting.testing;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.util.Arrays;
import org.evoting.authority.Model;
import org.evoting.common.Group;
import org.evoting.common.Importer;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.security.ElGamal;
import org.evoting.security.Security;
import org.junit.Assert;
import org.junit.Test;

public class HomomorphicEncryptionTest 
{
	private final static BigInteger p = new BigInteger("7781871412403125272081994420237498498848517960190636813994593624893166160780445513887821412313009361625936687452586174696909303141691170346568788390764347");
	private final static BigInteger g = new BigInteger("1112413672743230891881273653171640834687833299890125651932066327486193358080471215100797237775771467149894260123512660136402158406998369481252844452718796");
	private final static int l = 0;
	
	@Test
	public void testRaiseAndDiscreteLog()
	{
		Group.getInstance().setGenerator(g);
		Group.getInstance().setModulo(p);
		
		long message = 1336;
		BigInteger messageAsPower = Group.getInstance().raiseGenerator(message);
		long log = Group.getInstance().discreteLogarithm(messageAsPower);
		assertEquals(message, log);
		
		long message2 = 8521;
		BigInteger message2AsPower = Group.getInstance().raiseGenerator(message2);
		BigInteger messagesProduct = messageAsPower.multiply(message2AsPower);
		messagesProduct = messagesProduct.mod(Group.getInstance().getModulo());
		
		log = Group.getInstance().discreteLogarithm(message2AsPower);
		assertEquals(message2, log);
		
		log = Group.getInstance().discreteLogarithm(messagesProduct);
		assertEquals(message + message2, log);
	}
	
	@Test
	public void testRaiseAndDiscreteLogWithCache()
	{
		Group.getInstance().setGenerator(g);
		Group.getInstance().setModulo(p);
		Group.getInstance().buildCache(20000);
		
		long message = 1336;
		BigInteger messageAsPower = Group.getInstance().raiseGenerator(message);
		long log = Group.getInstance().discreteLogarithm(messageAsPower);
		assertEquals(message, log);
		
		long message2 = 8521;
		BigInteger message2AsPower = Group.getInstance().raiseGenerator(message2);
		BigInteger messagesProduct = messageAsPower.multiply(message2AsPower);
		messagesProduct = messagesProduct.mod(Group.getInstance().getModulo());
		
		log = Group.getInstance().discreteLogarithm(message2AsPower);
		assertEquals(message2, log);
		
		log = Group.getInstance().discreteLogarithm(messagesProduct);
		assertEquals(message + message2, log);
	}
	
	@Test
	public void testRaiseAndDiscreteLogFromExpectedRange()
	{
		Group.getInstance().setGenerator(g);
		Group.getInstance().setModulo(p);
		
		long message = 1336;
		BigInteger messageAsPower = Group.getInstance().raiseGenerator(message);
		long log = Group.getInstance().discreteLogarithmFromExpectedRange(messageAsPower, message, 10);
		assertEquals(message, log);	
	}
	
	@Test
	public void testEncryptionAndDecryption()
	{
		ElGamalPrivateKeyParameters ElGamalPrivateKey;
		ElGamalPublicKeyParameters ElGamalPublicKey;
		ElGamal.generateKeyPair(true);
		ElGamalPrivateKey = ElGamal.getPrivateKey();
		ElGamalPublicKey = ElGamal.getPublicKey();
		
		BigInteger message1 = new BigInteger("900");
		byte[] messageByte1 = message1.toByteArray();
		byte[] cipher1 = Security.encryptElGamal(messageByte1, ElGamalPublicKey);
		
        byte[] messageByte = Security.decryptElgamal(cipher1, ElGamalPrivateKey);
        BigInteger result = new BigInteger(messageByte);
        
        assertEquals(message1, result);
	}
	
	@Test
	public void testEncryptionAndDecryptionWithImporter()
	{
		try {
			ElGamalPublicKeyParameters pubKey = Importer.importElGamalPublicKeyParameters(Model.getElGamalPublicKeyFile());
			ElGamalPrivateKeyParameters privKey = Importer.importElGamalPrivateKeyParameters(Model.getElGamalPrivateKeyFile());
			Security.setElGamalPrivateKey(privKey);
			Security.setElGamalPublicKey(pubKey);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BigInteger message1 = new BigInteger("900");
		byte[] messageByte1 = message1.toByteArray();
		byte[] cipher1 = Security.encryptElGamal(messageByte1, Security.getElgamalPublicKey());
		
        byte[] messageByte = Security.decryptElgamal(cipher1, Security.getElgamalPrivateKey());
        BigInteger result = new BigInteger(messageByte);
        
        assertEquals(message1, result);
	}
	
	@Test
	public void testLorena()//TODO:Name change?
	{
		ElGamalParameters localElGamalParameters = new ElGamalParameters(p, g, 0);
		ElGamalKeyGenerationParameters localElGamalKeyGenerationParameters = new ElGamalKeyGenerationParameters(new SecureRandom(), localElGamalParameters);
		ElGamalKeyPairGenerator localElGamalKeyPairGenerator = new ElGamalKeyPairGenerator();
		localElGamalKeyPairGenerator.init(localElGamalKeyGenerationParameters);
		AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = localElGamalKeyPairGenerator.generateKeyPair();

		// Get the two keys
		ElGamalPublicKeyParameters localElGamalPublicKeyParameters = (ElGamalPublicKeyParameters) localAsymmetricCipherKeyPair.getPublic();
		ElGamalPrivateKeyParameters localElGamalPrivateKeyParameters = (ElGamalPrivateKeyParameters) localAsymmetricCipherKeyPair.getPrivate();
		
		ElGamalPublicKeyParameters pubK = localElGamalPublicKeyParameters;
		ElGamalPrivateKeyParameters privK = localElGamalPrivateKeyParameters;
		
		Group.getInstance().setGenerator(g);
		Group.getInstance().setModulo(p);
		Security.setElGamalPublicKey(pubK.getY(), p, g, l);
		//ElGamalParameters elGamalp = new ElGamalParameters(p, g);
		//ElGamalPrivateKeyParameters epkp = new ElGamalPrivateKeyParameters(x, elGamalp);
		Security.setElGamalPrivateKey(privK);
		
		BigInteger message1 = new BigInteger("900");
		byte[] messageByte1 = message1.toByteArray();
		byte[] cipher1 = Security.encryptElGamal(messageByte1, Security.getElgamalPublicKey());
		
		BigInteger message2 = new BigInteger("100");
		byte[] messageByte2 = message2.toByteArray();
		byte[] cipher2 = Security.encryptElGamal(messageByte2, Security.getElgamalPublicKey());
		
		byte[] cipherGamma = Arrays.copyOfRange(cipher1, 0, cipher1.length/2);
		byte[] cipherPhi = Arrays.copyOfRange(cipher1, cipher1.length/2, cipher1.length);
		byte[] cipherGamma2 = Arrays.copyOfRange(cipher2, 0, cipher2.length/2);
		byte[] cipherPhi2 = Arrays.copyOfRange(cipher2, cipher2.length/2, cipher2.length);	
		
		BigInteger cipherGammaInt = new BigInteger(1,cipherGamma);
		BigInteger cipherPhiInt = new BigInteger(1,cipherPhi);
		BigInteger cipherGammaInt2 = new BigInteger(1,cipherGamma2);
		BigInteger cipherPhiInt2 = new BigInteger(1,cipherPhi2);
		
		BigInteger cipherGammaProduct = cipherGammaInt.multiply(cipherGammaInt2).mod(Group.getInstance().getModulo());
		BigInteger cipherPhiProduct = cipherPhiInt.multiply(cipherPhiInt2).mod(Group.getInstance().getModulo());
		
		byte[] out1 = cipherGammaProduct.toByteArray();
		byte[] out2 = cipherPhiProduct.toByteArray();
		
        byte[]  output = new byte[128];

        if (out1.length > output.length / 2)
        {
            System.arraycopy(out1, 1, output, output.length / 2 - (out1.length - 1), out1.length - 1);
        }
        else
        {
            System.arraycopy(out1, 0, output, output.length / 2 - out1.length, out1.length);
        }

        if (out2.length > output.length / 2)
        {
            System.arraycopy(out2, 1, output, output.length - (out2.length - 1), out2.length - 1);
        }
        else
        {
            System.arraycopy(out2, 0, output, output.length - out2.length, out2.length);
        }
		
        
        byte[] messageByte = Security.decryptElgamal(output, Security.getElgamalPrivateKey());
        BigInteger result = new BigInteger(1,messageByte);

        assertEquals(message1.multiply(message2), result);	
        
	}
	
	@Test
	public void testExponentialHomomorphicProperties1()
	{
		testExponentialHomomorphicProperties(200, 100);
	}
	
	@Test
	public void testExponentialHomomorphicProperties2()
	{
		testExponentialHomomorphicProperties(17, 16);
	}
	
	@Test
	public void testExponentialHomomorphicProperties3()
	{
		testExponentialHomomorphicProperties(1, 1);
	}
	
	@Test
	public void testExponentialHomomorphicProperties4()
	{
		testExponentialHomomorphicProperties(10, 20);
	}
	
	@Test
	public void testExponentialHomomorphicProperties5()
	{
		testExponentialHomomorphicProperties(1000, 2000);
	}
	
	@Test
	public void testExponentialHomomorphicProperties6()
	{
		testExponentialHomomorphicProperties(5, 4);
	}
	
	public void testExponentialHomomorphicProperties(long base1, long base2)
	{
		ElGamalPrivateKeyParameters ElGamalPrivateKey;
		ElGamalPublicKeyParameters ElGamalPublicKey;
		ElGamal.generateKeyPair(true);
		ElGamalPrivateKey = ElGamal.getPrivateKey();
		ElGamalPublicKey = ElGamal.getPublicKey();
		
		Group.getInstance().setGenerator(ElGamalPublicKey.getParameters().getG());
		Group.getInstance().setModulo(ElGamalPublicKey.getParameters().getP());
		BigInteger message1 = Group.getInstance().raiseGenerator(base1);
		byte[] messageByte1 = message1.toByteArray();
		messageByte1 = removeSignByte(messageByte1);
		messageByte1 = removeSignByte(messageByte1);
		byte[] cipher1 = Security.encryptElGamal(messageByte1, ElGamalPublicKey);
		
		BigInteger message2 = Group.getInstance().raiseGenerator(base2);
		byte[] messageByte2 = message2.toByteArray();
		messageByte2 = removeSignByte(messageByte2);
		byte[] cipher2 = Security.encryptElGamal(messageByte2, ElGamalPublicKey);
		
		byte[] product = Security.multiplyElGamalCiphers(cipher1, cipher2);
        
        byte[] messageByte = Security.decryptElgamal(product, ElGamalPrivateKey);
        BigInteger result = new BigInteger(1, messageByte);
        
        assertEquals(message1.multiply(message2).mod(Group.getInstance().getModulo()), result);
        
        assertEquals(base1+base2, Group.getInstance().discreteLogarithm(result));
	}
	
	@Test
	public void testExponentialHomomorphicPropertiesWithImporter()
	{
		try {
			ElGamalPublicKeyParameters pubKey = Importer.importElGamalPublicKeyParameters(Model.getElGamalPublicKeyFile());
			ElGamalPrivateKeyParameters privKey = Importer.importElGamalPrivateKeyParameters(Model.getElGamalPrivateKeyFile());
			Security.setElGamalPrivateKey(privKey);
			Security.setElGamalPublicKey(pubKey);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BigInteger message1base = new BigInteger("200");
		BigInteger message1 = Group.getInstance().raiseGenerator(message1base.longValue());
		byte[] messageByte1 = message1.toByteArray();
		messageByte1 = removeSignByte(messageByte1);
		byte[] cipher1 = Security.encryptElGamal(messageByte1, Security.getElgamalPublicKey());
		
		BigInteger message2base = new BigInteger("100");
		BigInteger message2 = Group.getInstance().raiseGenerator(message2base.longValue());
		byte[] messageByte2 = message2.toByteArray();
		byte[] cipher2 = Security.encryptElGamal(messageByte2, Security.getElgamalPublicKey());
		
		byte[] product = Security.multiplyElGamalCiphers(cipher1, cipher2);
        
        byte[] messageByte = Security.decryptElgamal(product, Security.getElgamalPrivateKey());
        BigInteger result = new BigInteger(messageByte);
        
        assertEquals(message1.multiply(message2).mod(Group.getInstance().getModulo()), result);
        
        assertEquals(message1base.add(message2base).longValue(), Group.getInstance().discreteLogarithm(result));
	}
	
	@Test
	public void testExponentialHomomorphicPropertiesWithLargeValues()
	{
		ElGamalPrivateKeyParameters ElGamalPrivateKey;
		ElGamalPublicKeyParameters ElGamalPublicKey;
		ElGamal.generateKeyPair(true);
		ElGamalPrivateKey = ElGamal.getPrivateKey();
		ElGamalPublicKey = ElGamal.getPublicKey();
		
		Group.getInstance().setGenerator(ElGamalPublicKey.getParameters().getG());
		Group.getInstance().setModulo(ElGamalPublicKey.getParameters().getP());
		
		BigInteger message1base;
		BigInteger message2base;
		
		for(long i = 10; i < 500000; i = i*10) {
			message1base = BigInteger.valueOf(i);
			message2base = BigInteger.valueOf(i*2);
			
			BigInteger message1 = Group.getInstance().raiseGenerator(message1base.longValue());
			byte[] messageByte1 = message1.toByteArray();
			messageByte1 = removeSignByte(messageByte1);
			System.out.println(byteArrayToString(messageByte1));
			byte[] cipher1 = Security.encryptElGamal(messageByte1, ElGamalPublicKey);
			
			BigInteger message2 = Group.getInstance().raiseGenerator(message2base.longValue());
			byte[] messageByte2 = message2.toByteArray();
			messageByte2 = removeSignByte(messageByte2);
			System.out.println(messageByte2.length);	
			byte[] cipher2 = Security.encryptElGamal(messageByte2, ElGamalPublicKey);
			
			byte[] product = Security.multiplyElGamalCiphers(cipher1, cipher2);
	        
	        byte[] messageByte = Security.decryptElgamal(product, ElGamalPrivateKey);
	        BigInteger result = new BigInteger(messageByte);
	        
	        assertEquals(message1base.add(message2base).longValue(), Group.getInstance().discreteLogarithm(result));
		}
	}
	
	@Test
	public void testExponentialHomomorphicPropertiesWithAccumulatedVotes() {
		try {
			ElGamalPublicKeyParameters pubKey = Importer.importElGamalPublicKeyParameters(Model.getElGamalPublicKeyFile());
			ElGamalPrivateKeyParameters privKey = Importer.importElGamalPrivateKeyParameters(Model.getElGamalPrivateKeyFile());
			Security.setElGamalPrivateKey(privKey);
			Security.setElGamalPublicKey(pubKey);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CorruptDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long message1 = 1;
		long message2 = 0;
		long accumulator = 0;
		
		byte[] cipher;
		
		byte[] productCipher = Security.encryptExponentialElgamal(message2, Security.getElgamalPublicKey());
		
		for (int i = 1; i < 100000; i = i * 10) {
			productCipher = Security.encryptExponentialElgamal(message2, Security.getElgamalPublicKey());
			accumulator = 0;
			for (int j = 0; j < i; j++) {
				cipher = Security.encryptExponentialElgamal(message1, Security.getElgamalPublicKey());
				productCipher = Security.multiplyElGamalCiphers(productCipher, cipher);
				accumulator += message1;
				cipher = Security.encryptExponentialElgamal(message2, Security.getElgamalPublicKey());
				productCipher = Security.multiplyElGamalCiphers(productCipher, cipher);
			}
			Assert.assertEquals(accumulator, Security.decryptExponentialElgamal(productCipher, Security.getElgamalPrivateKey()));
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testCipherLength() {
		ElGamalPrivateKeyParameters ElGamalPrivateKey;
		ElGamalPublicKeyParameters ElGamalPublicKey;
		ElGamal.generateKeyPair(true);
		ElGamalPrivateKey = ElGamal.getPrivateKey();
		ElGamalPublicKey = ElGamal.getPublicKey();
		
		String byteArrayS1 = "-119 57 -78 -108 -18 -14 10 84 -98 -70 48 13 84 -101 -86 -108 -90 -123 -92 126 95 -44 40 -31 -17 -87 69 -85 29 -84 23 54 17 46 15 -106 -106 -108 90 -106 -115 -43 -79 -33 -64 -79 28 -110 8 15 -69 119 -69 31 -35 65 40 -92 -122 -87 110 -76 -48 108";
		byte[] byteArray1 = stringToByteArray(byteArrayS1);
		System.out.println("Length of the byte array converted " + byteArray1.length);
		System.out.println(byteArrayToString(byteArray1));
		
		String byteArrayS2 = "111 -14 64 -36 52 -86 88 -118 44 -19 31 91 -38 125 115 -128 -5 -55 -9 -15 -20 -108 -60 73 92 17 -128 42 -53 46 6 -39 71 -111 65 -99 -88 -93 -112 0 113 -28 43 -92 -43 119 -65 55 45 100 42 121 -23 90 -10 89 -73 84 -26 -80 3 12 94 -111";
		byte[] byteArray2 = stringToByteArray(byteArrayS2);
		System.out.println("Length of the byte array converted " + byteArray2.length);
		System.out.println(byteArrayToString(byteArray2));
		
		byte[] cipher1 = Security.encryptElGamal(byteArray2, ElGamalPublicKey);
		System.out.println("Ciphered");
		byte[] cipher2 = Security.encryptElGamal(byteArray1, ElGamalPublicKey);
		System.out.println("Ciphered");
	}
	
	public byte[] removeSignByte(byte[] array)
	{
		if(array.length > Security.SIZE_OF_ELGAMAL_CIPHER / 2) {
			byte[] result = new byte[array.length-1];
			System.arraycopy(array, 1, result, 0, result.length);
			return result;
		} else {
			return array;
		}
	}

	public String byteArrayToString(byte[] array) {
		String result = "";
		for (int i = 0; i < array.length; i++) {
			result = result + Byte.toString(array[i]) + " ";
		}
		return result;
	}
	
	public byte[] stringToByteArray(String s) {
		s = s.trim();
		String[] split = s.split(" ");
		byte[] result = new byte[split.length];
		for (int i = 0; i < split.length; i++) {
			result[i] = Byte.parseByte(split[i]);
		}
		return result;
	}
	
	public static byte[] concat(byte[] first, byte[] second) {
		  byte[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
	}
}
