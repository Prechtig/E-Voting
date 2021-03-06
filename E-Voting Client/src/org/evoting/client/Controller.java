package org.evoting.client;

import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.embedding.RequestResponse;

import org.evoting.client.exceptions.NoElectionOptionsException;
import org.evoting.common.exceptions.BadValueException;
import org.evoting.common.jolie.ElectionOptions;
import org.evoting.common.jolie.LoginRequest;
import org.evoting.common.jolie.LoginResponse;
import org.evoting.common.jolie.SignedElectionOptions;

/**
 * Contains the methods that the client jolie script calls.
 *
 */
public class Controller extends JavaService
{
	public String getCommand() {
		return ConsoleIO.getCommand();
	}
	
	/**
	 * Sets the list of electionOptions that can be voted for.
	 * @param encryptedElectionOptions The value representation of the electionOption object.
	 */
	public void setElectionOptions(Value encryptedElectionOptionsValue)
	{
		// Interprets the value object as an encrypted election option list.
		SignedElectionOptions signedElectionOptions = new SignedElectionOptions(encryptedElectionOptionsValue);
		// Decrypts the electionOption list.
		ElectionOptions electionOptions = signedElectionOptions.getElectionOptions();
		Model.setElectionOptions(electionOptions);
	}
	
	/**
	 * Sets the public keys for encryption and decryption.
	 * @param publicKeyValues The value representation of the public keys.
	 */
	public void setPublicKeys(Value publicKeyValues)
	{
		Model.setPublicKeys(publicKeyValues);
	}
	
	@RequestResponse
	public Value getLoginInformation()
	{
		LoginRequest userInfo = ConsoleIO.getLoginData();
		Model.setLastLogin(userInfo);
		return userInfo.getValue();
	}
	
	@RequestResponse
	public Boolean validateLoginResponse(Value loginResponse) {
		try {
			new LoginResponse(loginResponse);
		} catch(BadValueException e) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	/**
	 * Gets the voting ballot from user input.
	 * @return Value representation of the ballot.
	 */
    private Value getBallot()
    {
    	UserInputData userInputData = ConsoleIO.getElectionOptionInput(Model.getNumberOfElectionOptions());
		try {
	    	return Model.getEncryptedBallot(userInputData).getValue();
		} catch (NoElectionOptionsException e) {
			System.out.println("No electionOption list has been retrieved from server.");
		}
		return null;
    }
  
    
    /**
     * Sets the electionOption list and requests a ballot from user input based on the electionOption list provided.
     * @param encryptedElectionOptions The electionOption list that the ballot is to be made of.
     * @return A value representation of the encrypted ballot.
     */
    public Value setElectionOptionsAndGetBallot(Value encryptedElectionOptions) {
    	setElectionOptions(encryptedElectionOptions);
    	return getBallot();
    }
    
    public static void main(String[] args) {
	}
}