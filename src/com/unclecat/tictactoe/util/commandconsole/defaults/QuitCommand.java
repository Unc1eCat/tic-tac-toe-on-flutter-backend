package com.unclecat.tictactoe.util.commandconsole.defaults;

import java.util.List;

import com.unclecat.tictactoe.util.commandconsole.ArgumentResult;
import com.unclecat.tictactoe.util.commandconsole.Command;
import com.unclecat.tictactoe.util.commandconsole.CommandHandler;

public class QuitCommand extends Command
{
	public QuitCommand()
	{
		super("quit", "Closes the application. You can pass exit code to it as the command's value.", (List<CommandHandler>)null, null);
	}

	@Override
	public void handle(List<ArgumentResult> arguments, List<String> values)
	{
		int exitCode = 0;
		
		if (values.size() > 0)
		{
			try
			{
				exitCode = Integer.parseInt(values.get(0));		
			} catch (Exception e)
			{
				System.out.println("Wrong exit code format: \"" + values.get(0) + "\". Not exiting.");
				return;
			}
		}
		
		System.out.print("Exited with code " + exitCode);
		System.exit(exitCode);
	}
}
