package com.unclecat.tictactoe.player;

import java.net.InetSocketAddress;

/*
 * Represents a player. Manages connection to it.
 * 
 * For now the server does not care about representing the sign and the color at the server, so it stores only name
 * that will be send to clients. The clients will itself understand what they need to render depending on the name.
 * Later I will store the data about the sign and the color(later "paint") on the server as well and build hierarchy
 * of sign and color types
 */
public class Player 
{
	protected PlayerColors color;
	protected PlayerSigns sign;
	protected String name;
	protected transient InetSocketAddress ip;
	
	public Player(PlayerColors color, PlayerSigns sign, InetSocketAddress ip, String name)
	{
		super();
		this.name = name;
		this.color = color;
		this.sign = sign;
		this.ip = ip;
	}
	
	public PlayerColors getColor()
	{
		return color;
	}
	public PlayerSigns getSign()
	{
		return sign;
	}
	public InetSocketAddress getIp()
	{
		return ip;
	}

	public String getName()
	{
		return name;
	}
}
