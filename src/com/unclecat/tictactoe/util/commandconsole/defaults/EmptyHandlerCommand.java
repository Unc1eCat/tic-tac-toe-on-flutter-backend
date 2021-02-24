package com.unclecat.tictactoe.util.commandconsole.defaults;

import java.util.Collection;
import java.util.List;

import com.unclecat.tictactoe.util.commandconsole.ArgumentHandler;
import com.unclecat.tictactoe.util.commandconsole.ArgumentResult;
import com.unclecat.tictactoe.util.commandconsole.Command;
import com.unclecat.tictactoe.util.commandconsole.CommandHandler;

public class EmptyHandlerCommand extends Command
{

	public EmptyHandlerCommand(String name, String description, Collection<CommandHandler> commands,
			Collection<ArgumentHandler> arguments)
	{
		super(name, description, commands, arguments);
	}

	public EmptyHandlerCommand(String name, String description, CommandHandler[] commands, ArgumentHandler[] arguments)
	{
		super(name, description, commands, arguments);
	}

	@Override
	public void handle(List<ArgumentResult> arguments, List<String> values)
	{
	}
}
