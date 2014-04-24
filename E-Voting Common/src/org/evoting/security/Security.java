package org.evoting.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.util.Arrays;
import org.evoting.common.Converter;
import org.evoting.common.Group;


public class Security {
	
	//True if RSA keys have been generated
	private static boolean RASKeysGenerated;
	//True if ElGamal keys have been generated
	private static boolean ElGamalKeysGenerated;
	//Size of ELGamal byte array
	private static final int sizeOfElGamalCipher = 128;
	//The group defined by the ElGamal parameters.
	private static Group group = Group.getInstance();
	
	//Main method for testing
	public static void main(String[] paramArrayOfString) {
		generateKeys();
	}
	
	/**
	 * Generates both RSA and ElGamal key sets
	 */
	public static void generateKeys() {
		ElGamal.generateKeyPair(true);
		RSA.generateKeyPair(true);
		RASKeysGenerated = true;
		ElGamalKeysGenerated = true;
	}
	
	/**
	 * Generate ElGamal key set and will overwrite previous key set if there is any
	 */
	public static void generateElGamalKeys(){
		ElGamal.generateKeyPair(true);
	}
	
	/**
	 * Generate RSA key set and will overwrite previous key set if there is any
	 */
	public static void generateRSAKeys(){
		RSA.generateKeyPair(true);
	}
	
	/**
	 * Check if RSA keys have been generated
	 * @return if RSA keys have been generated
	 */
	public static boolean RSAKeysGenerated() {
		return RASKeysGenerated;
	}
	
	/**
	 * Check if ElGamal keys have been generated
	 * @return if ElGamal keys have been generated
	 */
	public static boolean ElGamalKeysGenerated() {
		return ElGamalKeysGenerated;
	}
	
	/**
	 * Check if both RSA and ElGamal key sets have been generated
	 * @return if both key sets have been generated
	 */
	public static boolean keysGenerated() {
		return RASKeysGenerated && ElGamalKeysGenerated;
	}
	
	/**
	 * Encrypts a string using ElGamal
	 * @param m The string to encrypt
	 * @param pK the ElGamal public key used to encrypt
	 * @return The encrypted string
	 */
	public static byte[] encryptElGamal(String m, ElGamalPublicKeyParameters pK) {
		return ElGamal.encrypt(m, pK);
	}

	/**
	 * Encrypts a byte array using ElGamal
	 * @param m The byte array to encrypt
	 * @param pK the ElGamal public key used to encrypt
	 * @return The encrypted byte array
	 */
	public static byte[] encryptElGamal(byte[] m, ElGamalPublicKeyParameters pK) {
		return ElGamal.encrypt(m, pK);
	}

	/**
	 * Decrypts a byte array
	 * @param m The byte array to decrypt
	 * @param pK The ElGamal private key used to decrypt
	 * @return The decrypted byte array
	 */
	public static byte[] decryptElgamal(byte[] m, ElGamalPrivateKeyParameters pK) {
		return ElGamal.decrypt(m, pK);
	}
	
	/**
	 * Decrypts a byte array and solves the discrete logarithm.
	 * @param m The byte array to decrypt
	 * @param pK The ElGamal private key used to decrypt
	 * @return The decrypted byte array
	 */
	public static long decryptExponentialElgamal(byte[] m, ElGamalPrivateKeyParameters pK) {
		byte[] decrypted = ElGamal.decrypt(m, pK);
		BigInteger messageToPower = new BigInteger(decrypted);
		return group.discreteLogarithm(messageToPower);
	}

	/**
	 * Encrypt a string using RSA
	 * @param hash The string to encrypt
	 * @param pK The key used to encrypt. This can be either java.security.PublicKey or .PrivateKey
	 * @return The encrypted byte array
	 */
	public static byte[] encryptRSA(String hash, Key pK) {
		return RSA.encrypt(hash, pK);
	}
	
	
	/**
	 * Encrypt a byte array using RSA
	 * @param hash byte array to encrypt
	 * @param pK The key used to encrypt. This can be either java.security.PublicKey or .PrivateKey
	 * @return The encrypted byte array
	 */
	public static byte[] encryptRSA(byte[] hash, Key pK) {
		return RSA.encrypt(hash, pK);
	}

	/**
	 * Decrypt a byte array using RSA
	 * @param m byte array to decrypt
	 * @param pK The key used to decrypt. This can be either java.security.PublicKey or .PrivateKey
	 * @return The decrypted byte array
	 */
	public static byte[] decryptRSA(byte[] m, Key pK) {
		return RSA.decrypt(m, pK);
	}

	/**
	 * Has a string using SHA-1
	 * @param m The string to hash
	 * @return The hashed string
	 */
	public static String hash(String m) {
		return hash(m.getBytes());
	}
	
	public static String hash(byte[] m) {
		return SHA1.hash(m);
	}
	
	public static String hash(int m) {
		return hash(Converter.toByteArray(m));
	}
	
	public static String hash(long m) {
		return hash(Converter.toByteArray(m));
	}

	/**
	 * Method used to sign a string using an RSA key.
	 * It both hashes the given string and encrypt it using the given key
	 * @param m String to sign
	 * @param pK The RSA key to encrypt with
	 * @return The encrypted byte array
	 */
	public static byte[] sign(String m, Key pK) {
		String hash = hash(m);
		return encryptRSA(hash, pK);
	}
	
	public static byte[] sign(int m, Key pK) {
		String hash = hash(Converter.toByteArray(m));
		return encryptRSA(hash, pK);
	}
	
	public static byte[] sign(Key pK, byte[]... bytes){
		byte[] resultBytes = concatenateByteArrays(bytes);
		String hash = hash(resultBytes);
		return sign(hash, pK);
	}
	
	public static byte[] sign(long m, Key pK) {
		String hash = hash(Converter.toByteArray(m));
		return encryptRSA(hash, pK);
	}
	
	public static byte[] sign(byte[] m, Key pK){
		String hash = hash(m);
		return encryptRSA(hash, pK);
	}
	
	public static byte[] concatenateByteArrays(byte[]... bytes){
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream( );
		for (byte[] b : bytes) {
			try {
				resultStream.write(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultStream.toByteArray();
	}
	
	public static ElGamalPublicKeyParameters getElgamalPublicKey() {
		return ElGamal.getPublicKey();
	}

	public static ElGamalPrivateKeyParameters getElgamalPrivateKey() {
		return ElGamal.getPrivateKey();
	}

	public static PublicKey getRSAPublicKey() {
		return RSA.getPublicKey();
	}
	
	public static byte[] getRSAPublicKeyBytes() {
		return getRSAPublicKey().getEncoded();
	}

	public static PrivateKey getRSAPrivateKey() {
		return RSA.getPrivateKey();
	}

	public static void setElGamalPublicKey(BigInteger y, BigInteger p, BigInteger g, int l) {
		ElGamalParameters elGamalp = new ElGamalParameters(p, g);
		ElGamalPublicKeyParameters elGamalpkp = new ElGamalPublicKeyParameters(y, elGamalp);
		ElGamal.setPublicKey(elGamalpkp);
		group.setGenerator(elGamalp.getG());
		group.setModulo(elGamalp.getP());
	}

	public static void setElGamalPrivateKey(ElGamalPrivateKeyParameters privK) {
		ElGamal.setPrivateKey(privK);
		group.setGenerator(privK.getParameters().getG());
		group.setModulo(privK.getParameters().getP());
	}

	public static void setRSAPublicKey(PublicKey pubK) {
		RSA.setPublicKey(pubK);
	}
	
	public static void setRSAPublicKey(byte[] pubK) {
		//TODO: Do proper exception handling
		try {
			PublicKey publicKey =  KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubK));
			setRSAPublicKey(publicKey);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static void setRSAPrivateKey(PrivateKey privK) {
		RSA.setPrivateKey(privK);
	}

	
	public static byte[] multiplyElGamalCiphers(byte[] cipher1, byte[] cipher2)
	{
		if(cipher1.length != sizeOfElGamalCipher || cipher2.length != sizeOfElGamalCipher) {
			throw new IllegalArgumentException();
		}
		
		// Seperates the two parts of the cipher arrays.
		byte[] cipherGamma1 = Arrays.copyOfRange(cipher1, 0, cipher1.length/2);
		byte[] cipherPhi1 = Arrays.copyOfRange(cipher1, cipher1.length/2, cipher1.length);
		byte[] cipherGamma2 = Arrays.copyOfRange(cipher2, 0, cipher2.length/2);
		byte[] cipherPhi2 = Arrays.copyOfRange(cipher2, cipher2.length/2, cipher2.length);	
		
		BigInteger gammaInt1 = new BigInteger(1, cipherGamma1);
		BigInteger phiInt1 = new BigInteger(1, cipherPhi1);
		BigInteger gammaInt2 = new BigInteger(1, cipherGamma2);
		BigInteger phiInt2 = new BigInteger(1, cipherPhi2);
		

		
		BigInteger gammaProduct = gammaInt1.multiply(gammaInt2).mod(group.getModulo());
		BigInteger phiProduct = phiInt1.multiply(phiInt2).mod(group.getModulo());
		
		byte[] gammaProductByte = gammaProduct.toByteArray();
		byte[] phiProductByte = phiProduct.toByteArray();
		
        byte[]  result = new byte[128];

        if (gammaProductByte.length > result.length / 2) {
            System.arraycopy(gammaProductByte, 1, result, result.length / 2 - (gammaProductByte.length - 1), gammaProductByte.length - 1);
        } else {
            System.arraycopy(gammaProductByte, 0, result, result.length / 2 - gammaProductByte.length, gammaProductByte.length);
        }

        if (phiProductByte.length > result.length / 2) {
            System.arraycopy(phiProductByte, 1, result, result.length - (phiProductByte.length - 1), phiProductByte.length - 1);
        } else {
            System.arraycopy(phiProductByte, 0, result, result.length - phiProductByte.length, phiProductByte.length);
        }
		
		return result;
	}
	
	private static byte[] concat(byte[] first, byte[] second) {
		  byte[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
	}
}
