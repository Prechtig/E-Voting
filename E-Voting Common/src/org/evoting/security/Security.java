package org.evoting.security;

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

public class Security {
	
	//True if RSA keys have been generated
	private static boolean RASKeysGenerated;
	//True if ElGamal keys have been generated
	private static boolean ElGamalKeysGenerated;
	
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
		return SHA1.hash(m);
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

	
	//Temporary
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
	}

	public static void setElGamalPrivateKey(ElGamalPrivateKeyParameters privK) {
		ElGamal.setPrivateKey(privK);
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
}
