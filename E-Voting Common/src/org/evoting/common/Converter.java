package org.evoting.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.evoting.security.Security;


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
	
	public static byte[] convert(AllVotesAuthority allVotesAuthority)
	{
		ArrayList<byte[]> byteList = new ArrayList<byte[]>();
		List<AnonymousVote> votes = allVotesAuthority.getListOfVotes();
		for(AnonymousVote v : votes) {
			for(int i = 0; i < v.getEncryptedVote().length; i++) {
				byteList.add(v.getEncryptedVote()[i]);
				byteList.add(toByteArray(i));
			}
		}
		byte[][] bytes = new byte[byteList.size()][];
		byte[] result = Security.concatenateByteArrays(byteList.toArray(bytes));
		return result;
	}
	
}