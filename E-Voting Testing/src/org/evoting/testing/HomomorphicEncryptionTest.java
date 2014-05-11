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
		Security.buildCache(20000);
		
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
		
		long message = 1336;
		BigInteger messageAsPower = Group.getInstance().raiseGenerator(message);
		long log = Group.getInstance().discreteLogarithmFromExpectedRange(messageAsPower, message, 10);
		assertEquals(message, log);	
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
		
		byte[] cipher1 = Security.encryptExponentialElgamal(base1, Security.getElgamalPublicKey());
		byte[] cipher2 = Security.encryptExponentialElgamal(base2, Security.getElgamalPublicKey());
		
		byte[] product = Security.multiplyElGamalCiphers(cipher1, cipher2);
        
        byte[] messageByte = Security.decryptElgamal(product, Security.getElgamalPrivateKey());
        BigInteger result = new BigInteger(1, messageByte);
        
        assertEquals(base1+base2, Group.getInstance().discreteLogarithm(result));
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
}
