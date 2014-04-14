package org.evoting.testing;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.util.Arrays;
import org.evoting.common.Group;
import org.evoting.security.Security;
import org.junit.Test;

public class GroupTest 
{
	@Test
	public void testRaiseAndDiscreteLog()
	{
		Group.getInstance().setGenerator(new BigInteger("1112413672743230891881273653171640834687833299890125651932066327486193358080471215100797237775771467149894260123512660136402158406998369481252844452718796"));
		Group.getInstance().setModulo(new BigInteger("7781871412403125272081994420237498498848517960190636813994593624893166160780445513887821412313009361625936687452586174696909303141691170346568788390764347"));
		
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
		Group.getInstance().setGenerator(new BigInteger("1112413672743230891881273653171640834687833299890125651932066327486193358080471215100797237775771467149894260123512660136402158406998369481252844452718796"));
		Group.getInstance().setModulo(new BigInteger("7781871412403125272081994420237498498848517960190636813994593624893166160780445513887821412313009361625936687452586174696909303141691170346568788390764347"));
		
		long message = 1336;
		BigInteger messageAsPower = Group.getInstance().raiseGenerator(message);
		long log = Group.getInstance().discreteLogarithmFromExpectedRange(messageAsPower, message, 10);
		assertEquals(message, log);	
	}
	
	@Test
	public void testHomomorphicProperties()
	{
		BigInteger x = new BigInteger("4506780392188996570736695313796530827587031827892136430736487172170543905782645794528673254441288061446761336834685517003994123226338108710462038955234465");
		BigInteger y = new BigInteger("4769149317263233133961901176479144281438943771518267454962974918449664272035800816506479235917128178201252535931421419795967498293236707272729785739447608");
		BigInteger p = new BigInteger("7781871412403125272081994420237498498848517960190636813994593624893166160780445513887821412313009361625936687452586174696909303141691170346568788390764347");
		BigInteger g = new BigInteger("1112413672743230891881273653171640834687833299890125651932066327486193358080471215100797237775771467149894260123512660136402158406998369481252844452718796");
		int l = 0;
		
		Group.getInstance().setGenerator(g);
		Group.getInstance().setModulo(p);
		Security.setElGamalPublicKey(y, p, g, l);
		ElGamalParameters elGamalp = new ElGamalParameters(p, g);
		ElGamalPrivateKeyParameters epkp = new ElGamalPrivateKeyParameters(x, elGamalp);
		Security.setElGamalPrivateKey(epkp);
		
		long message = 1336;
		byte[] messageAsPowerAsByte = Group.getInstance().raiseGenerator(message).toByteArray();
		byte[] cipher = Security.encryptElGamal(messageAsPowerAsByte, Security.getElgamalPublicKey());
		
		long message2 = 8521;
		byte[] message2AsPowerAsByte = Group.getInstance().raiseGenerator(message2).toByteArray();
		byte[] cipher2 = Security.encryptElGamal(message2AsPowerAsByte, Security.getElgamalPublicKey());
		
		System.out.println("cipher1 = " + java.util.Arrays.toString(cipher));
		System.out.println("cipher2 = " + java.util.Arrays.toString(cipher2));
		
		byte[] cipherGamma = Arrays.copyOfRange(cipher, 0, cipher.length/2);
		byte[] cipherPhi = Arrays.copyOfRange(cipher, cipher.length/2, cipher.length);
		byte[] cipherGamma2 = Arrays.copyOfRange(cipher2, 0, cipher2.length/2);
		byte[] cipherPhi2 = Arrays.copyOfRange(cipher2, cipher2.length/2, cipher2.length);	
		
		System.out.println("cipherGamma = " + java.util.Arrays.toString(cipherGamma));
		System.out.println("cipherPhi = " + java.util.Arrays.toString(cipherPhi));
		System.out.println("cipherGamma2 = " + java.util.Arrays.toString(cipherGamma2));
		System.out.println("cipherPhi2 = " + java.util.Arrays.toString(cipherPhi2));
		
		BigInteger cipherGammaInt = new BigInteger(cipherGamma);
		BigInteger cipherPhiInt = new BigInteger(cipherPhi);
		BigInteger cipherGammaInt2 = new BigInteger(cipherGamma2);
		BigInteger cipherPhiInt2 = new BigInteger(cipherPhi2);
		
		BigInteger cipherGammaProduct = cipherGammaInt.multiply(cipherGammaInt2).mod(Group.getInstance().getModulo());
		BigInteger cipherPhiProduct = cipherPhiInt.multiply(cipherPhiInt2).mod(Group.getInstance().getModulo());
		
		byte[] cipherGammaProductByte = cipherGammaProduct.toByteArray();
		byte[] cipherPhiProductByte = cipherGammaProduct.toByteArray();
		
		byte[] cipherProduct = concat(cipherGammaProductByte, cipherPhiProductByte);
		
		byte[] messageProductAsPowerByte = Security.decryptElgamal(cipherProduct, Security.getElgamalPrivateKey());
		
		BigInteger messagesProductAsPower = new BigInteger(messageProductAsPowerByte);
		
		long log = Group.getInstance().discreteLogarithm(messagesProductAsPower);
		
		assertEquals(message + message2, log);	
		/*
		System.out.println(cipher.length);
		System.out.println(cipher2.length);
		
		BigInteger cipherInt = new BigInteger(cipher);
		BigInteger cipherInt2 = new BigInteger(cipher2);
		
		System.out.println(cipherInt.toString());
		System.out.println(cipherInt2.toString());
		
		BigInteger cipherProduct = cipherInt.multiply(cipherInt2);
		byte[] cipherProductByte = cipherProduct.toByteArray();
		
		System.out.println(cipherProduct.toString());
		System.out.println(cipherProductByte.length);
		
		byte[] messageProductAsPowerByte = Security.decryptElgamal(cipherProductByte, Security.getElgamalPrivateKey());
		
		BigInteger messagesProductAsPower = new BigInteger(messageProductAsPowerByte);
		
		long log = Group.getInstance().discreteLogarithm(messagesProductAsPower);
		
		assertEquals(message + message2, log);	
		*/
	}
	
	public static byte[] concat(byte[] first, byte[] second) {
		  byte[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
	}
}
