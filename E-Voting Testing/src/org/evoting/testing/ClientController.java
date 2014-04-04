package org.evoting.testing;

import jolie.runtime.Value;

import org.evoting.client.Model;
import org.evoting.client.UserInputData;
import org.evoting.client.exceptions.NoCandidateListException;
import org.junit.Test;

public class ClientController {

	@Test
	public void test() {
		org.evoting.bulletinboard.Controller BBController = new org.evoting.bulletinboard.Controller();
		org.evoting.client.Controller CController = new org.evoting.client.Controller();
		
		Value publicKeys = BBController.getPublicKeys();
		CController.setPublicKeys(publicKeys);
		
		Value encryptedCandidateList = BBController.getCandidateList();
		CController.setCandidateList(encryptedCandidateList);
		
		UserInputData userInputData = new UserInputData();
    	userInputData.setCandidateId(1);
    	userInputData.setPassword("123");
    	userInputData.setUserId(123);
    	
    	Value ballot = null;
    	try {
	    	ballot = Model.getEncryptedBallot(userInputData).getValue();
		} catch (NoCandidateListException e) {
			System.out.println("No candidate list has been retrieved from server.");
		}
		
    	if(ballot != null) {
    		BBController.vote(ballot);
    	}
	}
}