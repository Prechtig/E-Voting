package org.evoting.security;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.Cipher;
    

public class RSA {
   private final static BigInteger one      = new BigInteger("1");
   private final static SecureRandom random = new SecureRandom();

   private BigInteger privateKey;
   private BigInteger publicKey;
   private BigInteger modulus;

   // generate an N-bit (roughly) public and private key
   RSA(int N) {
      BigInteger p = BigInteger.probablePrime(N/2, random);
      BigInteger q = BigInteger.probablePrime(N/2, random);
      BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

      modulus    = p.multiply(q);                                  
      publicKey  = new BigInteger("65537");     // common value in practice = 2^16 + 1
      privateKey = publicKey.modInverse(phi);
   }


   BigInteger encrypt(BigInteger message) {
      return message.modPow(publicKey, modulus);
   }

   BigInteger decrypt(BigInteger encrypted) {
      return encrypted.modPow(privateKey, modulus);
   }

   public String toString() {
      String s = "";
      s += "public  = " + publicKey  + "\n";
      s += "private = " + privateKey + "\n";
      s += "modulus = " + modulus;
      return s;
   }
 
   public static void main(String[] args) {
      int N = Integer.parseInt(args[0]);
      RSA key = new RSA(N);
      System.out.println(key);
 
      // create random message, encrypt and decrypt
      BigInteger message = new BigInteger(N-1, random);

      //// create message by converting string to integer
      // String s = "test";
      // byte[] bytes = s.getBytes();
      // BigInteger message = new BigInteger(s);

      BigInteger encrypt = key.encrypt(message);
      BigInteger decrypt = key.decrypt(encrypt);
      System.out.println("message   = " + message);
      System.out.println("encrpyted = " + encrypt);
      System.out.println("decrypted = " + decrypt);
   }
   
   
   public void rsaTest() throws Exception{
// Generate new key
KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
PrivateKey privateKey = keyPair.getPrivate();
String plaintext = "This is the message being signed";

// Compute signature
Signature instance = Signature.getInstance("SHA1withRSA");
instance.initSign(privateKey);
instance.update((plaintext).getBytes());
byte[] signature = instance.sign();

// Compute digest
MessageDigest sha1 = MessageDigest.getInstance("SHA1");
byte[] digest = sha1.digest((plaintext).getBytes());

// Encrypt digest
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, privateKey);
byte[] cipherText = cipher.doFinal(digest);

// Display results
System.out.println("Input data: " + plaintext);
System.out.println("Digest: " + bytes2String(digest));
System.out.println("Cipher text: " + bytes2String(cipherText));
System.out.println("Signature: " + bytes2String(signature));
   }
   
   
   private static String bytes2String(byte[] bytes) {
	    StringBuilder string = new StringBuilder();
	    for (byte b : bytes) {
	        String hexString = Integer.toHexString(0x00FF & b);
	        string.append(hexString.length() == 1 ? "0" + hexString : hexString);
	    }
	    return string.toString();
	}
}