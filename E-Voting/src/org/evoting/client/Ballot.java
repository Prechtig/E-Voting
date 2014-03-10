package org.evoting.client;

import java.util.BitSet;

public class Ballot
{
	private int userId;
	private String password;
	private BitSet vote;
	
	public Ballot(int userId, String password, BitSet vote)
	{
		this.userId = userId;
		this.password = password;
		this.vote = vote;
	}
}
