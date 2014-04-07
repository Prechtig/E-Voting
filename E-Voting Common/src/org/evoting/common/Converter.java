package org.evoting.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Converter {
	
	/**
	 * @param value The byte value to convert
	 * @return The value converted to an int
	 */
	public static int toInt(byte[] value) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.BIG_ENDIAN);
		ByteBuffer wrappedBytes = bb.put(value);
		int i = wrappedBytes.getInt();
		return i;
		//byte[] wrappedValue = ByteBuffer.allocate(4).put(value).array();
		//return ByteBuffer.wrap(wrappedValue).getInt();
	}
	
	/**
	 * @param value The int value to convert
	 * @return The value converted to a byte array
	 */
	public static byte[] toByteArray(int value) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.BIG_ENDIAN);
		return bb.putInt(value).array();
	}
}