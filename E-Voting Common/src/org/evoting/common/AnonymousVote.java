package org.evoting.common;

public class AnonymousVote
{
	private byte[][] encryptedVote;
	
	public AnonymousVote(byte[][] encryptedVote)
	{
		this.encryptedVote = encryptedVote;
	}
	
	/**
	 * @return The encrypted vote
	 */
	public byte[][] getEncryptedVote() {
		return this.encryptedVote;
	}
}
