package org.evoting.security;

import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSATest {
	private PublicKey pubK;
	private PrivateKey privK;

	public RSATest() {

	}
	
	//TODO: Fix these warnings!
	@SuppressWarnings("unused")
	public void generateKeys() {
		try {
			KeyPairGenerator keyGen;
			keyGen = KeyPairGenerator.getInstance("RSA");

			keyGen.initialize(1024);
			Provider p = keyGen.getProvider();

			KeyPair keys = keyGen.generateKeyPair();

			pubK = keys.getPublic();
			privK = keys.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String encrypt(String text) {
		byte[] cipherText = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA");
			// encrypt the plain text using the public key
			cipher.init(Cipher.ENCRYPT_MODE, pubK);
			cipherText = cipher.doFinal(text.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(cipherText, Charset.forName("UTF-8"));
	}

	public String decrypt(byte[] text) {
		byte[] dectyptedText = null;
		try {
			// get an RSA cipher object and print the provider
			final Cipher cipher = Cipher.getInstance("RSA");

			// decrypt the text using the private key
			cipher.init(Cipher.DECRYPT_MODE, privK);
			dectyptedText = cipher.doFinal(text);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new String(dectyptedText, Charset.forName("UTF-8"));
	}
}
