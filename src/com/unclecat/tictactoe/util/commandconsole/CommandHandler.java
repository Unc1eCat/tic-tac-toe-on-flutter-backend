package com.unclecat.tictactoe.util.commandconsole;

import java.util.List;
import java.util.Map;

public interface CommandHandler
{
	public String getName();
	
	public String getDescription();
	
	// TODO: Make it null safe
	public Map<String, ArgumentHandler> getFullArguments();
	
	// TODO: Make it null safe
	public Map<String, ArgumentHandler> getShortArguments();
	
	public List<ArgumentHandler> getAllArguments();
	
	// TODO: Make it null safe
	public Map<String, CommandHandler> getCommands();
	
	public void handle(List<ArgumentResult> arguments, List<String> values);
}
