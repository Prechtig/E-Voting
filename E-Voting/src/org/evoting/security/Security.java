package org.evoting.security;

import org.bouncycastle.crypto.engines.ElGamalEngine;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;

public class Security implements ISecurity {

	@Override
	public String[] encryptElGamalToBB(String u, String p, ElGamalPublicKeyParameters pK) {
		byte[] uBytes = u.getBytes();
		byte[] pBytes = p.getBytes();
		
		ElGamalEngine engine = new ElGamalEngine();
		engine.init(true, pK);
		
		uBytes = engine.processBlock(uBytes, 0, uBytes.length);
		pBytes = engine.processBlock(pBytes, 0, pBytes.length);
		
		String encU = bytes2String(uBytes);
		String encP = bytes2String(pBytes);
		
		return new String[]{encU, encP};
	}
	
	@Override
	public String encryptElGamalToA(String b, ElGamalPublicKeyParameters pK) {
		byte[] bBytes = b.getBytes();
		
		ElGamalEngine engine = new ElGamalEngine();
		engine.init(true, pK);
		
		bBytes = engine.processBlock(bBytes, 0, bBytes.length);
		
		String encb = new String(bBytes);
		
		return encb;
	}

	@Override
	public String encryptSignatureToCC(String candidates) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public String encryptResponse(String response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String decryptElgamalInBB(String m, ElGamalPrivateKeyParameters pK) {
		// TODO Auto-generated method stub
		return null;
	}
	
   private static String bytes2String(byte[] bytes) {
	    StringBuilder string = new StringBuilder();
	    for (byte b : bytes) {
	        String hexString = Integer.toHexString(0x00FF & b);
	        string.append(hexString.length() == 1 ? "0" + hexString : hexString);
	    }
	    return string.toString();
	}
}
