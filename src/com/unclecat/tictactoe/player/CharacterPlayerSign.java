package com.unclecat.tictactoe.player;

public class CharacterPlayerSign implements PlayerSign
{
	public CharacterPlayerSign(char character)
	{
		super();
		this.character = character;
	}

	char character;

	@Override
	public String toString()
	{
		return Character.toString(character);
	}
}
