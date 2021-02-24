package com.unclecat.tictactoe.player;

public enum PlayerSigns
{
	X("x"),
	O("o"),
	y("y"),
	SHARP("#"),
	G("g"),
	EXCLAMATION_SIGN("!"),
	DOLLAR("$"),
	AT("@");
	
	public final String name;
	
	PlayerSigns(String name)
	{
		this.name = name;
		
	}
}
