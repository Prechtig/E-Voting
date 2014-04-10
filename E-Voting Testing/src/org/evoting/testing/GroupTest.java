package org.evoting.testing;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

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
		
		int message = 1336;
		BigInteger messageAsPower = Group.getInstance().raiseGenerator(BigInteger.valueOf(message));
		int log = Group.getInstance().discreteLogarithm2(messageAsPower);
		assertEquals(message, log);
		
		int message2 = 8521;
		BigInteger message2AsPower = Group.getInstance().raiseGenerator(BigInteger.valueOf(message2));
		BigInteger messagesProduct = messageAsPower.multiply(message2AsPower);
		messagesProduct = messagesProduct.mod(Group.getInstance().getModulo());
		
		log = Group.getInstance().discreteLogarithm2(message2AsPower);
		assertEquals(message2, log);
		
		log = Group.getInstance().discreteLogarithm2(messagesProduct);
		System.out.println("Third assert");
		assertEquals(message + message2, log);
		
	}
	
	@Test
	public void testHomomorphicProperties()
	{
		BigInteger y = new BigInteger("4769149317263233133961901176479144281438943771518267454962974918449664272035800816506479235917128178201252535931421419795967498293236707272729785739447608");
		BigInteger p = new BigInteger("7781871412403125272081994420237498498848517960190636813994593624893166160780445513887821412313009361625936687452586174696909303141691170346568788390764347");
		BigInteger g = new BigInteger("1112413672743230891881273653171640834687833299890125651932066327486193358080471215100797237775771467149894260123512660136402158406998369481252844452718796");
		int l = 0;
		
		Group.getInstance().setGenerator(g);
		Group.getInstance().setModulo(p);
		Security.setElGamalPublicKey(y, p, g, l);
		
		int message = 1336;
		byte[] messageAsPowerAsByte = Group.getInstance().raiseGenerator(BigInteger.valueOf(message)).toByteArray();
		byte[] cipher = Security.encryptElGamal(messageAsPowerAsByte, Security.getElgamalPublicKey());
		
		int message2 = 8521;
		byte[] message2AsPowerAsByte = Group.getInstance().raiseGenerator(BigInteger.valueOf(message2)).toByteArray();
		byte[] cipher2 = Security.encryptElGamal(message2AsPowerAsByte, Security.getElgamalPublicKey());
		
		BigInteger cipherInt = new BigInteger(cipher);
		BigInteger cipherInt2 = new BigInteger(cipher2);
		
		BigInteger cipherProduct = cipherInt.multiply(cipherInt2);
		
	}
}
