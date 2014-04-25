package org.evoting.common;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.database.entities.ElectionOption;

public class Importer {
	public static ElGamalPublicKeyParameters importElGamalPublicKeyParameters(String fileName) {
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

	public static ArrayList<ElectionOption> importElectionOptions(String fileName){		
		try {
			File file = new File(fileName);

			if (file.exists()) {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				
				String delimiter = br.readLine();
				ArrayList<ElectionOption> electionOptions = new ArrayList<ElectionOption>();
				
				String line;
				while ((line = br.readLine()) != null) {
					String[] parts = line.split(delimiter);
					int electionOptionId = Integer.parseInt(parts[0]);
					String name = parts[1];
					int partyId = Integer.parseInt(parts[2]);
					
					ElectionOption e = new ElectionOption(electionOptionId, name, partyId);
					electionOptions.add(e);
				}
				br.close();
				
			return electionOptions;
			}
		} catch (IOException e) {
			//TODO:fix message
			e.printStackTrace();
		}
		
		return null;
	}

	public static PublicKey importRsaPublicKey(String pathname) throws IOException {
		return (PublicKey) importRsaKey(pathname, KeyType.PUBLIC);
	}

	public static PrivateKey importRsaPrivateKey(String pathname) throws IOException {
		return (PrivateKey) importRsaKey(pathname, KeyType.PRIVATE);
	}

	private static Key importRsaKey(String pathname, KeyType type) throws IOException {
		try {
			byte[] readBytes = Files.readAllBytes(Paths.get(pathname));
			switch (type) {
			case PRIVATE:
				return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(readBytes));
			case PUBLIC:
				return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(readBytes));
			}
		} catch (InvalidKeySpecException e) {
			System.out.println("Invalid key file");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// Should not happen.
			e.printStackTrace();
		}
		return null;
	}

	private enum KeyType {
		PRIVATE, PUBLIC
	}
}
