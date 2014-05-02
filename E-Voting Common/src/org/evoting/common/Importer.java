package org.evoting.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.database.entities.ElectionOption;

public class Importer {
	
	/**
	 * Loads ElGamal public key parameters from file
	 * <p>
	 * File structure:
	 * <br>
	 * y: BIGINTEGER <br>
	 * g: BIGINTEGER <br>
	 * p: BIGINTEGER <br>
	 * 
	 * @param fileName Path to the file containing the ElGamal public key
	 * @return The corresponding ElGamal public key parameters
	 * @throws IOException 
	 * @throws CorruptDataException 
	 * @throws FileNotFoundException 
	 */
	public static ElGamalPublicKeyParameters importElGamalPublicKeyParameters(String fileName) throws FileNotFoundException, CorruptDataException, IOException {
		//Retreives the 3 values for the parameters
		BigInteger[] result = loadElGamalKeyFile(fileName, KeyType.PUBLIC);

		//Sets the values
		BigInteger y = result[0];
		BigInteger g = result[1];
		BigInteger p = result[2];

		//Create and return value
		return new ElGamalPublicKeyParameters(y, new ElGamalParameters(g, p));
	}

	/**
	 * Loads ElGamal public key parameters from file
	 * <p>
	 * File structure:
	 * <br>
	 * x: BIGINTEGER <br>
	 * g: BIGINTEGER <br>
	 * p: BIGINTEGER <br>
	 * 
	 * @param fileName Path to the file containing the ElGamal private key
	 * @return The corresponding ElGamal private key parameters
	 * @throws IOException 
	 * @throws CorruptDataException 
	 * @throws FileNotFoundException 
	 */
	public static ElGamalPrivateKeyParameters importElGamalPrivateKeyParameters(String fileName) throws FileNotFoundException, CorruptDataException, IOException {
		//Retreives the 3 values for the parameters
		BigInteger[] result = loadElGamalKeyFile(fileName, KeyType.PRIVATE);

		//Sets the values
		BigInteger x = result[0];
		BigInteger g = result[1];
		BigInteger p = result[2];

		//Create and return value
		return new ElGamalPrivateKeyParameters(x, new ElGamalParameters(g, p));
	}

	/**
	 * 
	 * @param fileName
	 * @param type
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException
	 * @throws CorruptDataException 
	 */
	private static BigInteger[] loadElGamalKeyFile(String fileName, KeyType type) throws FileNotFoundException, CorruptDataException, IOException {
		BigInteger[] result = new BigInteger[3];
		File file = new File(fileName);

		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			int i = 0;
			String line;
			while ((line = br.readLine()) != null) {
				switch (type) {
				case PUBLIC:
					result[i] = ElGamalKeySwitch(line, type);
					break;
				case PRIVATE:
					result[i] = ElGamalKeySwitch(line, type);
					break;
				}
				i++;
			}
			br.close();
		}
		return result;
	}

	private static BigInteger ElGamalKeySwitch(String line, KeyType type) throws CorruptDataException {
		if (type != null && type == KeyType.PUBLIC) {
			switch (line.charAt(0)) {
			case 'y':
				return new BigInteger(line.substring(2));
			case 'g':
				return new BigInteger(line.substring(2));
			case 'p':
				return new BigInteger(line.substring(2));
			default:
				throw new CorruptDataException("Corrupted file");
			}
		} else if (type != null && type == KeyType.PRIVATE) {
			switch (line.charAt(0)) {
			case 'x':
				return new BigInteger(line.substring(2));
			case 'g':
				return new BigInteger(line.substring(2));
			case 'p':
				return new BigInteger(line.substring(2));
			default:
				throw new CorruptDataException("Corrupted file");
			}
		}
		//Should not happen
		return null;
	}
	
	public static ArrayList<ElectionOption> importElectionOptions(String fileName) {
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
			} else {
				System.out.println("File does not exist");
				return null;
			}
		} catch (IOException e) {
			System.out.println("Error working with election options file");
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
}
