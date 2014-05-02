package org.evoting.authority.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import jolie.runtime.JavaService;

import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.authority.Model;
import org.evoting.common.Importer;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.database.entities.ElectionOption;
import org.evoting.security.Security;

public class Load extends Command {
	public static final String KEYWORD = "load";
	private static String invalidArguments = "Invalid arguments.";

	public Load(String[] args) {
		super(args);
	}

	public String execute(JavaService js) {
		if (args.length < 2) {
			return invalidArguments;
		}

		switch (args[1]) {
		case "options":
			return loadElectionOptions();
		case "rsa":
			return loadRSAKeys();
		case "elgamal":
			return loadElGamalKeys();
		default:
			return invalidArguments;
		}
	}

	/**
	 * Load ElGamal key pair into the system.
	 */
	private String loadElGamalKeys() { //TODO: add checks to see if the importer actually returns anything, like with optionslist
		try {
			ElGamalPublicKeyParameters pubKey = Importer.importElGamalPublicKeyParameters(Model.getElGamalPublicKeyFile());
			ElGamalPrivateKeyParameters privKey = Importer.importElGamalPrivateKeyParameters(Model.getElGamalPrivateKeyFile());
			Security.setElGamalPrivateKey(privKey);
			Security.setElGamalPublicKey(pubKey);

			return "Loaded ElGamal keys";
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Invalid file location.");
		} catch(CorruptDataException e) {
			e.printStackTrace();
			System.out.println("The file does not have the correct format.");
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("Something went wrong.");
		}
		return "ElGamal keys not loaded";
	}

	/**
	 * Load RSA key pairs into the system.
	 */
	private String loadRSAKeys() {//TODO: add checks to see if the importer actually returns anything, like with optionslist
		try {
			// Import the keys
			PublicKey bbPubKey = Importer.importRsaPublicKey(Model.getBbRsaPubKeyFilepath());
			PublicKey authPubKey = Importer.importRsaPublicKey(Model.getAuthRsaPubKeyFilepath());
			PrivateKey authPrivKey = Importer.importRsaPrivateKey(Model.getAuthRsaPrivKeyFilepath());
			// Set the keys
			Security.setAuthorityRSAPrivateKey(authPrivKey);
			Security.setAuthorityRSAPublicKey(authPubKey);
			Security.setBulletinBoardRSAPublicKey(bbPubKey);
			
			return "Loaded RSA keys";
		} catch (IOException e) {
			e.printStackTrace();
			return "IO failed";
		}
	}

	/**
	 * Loads the election options into the system. Only when election is not running
	 */
	private String loadElectionOptions() {
		// Load the election options
		ArrayList<ElectionOption> options = Importer.importElectionOptions(Model.getElectionOptionsFile());
		
		if(options != null && options.size() > 0){
			Model.setElectionOptions(options);
		} else {
			return "Failed to import list of election options";
		}
		return "Imported list of election options";
	}

}
