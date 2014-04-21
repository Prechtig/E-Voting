package org.evoting.testing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jolie.runtime.Value;

import org.evoting.client.Model;
import org.evoting.client.UserInputData;
import org.evoting.client.exceptions.NoElectionOptionsException;
import org.junit.Assert;
import org.junit.Test;

public class ClientController {

	@Test
	public void test() throws ParseException {
		org.evoting.bulletinboard.Controller BBController = new org.evoting.bulletinboard.Controller();
		org.evoting.client.Controller CController = new org.evoting.client.Controller();
		
		Value publicKeys = BBController.getPublicKeys();
		CController.setPublicKeys(publicKeys);
		
		Value encryptedCandidateList = BBController.getElectionOptions();
		CController.setElectionOptions(encryptedCandidateList);
		
		UserInputData userInputData = new UserInputData();
    	userInputData.setElectionOptionId(1);
    	userInputData.setPassword("123");
    	userInputData.setUserId(123);
    	
    	Value status = BBController.getElectionStatus();
    	long endTime = status.getFirstChild("endTime").longValue();
    	Date expectedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2014-06-30 20:00");
    	Assert.assertEquals(expectedDate.getTime(), endTime);
    	
    	Value ballot = null;
    	try {
	    	ballot = Model.getEncryptedBallot(userInputData).getValue();
		} catch (NoElectionOptionsException e) {
			System.out.println("No candidate list has been retrieved from server.");
		}
		
    	if(ballot != null) {
    		BBController.processVote(ballot);
    	}
	}
}