package org.evoting.testing;

import static org.junit.Assert.fail;
import jolie.runtime.Value;

import org.junit.Test;

public class ClientController {

	@Test
	public void test() {
		org.evoting.bulletinboard.Controller BBController = new org.evoting.bulletinboard.Controller();
		org.evoting.client.Controller CController = new org.evoting.client.Controller();
		
		Value publicKeys = BBController.getPublicKeys();
		CController.setPublicKeys(publicKeys);
		
		Value encryptedCandidateList = BBController.getCandidateList();
		Value ballot = CController.setCandidateListAndGetBallot(encryptedCandidateList);
		fail("Not yet implemented");
	}

}
