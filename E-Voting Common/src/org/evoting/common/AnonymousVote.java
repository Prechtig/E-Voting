package org.evoting.common;

public class AnonymousVote
{
	private byte[][] encryptedVote;
	private int index;
	
	public AnonymousVote(byte[][] encryptedVote, int index)
	{
		this.encryptedVote = encryptedVote;
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}

	/**
	 * @return The encrypted vote
	 */
	public byte[][] getEncryptedVote() {
		return this.encryptedVote;
	}
}
