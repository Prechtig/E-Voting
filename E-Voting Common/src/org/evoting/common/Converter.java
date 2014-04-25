package org.evoting.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Converter {
	
	/**
	 * @param value The byte value to convert
	 * @return The value converted to an int
	 */
	public static int toInt(byte[] value) {
		ByteBuffer buffer = ByteBuffer.allocate(128);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(value);
		buffer.rewind();
		return buffer.getInt();
	}
	
	/**
	 * @param value The int value to convert
	 * @return The value converted to a byte array
	 */
	public static byte[] toByteArray(int value) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.putInt(value).array();
	}
	
	public static byte[] toByteArray(long value) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.putLong(value).array();
	}
	
	public static byte[] convert(AllVotes allVotes)
	{
		throw new UnsupportedOperationException();
	}
}