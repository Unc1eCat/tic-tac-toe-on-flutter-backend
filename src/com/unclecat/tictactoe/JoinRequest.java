package com.unclecat.tictactoe;

import java.net.InetSocketAddress;

public class JoinRequest
{
	public JoinRequest()
	{
		
	}
	
	public JoinRequest(int maxPlayers, InetSocketAddress ip, String name)
	{
		super();
		this.maxPlayers = maxPlayers;
		this.ip = ip;
		this.name = name;
	}
	
	public String name;
	public int maxPlayers;
	public InetSocketAddress ip;
}
