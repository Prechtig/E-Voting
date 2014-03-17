package org.evoting.client;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Contains all encryption and decryption logic for the client.
 */
public class Security {
	
	private static final String passwordEncryptionAlgorithm = "MD5";
	private static final String bitEncoding = "UTF-8";

	/*
	 * Encrypts the password using the algorithm and encoding specified in the class fields.
	 */
	public static String encryptPassword(String password) {
		byte[] bytesOfPassword;
		byte[] digest = {0};
		try {
			bytesOfPassword = password.getBytes(bitEncoding);
			MessageDigest md = MessageDigest.getInstance(passwordEncryptionAlgorithm);
			digest = md.digest(bytesOfPassword);
		} catch (UnsupportedEncodingException e) {
			System.err.println("UTF-8 encoding is not supported on this platform.");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return digest.toString();
	}
}
