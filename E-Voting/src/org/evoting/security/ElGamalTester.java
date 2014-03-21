package org.evoting.security;

public class ElGamalTester {
	public static void main(String[] paramArrayOfString) {
		ElGamalHandler egh = new ElGamalHandler();
		System.out.println(egh.elgamalEncrypt("Hejsa"));
		//egh.enrypt("Hejsa");
	}
}
