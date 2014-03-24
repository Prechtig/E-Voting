package org.evoting.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSA {
	private PublicKey pubK;
	private PrivateKey privK;
	
	
	public void encryptDecryptTest() throws SecurityException{
		String m = "Test String";
		
		
		//Generate keys
		try {
			KeyPairGenerator keyGen;
			keyGen = KeyPairGenerator.getInstance("RSA");
	
			keyGen.initialize(1024);
			//Provider p = keyGen.getProvider(); TODO: What is this for
			
			KeyPair keys = keyGen.generateKeyPair();
			
			pubK = keys.getPublic();
			privK = keys.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Encrypt
		byte[] cipherText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance("RSA");
	      // encrypt the plain text using the public key
	      cipher.init(Cipher.ENCRYPT_MODE, pubK);
	      cipherText = cipher.doFinal(m.getBytes("UTF-8"));
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
		
	    
	    
	    //Decrypt
	    byte[] dectyptedText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance("RSA");

	      // decrypt the text using the private key
	      cipher.init(Cipher.DECRYPT_MODE, privK);
	      dectyptedText = cipher.doFinal(cipherText);

	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }
		
	    String result = new String(dectyptedText);
	    
	    if (!result.equals(m)){
	    	throw new SecurityException("RSA encryption/decryption exception");
	    }
	}
	
	public static byte[] encrypt(String m, PrivateKey pK){
		return encrypt(m.getBytes(), pK);
	}
	
	public static byte[] encrypt(byte[] m, PrivateKey pK){
		//ENCRYPT
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
	
	public static byte[] decrypt(String m, PublicKey pK){
		return decrypt(m.getBytes(), pK);
	}
	
	public static byte[] decrypt(byte[] m, PublicKey pK){
		//DECRYPT
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
}
