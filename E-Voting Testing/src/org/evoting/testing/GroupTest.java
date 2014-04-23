package org.evoting.testing;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.util.Arrays;
import org.evoting.common.Group;
import org.evoting.security.ElGamal;
import org.evoting.security.Security;
import org.junit.Test;

public class GroupTest 
{
	private final static BigInteger x = new BigInteger("4506780392188996570736695313796530827587031827892136430736487172170543905782645794528673254441288061446761336834685517003994123226338108710462038955234465");
	private final static BigInteger y = new BigInteger("4769149317263233133961901176479144281438943771518267454962974918449664272035800816506479235917128178201252535931421419795967498293236707272729785739447608");
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
	public void testHomomorphicProperties()
	{
		/*
		BigInteger test = new BigInteger("100");
		
		byte[] testbyte = test.toByteArray();
		//byte[] test1byte = test1.toByteArray();
		//byte[] test2byte = test2.toByteArray();
		
		test = new BigInteger(1, testbyte);
		BigInteger test1 = new BigInteger(0, testbyte);
		BigInteger test2 = new BigInteger(testbyte);
		
		System.out.println("Test biginteger" + test.toString());
		System.out.println("Test biginteger" + test1.toString());
		System.out.println("Test biginteger" + test2.toString());
		*/
		ElGamalPrivateKeyParameters ElGamalPrivateKey;
		ElGamalPublicKeyParameters ElGamalPublicKey;
		ElGamal.generateKeyPair(true);
		ElGamalPrivateKey = ElGamal.getPrivateKey();
		ElGamalPublicKey = ElGamal.getPublicKey();
		
		System.out.println("P" + Group.getInstance().getModulo());
		System.out.println("x" + ElGamalPrivateKey.getX());
		
		Group.getInstance().setGenerator(ElGamalPublicKey.getParameters().getG());
		Group.getInstance().setModulo(ElGamalPublicKey.getParameters().getP());
		
		BigInteger message1 = new BigInteger("900");
		byte[] messageByte1 = message1.toByteArray();
		byte[] cipher1 = Security.encryptElGamal(messageByte1, ElGamalPublicKey);
		
		BigInteger message2 = new BigInteger("100");
		byte[] messageByte2 = message2.toByteArray();
		byte[] cipher2 = Security.encryptElGamal(messageByte2, ElGamalPublicKey);
		
		byte[] cipherGamma = Arrays.copyOfRange(cipher1, 0, cipher1.length/2);
		byte[] cipherPhi = Arrays.copyOfRange(cipher1, cipher1.length/2, cipher1.length);
		byte[] cipherGamma2 = Arrays.copyOfRange(cipher2, 0, cipher2.length/2);
		byte[] cipherPhi2 = Arrays.copyOfRange(cipher2, cipher2.length/2, cipher2.length);	
		System.out.println(cipherGamma.length);
		System.out.println(cipherGamma2.length);
		System.out.println(cipherPhi.length);
		System.out.println(cipherPhi2.length);
		
		BigInteger cipherGammaInt = new BigInteger(cipherGamma);
		BigInteger cipherPhiInt = new BigInteger(cipherPhi);
		BigInteger cipherGammaInt2 = new BigInteger(cipherGamma2);
		BigInteger cipherPhiInt2 = new BigInteger(cipherPhi2);
		
		
		BigInteger cipherGammaProduct = cipherGammaInt.multiply(cipherGammaInt2).mod(Group.getInstance().getModulo());
		BigInteger cipherPhiProduct = cipherPhiInt.multiply(cipherPhiInt2).mod(Group.getInstance().getModulo());
		
		byte[] out1 = cipherGammaProduct.toByteArray();
		byte[] out2 = cipherPhiProduct.toByteArray();
		System.out.println(out1.length);
		System.out.println(out2.length);
		
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
		
        
        byte[] messageByte = Security.decryptElgamal(output, ElGamalPrivateKey);
        BigInteger result = new BigInteger(messageByte);
        
        assertEquals(message1.multiply(message2), result);
	}
	
	@Test
	public void testHomomorphicPropertiesAndMultipyElGamalCiphers()
	{
		ElGamalPrivateKeyParameters ElGamalPrivateKey;
		ElGamalPublicKeyParameters ElGamalPublicKey;
		ElGamal.generateKeyPair(true);
		ElGamalPrivateKey = ElGamal.getPrivateKey();
		ElGamalPublicKey = ElGamal.getPublicKey();
		
		Group.getInstance().setGenerator(ElGamalPublicKey.getParameters().getG());
		Group.getInstance().setModulo(ElGamalPublicKey.getParameters().getP());
		
		BigInteger message1 = new BigInteger("900");
		byte[] messageByte1 = message1.toByteArray();
		byte[] cipher1 = Security.encryptElGamal(messageByte1, ElGamalPublicKey);
		
		BigInteger message2 = new BigInteger("100");
		byte[] messageByte2 = message2.toByteArray();
		byte[] cipher2 = Security.encryptElGamal(messageByte2, ElGamalPublicKey);
		
		byte[] product = Security.multiplyElGamalCiphers(cipher1, cipher2);
		
        byte[] messageByte = Security.decryptElgamal(product, ElGamalPrivateKey);
        BigInteger result = new BigInteger(messageByte);
        
        assertEquals(message1.multiply(message2), result);
	}
	
	@Test
	public void testExponentialHomomorphicProperties()
	{
		ElGamalPrivateKeyParameters ElGamalPrivateKey;
		ElGamalPublicKeyParameters ElGamalPublicKey;
		ElGamal.generateKeyPair(true);
		ElGamalPrivateKey = ElGamal.getPrivateKey();
		ElGamalPublicKey = ElGamal.getPublicKey();
		
		Group.getInstance().setGenerator(ElGamalPublicKey.getParameters().getG());
		Group.getInstance().setModulo(ElGamalPublicKey.getParameters().getP());
		
		BigInteger message1 = new BigInteger("200");
		message1 = Group.getInstance().raiseGenerator(message1.longValue());
		byte[] messageByte1 = message1.toByteArray();
		byte[] cipher1 = Security.encryptElGamal(messageByte1, ElGamalPublicKey);
		
		BigInteger message2 = new BigInteger("100");
		message2 = Group.getInstance().raiseGenerator(message2.longValue());
		byte[] messageByte2 = message2.toByteArray();
		byte[] cipher2 = Security.encryptElGamal(messageByte2, ElGamalPublicKey);
		
		byte[] cipherGamma = Arrays.copyOfRange(cipher1, 0, cipher1.length/2);
		byte[] cipherPhi = Arrays.copyOfRange(cipher1, cipher1.length/2, cipher1.length);
		byte[] cipherGamma2 = Arrays.copyOfRange(cipher2, 0, cipher2.length/2);
		byte[] cipherPhi2 = Arrays.copyOfRange(cipher2, cipher2.length/2, cipher2.length);	
		
		BigInteger cipherGammaInt = new BigInteger(cipherGamma);
		BigInteger cipherPhiInt = new BigInteger(cipherPhi);
		BigInteger cipherGammaInt2 = new BigInteger(cipherGamma2);
		BigInteger cipherPhiInt2 = new BigInteger(cipherPhi2);
		
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
		
        
        byte[] messageByte = Security.decryptElgamal(output, ElGamalPrivateKey);
        BigInteger result = new BigInteger(messageByte);
        
        assertEquals(message1.multiply(message2), result);
	}

	public static byte[] concat(byte[] first, byte[] second) {
		  byte[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
	}
}
