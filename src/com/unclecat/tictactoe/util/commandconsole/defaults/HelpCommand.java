package com.unclecat.tictactoe.util.commandconsole.defaults;

import java.util.List;

import com.unclecat.tictactoe.util.commandconsole.Argument;
import com.unclecat.tictactoe.util.commandconsole.ArgumentHandler;
import com.unclecat.tictactoe.util.commandconsole.ArgumentResult;
import com.unclecat.tictactoe.util.commandconsole.Command;
import com.unclecat.tictactoe.util.commandconsole.CommandHandler;

public class HelpCommand extends Command
{
	public final Iterable<CommandHandler> availableCommands;
	public int descriptionMaxLineLength = 100;
	public int indentation = 4;

	/*
	 * The [[availableCommands]] is mutable. So passing the argument first and
	 * filling it with commands later will work
	 */
	public HelpCommand(Iterable<CommandHandler> availableCommands)
	{
		super("help",
				"Shows the list of commands with their descriptions. Pass a command name as the value to the command to see description only for the specified command, in this case all arguments are shown.",
				null, new Argument[]
				{ new Argument("arguments", "a", "Wheather to describe arguments of the commands.", false)
				{
					public Object handle(String value, CommandHandler command)
					{
						return null;
					}
				}, new Argument("subcommands", "s", "Wheather to describe subcommands of the commands.", false)
				{
					public Object handle(String value, CommandHandler command)
					{
						return null;
					}
				} });

		this.availableCommands = availableCommands;
	}

	public HelpCommand(String name, String description, CommandHandler[] commands, ArgumentHandler[] arguments,
			Iterable<CommandHandler> availableCommands, int descriptionMaxLineLength, int indentation)
	{
		this(availableCommands);

		this.descriptionMaxLineLength = descriptionMaxLineLength;
		this.indentation = indentation;
	}

	@Override
	public void handle(List<ArgumentResult> arguments, List<String> values)
	{
		boolean describeArguments = getArgumentByShortName("a", arguments) != null;
		boolean describeSubcommands = getArgumentByShortName("s", arguments) != null;

		if (values.size() > 0)
		{
			boolean commandDoesntExist = true;
			
			for (CommandHandler i : availableCommands)
			{
				if (i.getName().contentEquals(values.get(0)))
				{
					describeCommandFully(0, "", i, true, describeSubcommands);
					commandDoesntExist = false;
					break;
				}
			}
			
			if (commandDoesntExist)
			{
				System.err.println("Command \"" + values.get(0) + "\" doesn't exist. Use \"help\" to see the list of available commands.");
			}
		} else
		{
			System.out.println("The list describing available commands:");

			for (CommandHandler i : availableCommands)
			{
				describeCommandFully(0, "", i, describeArguments, describeSubcommands);
				System.out.println();
			}
		}
	}

	public void describeCommandFully(int indentation, String commandPathText, CommandHandler command,
			boolean describeArguments, boolean describeSubcommands)
	{
		int indent = describeCommand(indentation, "", command);

		if (describeArguments)
		{
			System.out.println();

			if (command.getAllArguments() != null && command.getAllArguments().size() > 0)
			{
				System.out.print(repeatString(" ", indent) + "-> Arguments for this command:\n");

				for (int a = 0; a < command.getAllArguments().size(); a++)
				{
					describeArgument(indent + this.indentation / 2, command.getAllArguments().get(a));
					if (a != command.getAllArguments().size() - 1)
					{
						System.out.println();
					}
				}
			} else
			{
				System.out.print(repeatString(" ", indent) + "-> The command has no arguments.");
			}
		} else if (command.getAllArguments() != null && command.getAllArguments().size() > 0)
		{
			System.out.println();

			System.out.print(repeatString(" ", indent) + "-> The command has " + command.getAllArguments().size()
					+ " arguments. Use \"--arguments\" or \"-a\" to show them.");
		}

		if (describeSubcommands)
		{
			System.out.println();

			if (command.getCommands() != null && command.getCommands().size() > 0)
			{
				System.out.print(repeatString(" ", indent) + "-> Subcommands of this command:\n");

				for (int s = 0; s < command.getCommands().size(); s++)
				{
					describeCommandFully(indentation + this.indentation,
							commandPathText + (commandPathText.length() <= 0 ? "" : " / ") + command.getName(),
							command.getCommands().values().toArray(new CommandHandler[0])[s], describeArguments,
							describeSubcommands);
					if (s != command.getCommands().size() - 1)
					{
						System.out.println();
					}
				}
			} else
			{
				System.out.print(repeatString(" ", indent) + "-> The command has no subcommands.");
			}
		} else if (command.getCommands() != null && command.getCommands().size() > 0)
		{
			System.out.println();

			System.out.print(repeatString(" ", indent) + "-> The command has " + command.getAllArguments().size()
					+ " subcommands. Use \"--subcommands\" or \"-s\" to show them.");
		}
	}

	public int describeArgument(int indentation, ArgumentHandler argument)
	{
		String descr = argument.getDescription();
		String fullName = argument.getFullName();
		String shortName = argument.getShortName();

		int indent = indentation + (fullName == null ? 0 : fullName.length() + (argument.hasValue() ? 7 : 0))
				+ (shortName == null ? 0 : 2 + shortName.length() + (argument.hasValue() ? 7 : 0) + this.indentation);

		System.out.print(
				repeatString(" ", indentation) + (fullName == null ? "" : fullName + (argument.hasValue() ? "<value>" : ""))
						+ (shortName == null ? "" : ", " + shortName + (argument.hasValue() ? "<value>" : "")));
		System.out.print(repeatString(" ", this.indentation));

		for (int c = 0; c < descr.length(); c++)
		{
			System.out.print(descr.charAt(c));

			if (c % descriptionMaxLineLength == descriptionMaxLineLength - 1)
			{
				System.out.print("\n" + repeatString(" ", indent));
			}
		}

		return indent;
	}

	/*
	 * Returns indentation
	 */
	public int describeCommand(int indentation, String commandPathText, CommandHandler command)
	{
		String descr = command.getDescription();

		System.out.println();
		System.out.print(repeatString(" ", indentation) + commandPathText + (commandPathText.length() <= 0 ? "" : " >> ")
				+ command.getName());
		System.out.print(repeatString(" ", this.indentation));

		int indent = indentation + commandPathText.length() + (commandPathText.length() <= 0 ? 0 : 4)
				+ command.getName().length() + this.indentation;

		System.out.print("-> ");

		for (int c = 0; c < descr.length(); c++)
		{
			System.out.print(descr.charAt(c));

			if (c % descriptionMaxLineLength == descriptionMaxLineLength - 1)
			{
				System.out.print("\n" + repeatString(" ", indent + this.indentation / 2));
			}
		}

		return indent;
	}

	public static String repeatString(String value, int times)
	{
		StringBuilder sb = new StringBuilder();

		for (int j = 0; j < times; j++)
		{
			sb.append(value);
		}

		return sb.toString();
	}
}
