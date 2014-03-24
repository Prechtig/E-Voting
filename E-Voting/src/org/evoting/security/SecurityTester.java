package org.evoting.security;

public class SecurityTester {
	public SecurityTester(){
		
	}
	
	public boolean testAll(){
		testElgamal();
		testRSA();
		testSHA1();
		return true;
	}
	
	public void testElgamal(){
		ElGamal e = new ElGamal();
		try {
			e.encryptDecryptTest();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
	}
	
	public void testRSA(){
		RSA r = new RSA();
		try {
			r.encryptDecryptTest();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	private void testSHA1() {
		SHA1 s = new SHA1();
		try {
			s.hashTest();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
	}
}
