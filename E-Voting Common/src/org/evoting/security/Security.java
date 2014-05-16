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
import org.evoting.common.utility.Converter;


public class Security {
	//Size of ELGamal byte array
	public static final int SIZE_OF_ELGAMAL_CIPHER = 128;
	//The group defined by the ElGamal parameters.
	private static Group group = Group.getInstance();
	
	//Main method for testing
	public static void main(String[] paramArrayOfString) {
		//generateKeys();
	}
	
	/**
	 * Generate ElGamal key set and will overwrite previous key set if there is any
	 */
	public static void generateElGamalKeys(boolean overwrite){
		//TODO:Is it ever called with false?
		ElGamal.generateKeyPair(overwrite);
	}
	
	/**
	 * Generate RSA key set and will overwrite previous key set if there is any
	 */
	public static void generateRSAKeys(){
		RSA.generateAuthKeyPair(true);
		RSA.generateBbKeyPair(true);
	}
	
	/**
	 * Check if the authority RSA keys have been sat
	 * @return if the RSA keys of the authority have been sat
	 */
	public static boolean RSAAuthKeysSat() {
		return (RSA.getAuthorityPrivateKey() != null && RSA.getAuthorityPublicKey() != null);
	}
	
	/**
	 * Check if both RSA and ElGamal key sets have been sat
	 * @return if both key sets have been sat
	 */
	//TODO:Unused
	public static boolean keysSat() {
		return RSAKeysSat() && ElGamalKeysSat();
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
	 * Encrypts a message with exponential ElGamal, such that it gain homomorphic properties.
	 * @param message The number to be encrypted
	 * @param pK The ElGamal public key
	 * @return The resulting ciphertext
	 */
	public static byte[] encryptExponentialElgamal(long message, ElGamalPublicKeyParameters pK) {
		byte[] raisedMessageByte = group.raiseGenerator(message).toByteArray();
		raisedMessageByte = removeSignByte(raisedMessageByte);
		return encryptElGamal(raisedMessageByte, pK);
	}
	
	/**
	 * Decrypts a byte array and solves the discrete logarithm.
	 * @param m The byte array to decrypt
	 * @param pK The ElGamal private key used to decrypt
	 * @return The decrypted byte array
	 */
	public static long decryptExponentialElgamal(byte[] m, ElGamalPrivateKeyParameters pK) {
		byte[] decrypted = ElGamal.decrypt(m, pK);
		BigInteger messageToPower = new BigInteger(1, decrypted);
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
	
	//TODO:Unused
	public static String hash(int m) {
		return hash(Converter.toByteArray(m));
	}
	
	//TODO:Unused
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
	
	//TODO:Unused
	public static byte[] sign(int m, Key pK) {
		String hash = hash(Converter.toByteArray(m));
		return encryptRSA(hash, pK);
	}
	
	public static byte[] sign(Key pK, byte[]... bytes){
		byte[] resultBytes = concatenateByteArrays(bytes);
		String hash = hash(resultBytes);
		return sign(hash, pK);
	}
	
	//TODO:Unused
	public static byte[] sign(long m, Key pK) {
		String hash = hash(Converter.toByteArray(m));
		return encryptRSA(hash, pK);
	}
	
	//TODO:Unused
	public static byte[] sign(byte[] m, Key pK) {
		String hash = hash(m);
		return encryptRSA(hash, pK);
	}
	
	public static boolean authenticate(byte[] receivedData, byte[] signature, PublicKey pk) {
		String signatureHash = new String(decryptRSA(signature, pk));
		String receivedDataHash = SHA1.hash(receivedData);
		
		return signatureHash.equals(receivedDataHash);
	}
	
	public static byte[] concatenateByteArrays(byte[]... bytes){
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream( );
		for (byte[] b : bytes) {
			try {
				resultStream.write(b);
			} catch (IOException e) {
				// Should not happen
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

	public static PublicKey getAuthorityRSAPublicKey() {
		return RSA.getAuthorityPublicKey();
	}
	
	public static PrivateKey getAuthorityRSAPrivateKey() {
		return RSA.getAuthorityPrivateKey();
	}
	
	public static PublicKey getBulletinBoardRSAPublicKey() {
		return RSA.getBulletinBoardPublicKey();
	}
	
	public static PrivateKey getBulletinBoardRSAPrivateKey() {
		return RSA.getBulletinBoardPrivateKey();
	}

	public static void setElGamalPublicKey(BigInteger y, BigInteger p, BigInteger g, int l) {
		ElGamalParameters elGamalp = new ElGamalParameters(p, g);
		ElGamalPublicKeyParameters elGamalpkp = new ElGamalPublicKeyParameters(y, elGamalp);
		setElGamalPublicKey(elGamalpkp);
		setGroupParameters(g, p);
	}
	
	public static void setElGamalPublicKey(ElGamalPublicKeyParameters elGamalPublicKey) {
		ElGamal.setPublicKey(elGamalPublicKey);
		setGroupParameters(elGamalPublicKey.getParameters().getG(), elGamalPublicKey.getParameters().getP());
	}

	public static void setElGamalPrivateKey(ElGamalPrivateKeyParameters privK) {
		ElGamal.setPrivateKey(privK);
		setGroupParameters(privK.getParameters().getG(), privK.getParameters().getP());
	}
	
	private static void setGroupParameters(BigInteger g, BigInteger p) {
		group.setGenerator(g);
		group.setModulo(p);
	}

	public static void setAuthorityRSAPublicKey(PublicKey pubK) {
		RSA.setAuthorityPublicKey(pubK);
	}
	
	public static void setBulletinBoardRSAPublicKey(byte[] pubK) {
		try {
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubK));
			setBulletinBoardRSAPublicKey(publicKey);
		} catch (InvalidKeySpecException e) {
			System.out.println("Corrupted bytes for public key");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// Should not happen
			e.printStackTrace();
		}
	}

	public static void setAuthorityRSAPrivateKey(PrivateKey privK) {
		RSA.setAuthorityPrivateKey(privK);
	}
	
	public static void setBulletinBoardRSAPublicKey(PublicKey pubK) {
		RSA.setBulletinBoardPublicKey(pubK);
	}
	
	public static void setBulletinBoardRSAPrivateKey(PrivateKey privK) {
		RSA.setBulletinBoardPrivateKey(privK);
	}

	public static byte[] multiplyElGamalCiphers(byte[] cipher1, byte[] cipher2)
	{
		if(cipher1.length != ElGamal.getSizeOfElgamalCipher() || cipher2.length != ElGamal.getSizeOfElgamalCipher()) {
			throw new IllegalArgumentException();
		}
		
		// Separates the two parts of the cipher arrays.
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
		
        byte[]  result = new byte[ElGamal.getSizeOfElgamalCipher()];

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
	
	/**
	 * Builds a cache to speed up the decryption of exponential elgamal.
	 * @param max the highest number of votes that the cache can provide performance enhancements for.
	 */
	public static void buildCache(long max) {
		group.buildCache(max);
	}
	
	private static byte[] removeSignByte(byte[] array)
	{
		if(array.length > Security.SIZE_OF_ELGAMAL_CIPHER / 2) {
			byte[] result = new byte[array.length-1];
			System.arraycopy(array, 1, result, 0, result.length);
			return result;
		} else {
			return array;
		}
	}
	
	
	   //---------------------------------------\\
	  //										 \\
	 //         ONLY USED IN TEST METHODS         \\
	//											   \\
   //-----------------------------------------------\\
	
	/**
	 * Encrypts a string using ElGamal
	 * @param m The string to encrypt
	 * @param pK the ElGamal public key used to encrypt
	 * @return The encrypted string
	 */
	//TODO:Used in: SecurityTest
	public static byte[] encryptElGamal(String m, ElGamalPublicKeyParameters pK) {
		return ElGamal.encrypt(m, pK);
	}
	
	/**
	 * Decrypts a byte array
	 * @param m The byte array to decrypt
	 * @param pK The ElGamal private key used to decrypt
	 * @return The decrypted byte array
	 */
	//TODO:Used in: SecurityTest, HomomorphicEncryptionTest
	public static byte[] decryptElgamal(byte[] m, ElGamalPrivateKeyParameters pK) {
		return ElGamal.decrypt(m, pK);
	}
	
	/**
	 * Check if RSA keys have been sat
	 * @return if RSA keys have been sat
	 */
	//TODO:Used in: HibernateTest, Database
	public static boolean RSAKeysSat() {
		return (RSA.getAuthorityPrivateKey() != null && RSA.getAuthorityPublicKey() != null && RSA.getBulletinBoardPrivateKey() != null && RSA.getBulletinBoardPublicKey() != null);
	}
	
	/**
	 * Check if ElGamal keys have been sat
	 * @return if ElGamal keys have been sat
	 */
	//TODO:Used in: Database
	public static boolean ElGamalKeysSat() {
		return (ElGamal.getPrivateKey() != null && ElGamal.getPublicKey() != null);
	}
	
	/**
	 * Generates both RSA and ElGamal key sets
	 */
	//TODO:Used in: ImportExportTest
	public static void generateKeys() {
		generateElGamalKeys(true);
		generateRSAKeys();
	}
}
