package org.evoting.security;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSA {
	private static PublicKey authPubKey, bbPubKey;
	private static PrivateKey authPrivKey, bbPrivKey;
	
	public static void generateAuthKeyPair(boolean overwrite) {
		if ((authPubKey == null && authPrivKey == null) || overwrite == true) {
			try {
				KeyPairGenerator keyGen;
				keyGen = KeyPairGenerator.getInstance("RSA");

				keyGen.initialize(1024);

				KeyPair keys = keyGen.generateKeyPair();

				authPubKey = keys.getPublic();
				authPrivKey = keys.getPrivate();
			} catch (NoSuchAlgorithmException e) {
				// Should not happen
				e.printStackTrace();
			}
		}
	}
	
	public static void generateBbKeyPair(boolean overwrite) {
		if ((bbPubKey == null && bbPrivKey == null) || overwrite == true) {
			try {
				KeyPairGenerator keyGen;
				keyGen = KeyPairGenerator.getInstance("RSA");

				keyGen.initialize(1024);

				KeyPair keys = keyGen.generateKeyPair();

				bbPubKey = keys.getPublic();
				bbPrivKey = keys.getPrivate();
			} catch (NoSuchAlgorithmException e) {
				// Should not happen
				e.printStackTrace();
			}
		}
	}

	/*//TODO:Remove?
	private static void generateKeyPair(PublicKey pubKey, PrivateKey privKey, boolean overwrite) {
		if ((pubKey == null && privKey == null) || overwrite == true) {
			try {
				KeyPairGenerator keyGen;
				keyGen = KeyPairGenerator.getInstance("RSA");

				keyGen.initialize(1024);

				KeyPair keys = keyGen.generateKeyPair();

				pubKey = keys.getPublic();
				privKey = keys.getPrivate();
			} catch (NoSuchAlgorithmException e) {
				// Should not happen
				e.printStackTrace();
			}
		}
	}*/
	
	public static PublicKey getAuthorityPublicKey() {
		return authPubKey;
	}

	public static PrivateKey getAuthorityPrivateKey() {
		return authPrivKey;
	}
	
	public static PublicKey getBulletinBoardPublicKey() {
		return bbPubKey;
	}
	
	public static PrivateKey getBulletinBoardPrivateKey() {
		return bbPrivKey;
	}

	public static byte[] encrypt(String m, Key pK) {
		return encrypt(m.getBytes(), pK);
	}

	public static byte[] encrypt(byte[] m, Key pK) {
		// ENCRYPT
		byte[] result = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA");
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, pK);
			result = cipher.doFinal(m);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static byte[] decrypt(String m, Key pK) {
		return decrypt(m.getBytes(), pK);
	}

	public static byte[] decrypt(byte[] m, Key pK) {
		// DECRYPT
		byte[] result = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA");

			// decrypt the text using the private key
			cipher.init(Cipher.DECRYPT_MODE, pK);
			result = cipher.doFinal(m);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	public static void setAuthorityPublicKey(PublicKey pubK) {
		authPubKey = pubK;
	}

	public static void setAuthorityPrivateKey(PrivateKey privK) {
		authPrivKey = privK;
	}
	
	public static void setBulletinBoardPublicKey(PublicKey pubK) {
		bbPubKey = pubK;
	}
	
	public static void setBulletinBoardPrivateKey(PrivateKey privK) {
		bbPrivKey = privK;
	}
}
