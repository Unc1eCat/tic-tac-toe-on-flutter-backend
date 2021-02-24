package com.unclecat.tictactoe.player;

public class SolidPlayerColor implements PlayerColor
{
	int colorValue;
	String name;
	
	/*
	 * Color value goes like 0xAARRGGBB
	 */
	public SolidPlayerColor(int colorValue)
	{
		super();
		this.colorValue = colorValue;
	}
}
