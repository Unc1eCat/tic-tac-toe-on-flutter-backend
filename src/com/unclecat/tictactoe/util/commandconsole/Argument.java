package com.unclecat.tictactoe.util.commandconsole;

public abstract class Argument implements ArgumentHandler
{
	String fullName;
	String shortName;
	String description;
	boolean hasValue;

	public Argument(String fullName, String shortName, String description, boolean hasValue)
	{
		this.fullName = fullName;
		this.shortName = shortName;
		this.description = description;
		this.hasValue = hasValue;
	}

	@Override
	public String getFullName()
	{
		return fullName;
	}

	@Override
	public String getShortName()
	{
		return shortName;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public boolean hasValue()
	{
		return hasValue;
	}
	
	@Override
	public abstract Object handle(String value, CommandHandler command);
}
