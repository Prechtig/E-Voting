package org.evoting.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {
	public void hashTest() throws SecurityException{
		String m = "Test String";
		String trueResult = "a5103f9c0b7d5ff69ddc38607c74e53d4ac120f2";
		String result = "";
		byte[] hashed = null;
		
		MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("SHA-1");
	        
	        hashed = md.digest(m.getBytes());
	    }
	    catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } 
	    result = byteToHex(hashed);
	    if (!result.equals(trueResult)){
	    	throw new SecurityException("SHA1 hash exception");
	    }
	}
	
	private static String byteToHex(byte[] b) {
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
