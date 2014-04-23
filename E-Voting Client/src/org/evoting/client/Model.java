package org.evoting.client;

import java.math.BigInteger;

import jolie.runtime.Value;

import org.evoting.client.exceptions.NoElectionOptionsException;
import org.evoting.common.ElectionOptions;
import org.evoting.common.EncryptedBallot;
import org.evoting.common.Group;
import org.evoting.common.ValueIdentifiers;
import org.evoting.common.exceptions.BadValueException;
import org.evoting.security.Security;


/**
 * Contains data about the electionOptions and logic supporting ballot creation.
 * @author Mark
 *
 */
public class Model
{
	private static ElectionOptions electionOptions;
	// List of electionOption names. The index is equal to the electionOption id.
	//private static List<String> electionOptionNames;
	// The time stamp that marks the electionOption list.
	//private static byte[] electionOptionsTime;
	// The number of electionOptions contained in the electionOptions.
	//private static int numberOfElectionOptions = 0;
	// Group data used for homomorphic encryption.
	private static Group group = Group.getInstance();
	
	/**
	 * Sets the list of available electionOptions.
	 * @param electionOptions The electionOptions available for voting.
	 */
	public static void setElectionOptions(ElectionOptions electionOptions)
	{
		Model.electionOptions = electionOptions;
		//electionOptionNames = electionOptions.getElectionOptions();
		//electionOptionsTime = electionOptions.getTimestamp();
		//numberOfElectionOptions = electionOptionNames.size();
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
		
		group.setGenerator(g);
		group.setModulo(p);
		Security.setElGamalPublicKey(y, p, g, l);
		Security.setRSAPublicKey(rsaPublicKeyValue.byteArrayValue().getBytes());
		
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
