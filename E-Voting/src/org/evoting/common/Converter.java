package org.evoting.common;

import java.nio.ByteBuffer;


public class Converter {
	
	/**
	 * @param value The byte value to convert
	 * @return The value converted to an int
	 */
	public static int toInt(byte[] value) {
		return ByteBuffer.wrap(value).getInt();
	}
	
	/**
	 * @param value The int value to convert
	 * @return The value converted to a byte array
	 */
	public static byte[] toByteArray(int value) {
		return ByteBuffer.allocate(4).putInt(value).array();
	}
}