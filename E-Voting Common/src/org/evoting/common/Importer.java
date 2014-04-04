package org.evoting.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;

public class Importer {
	public static ElGamalPublicKeyParameters importElGamalPublicKeyParameters(String fileName){
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
	
	public static ElGamalPrivateKeyParameters importElGamalPrivateKeyParameters(String fileName) {
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
}
