package org.evoting.authority;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import jolie.runtime.ByteArray;
import jolie.runtime.Value;

import org.evoting.common.ValueIdentifiers;
import org.evoting.database.entities.ElectionOption;
import org.evoting.security.Security;

public class Model {
	private static String ElGamalPublicKeyFile = "ElGamalPublicKey";
	private static String ElGamalPrivateKeyFile = "ElGamalPrivateKey";
	private static String authRsaPrivKeyFilepath = "AuthRsaPriv";
	private static String authRsaPubKeyFilepath = "AuthRsaPub";
	private static String bbRsaPrivKeyFilepath = "BbRsaPriv";
	private static String bbRsaPubKeyFilepath = "BbRsaPub";
	
	private static String aCommunicationPath = "/";
	private static String electionOptionsFile = "ElectionOptions";

	private static List<ElectionOption> eOptions;

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * Creates a value containing a string and the string signed using the private key of the authority
	 * 
	 * @return A Value containing the informaiton for a validator as defined in Types.ol
	 */
	public static Value getNewValidator() {
		// Get random string
		String message = nextRandomString();
		if (Security.RSAAuthKeysSat()) {
			// Sign the random message
			byte[] signature = Security.sign(message, Security.getAuthorityRSAPrivateKey());
			// Create new value and set children
			Value val = Value.create();
			val.getNewChild(ValueIdentifiers.getMessage()).setValue(message);
			val.getNewChild(ValueIdentifiers.getSignature()).setValue(new ByteArray(signature));
			return val;
		} else {
			System.out.println("Cannot create validator without RSA keys");
		}
		return null;
	}
	
	/**
	 * Generates a random string
	 * 
	 * @return A random string
	 */
	private static String nextRandomString() {
		return new BigInteger(130, random).toString(32);
	}

	public static String getElGamalPublicKeyFile() {
		return ElGamalPublicKeyFile;
	}

	public static void setElGamalPublicKeyFile(String elGamalPublicKeyFile) {
		ElGamalPublicKeyFile = elGamalPublicKeyFile;
	}

	public static String getElGamalPrivateKeyFile() {
		return ElGamalPrivateKeyFile;
	}

	public static void setElGamalPrivateKeyFile(String elGamalPrivateKeyFile) {
		ElGamalPrivateKeyFile = elGamalPrivateKeyFile;
	}

	public static String getAuthRsaPrivKeyFilepath() {
		return authRsaPrivKeyFilepath;
	}

	public static void setAuthRsaPrivKeyFilepath(String authRsaPrivKeyFilepath) {
		Model.authRsaPrivKeyFilepath = authRsaPrivKeyFilepath;
	}

	public static String getAuthRsaPubKeyFilepath() {
		return authRsaPubKeyFilepath;
	}

	public static void setAuthRsaPubKeyFilepath(String authRsaPubKeyFilepath) {
		Model.authRsaPubKeyFilepath = authRsaPubKeyFilepath;
	}

	public static String getBbRsaPrivKeyFilepath() {
		return bbRsaPrivKeyFilepath;
	}

	public static void setBbRsaPrivKeyFilepath(String bbRsaPrivKeyFilepath) {
		Model.bbRsaPrivKeyFilepath = bbRsaPrivKeyFilepath;
	}

	public static String getBbRsaPubKeyFilepath() {
		return bbRsaPubKeyFilepath;
	}

	public static void setBbRsaPubKeyFilepath(String bbRsaPubKeyFilepath) {
		Model.bbRsaPubKeyFilepath = bbRsaPubKeyFilepath;
	}

	public static String getaCommunicationPath() {
		return aCommunicationPath;
	}

	public static void setaCommunicationPath(String aCommunicationPath) {
		Model.aCommunicationPath = aCommunicationPath;
	}

	public static String getElectionOptionsFile() {
		return electionOptionsFile;
	}

	public static void setElectionOptionsFile(String electionOptionsFile) {
		Model.electionOptionsFile = electionOptionsFile;
	}

	public static SecureRandom getRandom() {
		return random;
	}

	public static void setRandom(SecureRandom random) {
		Model.random = random;
	}

	public static List<ElectionOption> geteOptions() {
		return eOptions;
	}

	public static void seteOptions(List<ElectionOption> eOptions) {
		Model.eOptions = eOptions;
	}

	public static Value getElectionOptionsValue() {
		Value result = Value.create();
		
		for(ElectionOption e : eOptions) {
			Value electionOptions = result.getNewChild(ValueIdentifiers.getElectionOptions());
			electionOptions.getNewChild(ValueIdentifiers.getId()).setValue(e.getId());
			electionOptions.getNewChild(ValueIdentifiers.getName()).setValue(e.getName());
			electionOptions.getNewChild(ValueIdentifiers.getPartyId()).setValue(e.getPartyId());
		}
		
		return result;
	}
}
