package org.evoting.bulletinboard;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.ValueVector;
import jolie.runtime.embedding.RequestResponse;

import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.evoting.bulletinboard.exceptions.ElectionNotStartedException;
import org.evoting.bulletinboard.exceptions.InvalidUserInformationException;
import org.evoting.common.exceptions.BadValueException;
import org.evoting.common.exceptions.CorruptDataException;
import org.evoting.common.jolie.AnonymousVoteList;
import org.evoting.common.jolie.Ballot;
import org.evoting.common.jolie.ElectionStart;
import org.evoting.common.jolie.EncryptedBallot;
import org.evoting.common.jolie.LoginRequest;
import org.evoting.common.jolie.LoginResponse;
import org.evoting.common.jolie.SignedElectionOptions;
import org.evoting.common.jolie.ValueIdentifiers;
import org.evoting.common.utility.Importer;
import org.evoting.database.entities.ElectionOption;
import org.evoting.security.KeyType;
import org.evoting.security.Security;

public class Controller extends JavaService {
	private static ElectionStart electionStart;
	private static Value publicKeys = null;

	@RequestResponse
	public Boolean sendElectionOptionList(Value electionOptions) {
		validate(electionOptions.getFirstChild(ValueIdentifiers.getValidator()));

		ValueVector options = electionOptions.getChildren(ValueIdentifiers.getElectionOptions());

		ElectionOption[] arr = new ElectionOption[options.size()];
		for (int i = 0; i < options.size(); i++) {
			Value currentOption = options.get(i);

			int id = currentOption.getFirstChild(ValueIdentifiers.getId()).intValue();
			String name = currentOption.getFirstChild(ValueIdentifiers.getName()).strValue();
			int partyId = currentOption.getFirstChild(ValueIdentifiers.getPartyId()).intValue();

			arr[id] = new ElectionOption(id, name, partyId);
		}
		Model.setElectionOptions(arr);
		return Boolean.TRUE;
	}

	@RequestResponse
	public Boolean startElection(Value electionStartValue) {
		electionStart = new ElectionStart(electionStartValue);
		return Boolean.TRUE;
	}

	@RequestResponse
	/**
	 * Processes a vote, effectively saving it in the persistent storage
	 * if the userId + passwordHash matches, and the ballot is valid
	 * @param valueEncryptedBallot The vote to process, as a value from Jolie
	 * @return true if the vote is registered, otherwise false
	 */
	public Boolean processVote(Value encryptedBallot) {
		if (!electionIsRunning()) {
			throw new ElectionNotStartedException();
		}
		Ballot ballot = new EncryptedBallot(encryptedBallot).getBallot();

		String userId = ballot.getUserId();
		byte[][] encryptedVote = ballot.getVote();

		Model.persistVote(userId, encryptedVote);

		return Boolean.TRUE;
	}

	@RequestResponse
	public Value login(Value userInformation) {
		LoginRequest loginRequest = new LoginRequest(userInformation);
		try {
			Model.validateUser(loginRequest);
		} catch (InvalidUserInformationException e) {
			return new LoginResponse(false).getValue();
		}
		return new LoginResponse(true).getValue();
	}

	@RequestResponse
	/**
	 * @return Returns the electionOptionlist as a Value, used in Jolie 
	 */
	public Value getElectionOptions() {
		if (!electionIsRunning()) {
			throw new ElectionNotStartedException();
		}

		List<ElectionOption> electionOptions = Model.getEncryptedElectionOptions();
		SignedElectionOptions signedElectionOptions = new SignedElectionOptions(electionOptions, electionStart.getEndTime());
		
		return signedElectionOptions.getValue();
	}

	@RequestResponse
	/**
	 * @return Returns the elgamal + rsa public key
	 */
	public Value getPublicKeys() {
		if (!electionIsRunning()) {
			throw new ElectionNotStartedException();
		}
		if(publicKeys == null) {
			publicKeys = Model.getPublicKeysValue();
		}
		return publicKeys;
	}

	@RequestResponse
	/**
	 * @return All votes in the database
	 */
	public Value getAllVotes() {
		if (!electionIsRunning()) {
			throw new ElectionNotStartedException();
		}

		AnonymousVoteList allVotes = Model.getAllVotes();
		return allVotes.toValue();
	}

	@RequestResponse
	/**
	 * @param validator
	 * @return
	 */
	public Value getAllVotesAuthority(Value validator) {
		if (!electionIsRunning()) {
			throw new ElectionNotStartedException();
		}
		validate(validator);

		AnonymousVoteList allVotesAuthority = Model.getAllVotesAuthority();
		return allVotesAuthority.toValue();
	}

	private boolean electionIsRunning() {
		Date currentTime = new Date(System.currentTimeMillis());
		return currentTime.after(electionStart.getStartTime()) && currentTime.before(electionStart.getEndTime());
	}

	private void validate(Value validation) {
		if(!validation.hasChildren(ValueIdentifiers.getMessage()) ||
		   !validation.hasChildren(ValueIdentifiers.getSignature())) {
			throw new BadValueException();
		}
		
		String message = validation.getFirstChild(ValueIdentifiers.getMessage()).strValue();
		byte[] signature = validation.getFirstChild(ValueIdentifiers.getSignature()).byteArrayValue().getBytes();
		String hashedMessage = Security.hash(message);
		if (!hashedMessage.equals(new String(Security.decryptRSA(signature, Security.getAuthorityRSAPublicKey())))) {
			throw new CorruptDataException("Validation error");
		}
	}

	/**
	 * Load the ElGamal authority public key
	 */
	public Boolean loadElGamalKey() {
		Console console = System.console();
		String message = "Enter the location of the Authority ElGamal public key";
		String input;
		ElGamalPublicKeyParameters pubKey = null;
		while (true) {
			try {
				System.out.println(message);
				input = console.readLine();
				pubKey = Importer.importElGamalPublicKeyParameters(input);
				break;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Invalid file location.");
			} catch (CorruptDataException e) {
				e.printStackTrace();
				System.out.println("The file does not have the correct format.");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Something went wrong.");
			} finally {
				if (pubKey != null) {
					Security.setElGamalPublicKey(pubKey);
					return Boolean.TRUE;
				}
				System.out.println("Try again y/n?");
				input = console.readLine();
				if (!"y".equals(input.toLowerCase())) {
					break;
				}
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Load the RSA keys for the bulletinboard
	 */
	public Boolean loadRSAKeys() {
		String message = "Enter the location of the BulletinBoard RSA private key";
		PrivateKey bbPrivKey = (PrivateKey) importRsaKey(message, KeyType.PRIVATE);

		message = "Enter the location of the BulletinBoard RSA public key";
		PublicKey bbPubKey = (PublicKey) importRsaKey(message, KeyType.PUBLIC);

		message = "Enter the location of the Authority RSA public key";
		PublicKey authPubKey = (PublicKey) importRsaKey(message, KeyType.PUBLIC);

		if (bbPrivKey != null && bbPubKey != null && authPubKey != null) {
			Security.setBulletinBoardRSAPrivateKey(bbPrivKey);
			Security.setBulletinBoardRSAPublicKey(bbPubKey);
			Security.setAuthorityRSAPublicKey(authPubKey);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private Key importRsaKey(String message, KeyType keytype) {
		Console console = System.console();
		String input;
		while (true) {
			try {
				System.out.println(message);
				input = console.readLine();
				switch (keytype) {
				case PRIVATE:
					return Importer.importRsaPrivateKey(input);
				case PUBLIC:
					return Importer.importRsaPublicKey(input);
				}
			} catch (IOException e) {
				System.out.println("Invalid file location. Try again y/n?");
				input = console.readLine();
				if (!"y".equals(input.toLowerCase())) {
					return null;
				}
			} catch (InvalidKeySpecException e) {
				System.out.println("Invalid key file");
			}
		}
	}

	public static void main(String[] args) {
	}
}