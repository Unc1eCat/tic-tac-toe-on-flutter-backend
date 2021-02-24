package com.unclecat.tictactoe.player;

public enum PlayerColors
{
	RED("red"),
	PURPLE("purple"),
	BLUE("blue"),
	AMBER("amber"),
	GREEN("green"),
	INDIGO("indigo"),
	PINK("pink"),
	ORANGE("orange");
	
	public final String name;
	
	PlayerColors(String name)
	{
		this.name = name;
	}
}
