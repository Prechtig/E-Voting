package org.evoting.security;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;

public class Security implements ISecurity {

	public static void main(String[] paramArrayOfString) {
		generateKeys();
	}

	public static void generateKeys() {
		ElGamal.generateKeyPair(true);
		RSA.generateKeyPair(true);
	}

	@Override
	public byte[] encryptElGamal(String m, ElGamalPublicKeyParameters pK) {
		return ElGamal.encrypt(m, pK);
	}

	@Override
	public byte[] encryptElGamal(byte[] m, ElGamalPublicKeyParameters pK) {
		return ElGamal.encrypt(m, pK);
	}

	@Override
	public byte[] decryptElgamal(byte[] m, ElGamalPrivateKeyParameters pK) {
		return ElGamal.decrypt(m, pK);
	}

	@Override
	public byte[] encryptRSA(String hash, PrivateKey pK) {
		return RSA.encrypt(hash, pK);
	}

	@Override
	public byte[] encryptRSA(byte[] hash, PrivateKey pK) {
		return RSA.encrypt(hash, pK);
	}

	@Override
	public byte[] decryptRSA(byte[] m, PublicKey pK) {
		return RSA.decrypt(m, pK);
	}

	@Override
	public String hash(String m) {
		return SHA1.hash(m);
	}

	@Override
	public byte[] sign(String m, PrivateKey pK) {
		String hash = hash(m);
		return encryptRSA(hash, pK);
	}

	
	//Temporary
	public ElGamalPublicKeyParameters getElgamalPublicKey() {
		return ElGamal.getPublicKey();
	}

	public ElGamalPrivateKeyParameters getElgamalPrivatecKey() {
		return ElGamal.getPrivateKey();
	}

	public PublicKey getRSAPublicKey() {
		return RSA.getPublicKey();
	}

	public PrivateKey getRSAPrivateKey() {
		return RSA.getPrivateKey();
	}

	public void setElGamalPublicKey(ElGamalPublicKeyParameters pubK) {
		ElGamal.setPublicKey(pubK);
	}

	public void setElGamalPrivateKey(ElGamalPrivateKeyParameters privK) {
		ElGamal.setPrivateKey(privK);
	}

	public void setRSAPublicKey(PublicKey pubK) {
		RSA.setPublicKey(pubK);
	}

	public void setRSAPrivateKey(PrivateKey privK) {
		RSA.setPrivateKey(privK);
	}
}
