package org.evoting.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {
	public static String byteToHex(byte[] b) {
	  String result = "";
	  for (int i=0; i < b.length; i++) {
	    result +=
	          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	  }
	  return result;
	}
	
	public static String hash(String m){
		return hash(m.getBytes());
	}
	
	public static String hash(byte[] m){
		MessageDigest md = null;
	    byte[] hashed = null;
		try {
	        md = MessageDigest.getInstance("SHA-1");
	    }
	    catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
		hashed = md.digest(m);
	    return byteToHex(hashed);
	}
}
