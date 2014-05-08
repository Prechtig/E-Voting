package org.evoting.testing;

import org.evoting.security.Security;
import org.junit.Assert;
import org.junit.Test;

public class AuthenticationTest {
	@Test
	public void testAuthentication() {
		byte[] bytes = {123, -12, 88, 41};
		Security.generateRSAKeys();
		byte[] signature = Security.sign(bytes, Security.getBulletinBoardRSAPrivateKey());
		Assert.assertEquals(true, Security.authenticate(bytes, signature, Security.getBulletinBoardRSAPublicKey()));
	}
}
