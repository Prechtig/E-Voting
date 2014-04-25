package org.evoting.client;

import java.math.BigInteger;

import jolie.runtime.Value;

import org.evoting.client.exceptions.NoElectionOptionsException;
import org.evoting.common.ElectionOptions;
import org.evoting.common.EncryptedBallot;
import org.evoting.common.ValueIdentifiers;
import org.evoting.common.exceptions.BadValueException;
import org.evoting.security.Security;


/**
 * Contains data about the election options and logic supporting ballot creation.
 * @author Mark
 *
 */
public class Model
{
	// The list of candidate and parties available for voting.
	private static ElectionOptions electionOptions;
	
	/**
	 * Sets the list of available electionOptions.
	 * @param electionOptions The electionOptions available for voting.
	 */
	public static void setElectionOptions(ElectionOptions electionOptions)
	{
		Model.electionOptions = electionOptions;
	}
	
	/**
	 * Sets the public keys for signing the ballot and decrypting the electionOption list.
	 * @param publicKeyValues
	 */
	public static void setPublicKeys(Value publicKeyValues)
	{
		// Checks whether the value object has the required fields.
		if(!publicKeyValues.hasChildren(ValueIdentifiers.getElGamalPublicKey()) ||
			!publicKeyValues.hasChildren(ValueIdentifiers.getRsaPublicKey()) ||
			!publicKeyValues.getFirstChild(ValueIdentifiers.getElGamalPublicKey()).hasChildren(ValueIdentifiers.getY()) ||
			!publicKeyValues.getFirstChild(ValueIdentifiers.getElGamalPublicKey()).getFirstChild(ValueIdentifiers.getParameters()).hasChildren(ValueIdentifiers.getP()) ||
			!publicKeyValues.getFirstChild(ValueIdentifiers.getElGamalPublicKey()).getFirstChild(ValueIdentifiers.getParameters()).hasChildren(ValueIdentifiers.getG()) ||
			!publicKeyValues.getFirstChild(ValueIdentifiers.getElGamalPublicKey()).getFirstChild(ValueIdentifiers.getParameters()).hasChildren(ValueIdentifiers.getL())) {
				throw new BadValueException();
		}
		
		Value elGamalPublicKey = publicKeyValues.getFirstChild(ValueIdentifiers.getElGamalPublicKey());
		Value parameters = elGamalPublicKey.getFirstChild(ValueIdentifiers.getParameters());
		Value rsaPublicKeyValue = publicKeyValues.getFirstChild(ValueIdentifiers.getRsaPublicKey());
		BigInteger y = new BigInteger(elGamalPublicKey.getFirstChild(ValueIdentifiers.getY()).strValue());
		BigInteger p = new BigInteger(parameters.getFirstChild(ValueIdentifiers.getP()).strValue());
		BigInteger g = new BigInteger(parameters.getFirstChild(ValueIdentifiers.getG()).strValue());
		int l = Integer.parseInt(parameters.getFirstChild(ValueIdentifiers.getL()).strValue());
		
		Security.setElGamalPublicKey(y, p, g, l);
		Security.setBulletinBoardRSAPublicKey(rsaPublicKeyValue.byteArrayValue().getBytes());
	}
	
	
	/**
	 * Gets the number of electionOptions available for voting. Returns zero if setElectionOptions has not been called.
	 * @return The number of electionOptions available for voting.
	 */
	public static int getNumberOfElectionOptions()
	{
		return electionOptions.getElectionOptions().size();
	}
	
	
	/**
	 * Creates an encrypted ballot from the user input.
	 * @param userInputData The data that the user submitted to cast his vote.
	 * @return An encrypted ballot.
	 * @throws NoElectionOptionsException
	 */
	public static EncryptedBallot getEncryptedBallot(UserInputData userInputData) throws NoElectionOptionsException
	{
		if(Model.electionOptions == null) {
			throw new NoElectionOptionsException();
		}
		int[] votes = getVoteFromElectionOptionId(userInputData.getElectionOptionId());
		return new EncryptedBallot(userInputData.getUserId(), userInputData.getPassword(), electionOptions.getElectionId(), votes);
	}
	
	/**
	 * Returns a boolean array with indexes representing each electionOption id and value representing whether or not he/she is voted for.
	 * @param electionOptionId The index that is true in the return array.
	 * @return Boolean array with one value set to true.
	 */
	private static int[] getVoteFromElectionOptionId(int electionOptionId)
	{
		int[] result = new int[getNumberOfElectionOptions()];
		result[electionOptionId] = 1;
		return result;
	}
}
