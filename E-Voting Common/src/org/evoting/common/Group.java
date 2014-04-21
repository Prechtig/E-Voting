package org.evoting.common;

import java.math.BigInteger;

/**
 * Contains data defining a cyclic group, and methods for generating and finding group elements.
 * @author Mark
 *
 */
public class Group
{
	private static Group instance = new Group();
	private BigInteger generator;
	private BigInteger modulo;
	
	/**
	 * Raises the group generator to the power of the input provided and applies the modulo operator with the modulo of the group on the result.
	 * @param power The power to raise the generator to.
	 * @return A group element.
	 */
	public BigInteger raiseGenerator(long power)
	{
		return generator.modPow(BigInteger.valueOf(power), modulo);
	}
	
	/**
	 * Applies the logarithm operator to a value, assuming that the value is a member of the group. This is done by exhaustive search.
	 * @param value The value that is a member of the group.
	 * @return The power which the generator is raised to, to get the value provided.
	 */
	public long discreteLogarithm(BigInteger value)
	{
		boolean logFound = false;
		long power = 0;
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
	
	/**
	 * Applies the logarithm operator to a value, assuming that the value is a member of the group. This is done by exhaustive search in the range provided.
	 * The range is defined as from(including) searchRadius subtracted from expectedResult to(including) expectedResult added to searchRadius.
	 * If the range does not contain the result, the result is obtained by exhaustive search in the entire range of non-negative integers.
	 * @param value The value that is a member of the group.
	 * @param expectedResult The median value of the search range.
	 * @param searchRadius The offset from the expectedResult, from which the range is defined. Cannot be greater than expectedResult or less than or equal to zero.
	 * @return The power which the generator is raised to, to get the value provided.
	 */
	public long discreteLogarithmFromExpectedRange(BigInteger value, long expectedResult, long searchRadius)
	{   
		if(expectedResult - searchRadius < 0 || searchRadius == 0) {
			throw new IllegalArgumentException();
		}
		
		long start = expectedResult - searchRadius;
		long end = expectedResult + searchRadius;
		boolean logFound = false;
		long power = start;
		BigInteger current = raiseGenerator(power);
		
		while(!logFound && power <= end)
		{
			if(current.equals(value)) {
				logFound = true;
			} else {
				power++;
				current = multiplyMod(current);
			}
		}
		
		if(logFound) {
			return power;
		} else {
			return discreteLogarithm(value);
		}
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
