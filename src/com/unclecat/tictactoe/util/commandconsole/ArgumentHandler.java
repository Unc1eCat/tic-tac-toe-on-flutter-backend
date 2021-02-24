package com.unclecat.tictactoe.util.commandconsole;

public interface ArgumentHandler
{
	public abstract String getFullName();
	
	public abstract String getShortName();
	
	public abstract String getDescription();
	
	public abstract boolean hasValue();
	
	public default Object handle(String value, CommandHandler command)
	{
		return value;
	}
}
