package org.evoting.authority.commands;

import java.io.IOException;

import org.evoting.authority.Model;
import org.evoting.common.utility.Exporter;
import org.evoting.security.Security;

import jolie.runtime.JavaService;

public class Generate extends Command {
	public static final String KEYWORD = "generate";
	private static String invalidArguments = "Invalid arguments.";

	public Generate(String[] args) {
		super(args);
	}

	public String execute(JavaService js) {
		if (args.length < 2) {
			return invalidArguments;
		}

		switch (args[1]) {
		// Generate RSA keypair
		case "rsa":
			return generateRsaKeys();
			// Generate ElGamal keypair
		case "elgamal":
			return generateElGamalKeys();
			// Generate both RSA and ElGamal key pair
		case "both":
			StringBuilder sb = new StringBuilder();
			sb.append(generateRsaKeys());
			sb.append("\n");
			sb.append(generateElGamalKeys());
			return sb.toString();
		default:
			return invalidArguments;
		}
	}

	/**
	 * Generates and exports two new set of RAS keys
	 */
	private String generateRsaKeys() {
		// Generate RSA keys
		Security.generateRSAKeys();
		try {
			// Export the keys
			Exporter.exportRsaKey(Model.getAuthRsaPrivKeyFilepath(), Security.getAuthorityRSAPrivateKey());
			Exporter.exportRsaKey(Model.getAuthRsaPubKeyFilepath(), Security.getAuthorityRSAPublicKey());
			Exporter.exportRsaKey(Model.getBbRsaPrivKeyFilepath(), Security.getBulletinBoardRSAPrivateKey());
			Exporter.exportRsaKey(Model.getBbRsaPubKeyFilepath(), Security.getBulletinBoardRSAPublicKey());

			return "Generated and exported new RSA keys";
		} catch (IOException e) {
			e.printStackTrace();
			return "Something went wrong. Try again";
		}
	}

	/**
	 * Generates and exports a new set of ElGamal keys and exports them
	 */
	private String generateElGamalKeys() {
		// Export keys
		Security.generateElGamalKeys(true);

		try {
			Exporter.exportElGamalPrivateKeyParameters(Security.getElgamalPrivateKey(), Model.getElGamalPrivateKeyFile());
			Exporter.exportElGamalPublicKeyParameters(Security.getElgamalPublicKey(), Model.getElGamalPublicKeyFile());
			return "Generated and exported new ElGamal keys";
		} catch (IOException e) {
			e.printStackTrace();
			return "Something went wrong. Try again";
		}
	}
}
