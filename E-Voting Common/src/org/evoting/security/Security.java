package org.evoting.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;

public class Security implements ISecurity {
	
	private static boolean RASKeysGenerated;
	private static boolean ElGamalKeysGenerated;
	
	private static Security instance = new Security();

	public static void main(String[] paramArrayOfString) {
		generateKeys();
	}

	public static void generateKeys() {
		ElGamal.generateKeyPair(true);
		RSA.generateKeyPair(true);
		RASKeysGenerated = true;
		ElGamalKeysGenerated = true;
	}
	
	public static void generateElGamalKeys(){
		ElGamal.generateKeyPair(true);
	}
	
	public static void generateRSAKeys(){
		RSA.generateKeyPair(true);
	}
	
	public static boolean RSAKeysGenerated() {
		return RASKeysGenerated;
	}
	
	public static boolean ElGamalKeysGenerated() {
		return ElGamalKeysGenerated;
	}
	
	public static boolean keysGenerated() {
		return RASKeysGenerated && ElGamalKeysGenerated;
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
	
	
	// FILE HANDLING
	public static ElGamalPublicKeyParameters loadElGamalPublicKey(String fileName) {
		BigInteger y = null;
		BigInteger g = null;
		BigInteger p = null;

		try {
			File file = new File(fileName);

			if (file.exists()) {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while ((line = br.readLine()) != null) {
					switch (line.charAt(0)) {
					case 'y':
						y = new BigInteger(line.substring(2));
						break;
					case 'g':
						g = new BigInteger(line.substring(2));
						break;
					case 'p':
						p = new BigInteger(line.substring(2));
						break;
					default:
						throw new IOException("Corrupted file");
					}
				}
				br.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ElGamalPublicKeyParameters(y, new ElGamalParameters(g, p));
	}

	public static ElGamalPrivateKeyParameters loadElGamalPrivateKey(String fileName) {
		// load file
		// get
		BigInteger x = null;
		BigInteger g = null;
		BigInteger p = null;

		try {
			File file = new File(fileName);

			if (file.exists()) {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				while ((line = br.readLine()) != null) {
					switch (line.charAt(0)) {
					case 'x':
						x = new BigInteger(line.substring(2));
						break;
					case 'g':
						g = new BigInteger(line.substring(2));
						break;
					case 'p':
						p = new BigInteger(line.substring(2));
						break;
					default:
						throw new IOException("Corrupted file");
					}
				}
				br.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ElGamalPrivateKeyParameters(x, new ElGamalParameters(g, p));
	}

	public static void saveElGamalPublicKey(ElGamalPublicKeyParameters pubK, String fileName) {
		BigInteger y = pubK.getY();
		ElGamalParameters param = pubK.getParameters();
		BigInteger g = param.getG();
		BigInteger p = param.getP();

		StringBuilder sb = new StringBuilder();
		sb.append("y:");
		sb.append(y);
		sb.append(System.getProperty("line.separator"));
		sb.append("g:");
		sb.append(g);
		sb.append(System.getProperty("line.separator"));
		sb.append("p:");
		sb.append(p);
		sb.append(System.getProperty("line.separator"));

		try {
			File file = new File(fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// write one method to save a given string to a given filename. A static class to handle that maybe? to load and save files
	public static void saveElGamalPrivateKey(ElGamalPrivateKeyParameters privK, String fileName) {
		BigInteger x = privK.getX();
		ElGamalParameters param = privK.getParameters();
		BigInteger g = param.getG();
		BigInteger p = param.getP();

		StringBuilder sb = new StringBuilder();
		sb.append("x:");
		sb.append(x);
		sb.append(System.getProperty("line.separator"));
		sb.append("g:");
		sb.append(g);
		sb.append(System.getProperty("line.separator"));
		sb.append("p:");
		sb.append(p);
		sb.append(System.getProperty("line.separator"));

		try {
			File file = new File(fileName);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public byte[] getRSAPublicKeyBytes() {
		return getRSAPublicKey().getEncoded();
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
	
	public void setRSAPublicKey(byte[] pubK) {
		//TODO: Do proper exception handling
		try {
			PublicKey publicKey =  KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubK));
			setRSAPublicKey(publicKey);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void setRSAPrivateKey(PrivateKey privK) {
		RSA.setPrivateKey(privK);
	}
	
	public static Security getInstance()
	{
		return instance;
	}
}
