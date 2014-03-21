package org.evoting.security;

import org.bouncycastle.crypto.engines.ElGamalEngine;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;

public class Security implements ISecurities {

	@Override
	public String[] encryptElGamalToBB(String u, String p, ElGamalPrivateKeyParameters pK) {
		byte[] uBytes = u.getBytes();
		byte[] pBytes = p.getBytes();
		
		ElGamalEngine engine = new ElGamalEngine();
		engine.init(true, pK);
		
		uBytes = engine.processBlock(uBytes, 0, uBytes.length);
		pBytes = engine.processBlock(pBytes, 0, pBytes.length);
		
		String encU = new String(uBytes);
		String encP = new String(pBytes);
		
		return new String[]{encU, encP};
	}

	@Override
	public String encryptSignatureToCC(String candidates) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encryptElGamalToA(String b, ElGamalPrivateKeyParameters pK) {
		byte[] bBytes = b.getBytes();
		
		ElGamalEngine engine = new ElGamalEngine();
		engine.init(true, pK);
		
		bBytes = engine.processBlock(bBytes, 0, bBytes.length);
		
		String encb = new String(bBytes);
		
		return encb;
	}

	@Override
	public String encryptResponse(String response) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
