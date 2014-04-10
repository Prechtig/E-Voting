package org.evoting.common;

import java.math.BigInteger;

public class Group
{
	private static Group instance = new Group();
	private BigInteger generator;
	private BigInteger modulo;
	
	public BigInteger raiseGenerator(BigInteger power)
	{
		return generator.modPow(power, modulo);
	}
	
	public int discreteLogarithm(BigInteger value)
	{
		boolean logFound = false;
		int power = 0;
		BigInteger current;
		while(!logFound)
		{
			current = raiseGenerator(BigInteger.valueOf(power));
			if(current.equals(value)) {
				logFound = true;
			} else {
				power++;
			}
		}
		
		return power;
	}
	
	public int discreteLogarithm2(BigInteger value)
	{
		boolean logFound = false;
		int power = 0;
		BigInteger current = BigInteger.valueOf(1);
		while(!logFound)
		{
			if(current.equals(value)) {
				logFound = true;
			} else {
				power++;
				current = multiplyMod(current);
			}
		}
		
		return power;
	}

	private BigInteger multiplyMod(BigInteger value)
	{
		return value.multiply(generator).mod(modulo);
	}
	
	public static Group getInstance()
	{
		return instance;
	}

	public BigInteger getGenerator()
	{
		return generator;
	}

	public void setGenerator(BigInteger generator)
	{
		this.generator = generator;
	}

	public BigInteger getModulo()
	{
		return modulo;
	}

	public void setModulo(BigInteger modulo)
	{
		this.modulo = modulo;
	}
}
