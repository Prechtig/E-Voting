package org.evoting.security;

import java.io.Serializable;
import java.math.BigInteger;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;

public class ElGamalSerilizablePrivateKeyParameters extends ElGamalPrivateKeyParameters implements Serializable {

	private static final long serialVersionUID = -7053998268214971617L;

	public ElGamalSerilizablePrivateKeyParameters(BigInteger arg0, ElGamalParameters arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
}
