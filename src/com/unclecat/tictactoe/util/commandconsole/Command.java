package com.unclecat.tictactoe.util.commandconsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 
	Example: 
	
	command subCommand subCommandOfThatSubCommand -shortArgumentWithValue theValue 
	--fullArgumentWithValue theValue -shortArgumentWithoutValue --fullArgumentWithoutValue
	-shortArgumentWithValue=value --fullArgumentWithValue=value
*/
public abstract class Command implements CommandHandler
{
	Map<String, CommandHandler> commands;
	Map<String, ArgumentHandler> argumentsFull;
	Map<String, ArgumentHandler> argumentsShort;
	
	List<ArgumentHandler> allArguments;

	String name;
	String description;

	public Command(String name, String description, CommandHandler[] commands, ArgumentHandler[] arguments)
	{
		this.name = name;
		this.description = description;
		this.allArguments = arguments == null ? null : Arrays.asList(arguments);
		this.argumentsFull = new HashMap<String, ArgumentHandler>();
		this.argumentsShort = new HashMap<String, ArgumentHandler>();
		this.commands = new HashMap<String, CommandHandler>();
			
		if (commands != null)
		{
			for (CommandHandler i : commands)
			{
				this.commands.put(i.getName(), i);
			}
		}

		if (arguments != null)
		{
			for (ArgumentHandler i : arguments)
			{
				String fullName = i.getFullName();
				String shortName = i.getShortName();

				if (fullName != null && !fullName.isEmpty())
				{
					argumentsFull.put(fullName, i);
				}

				if (shortName != null && !shortName.isEmpty())
				{
					argumentsShort.put(shortName, i);
				}
			}
		}
	}

	public Command(String name, String description, Collection<CommandHandler> commands,
			Collection<ArgumentHandler> arguments)
	{
		this(name, description, commands == null ? null : commands.toArray(new CommandHandler[0]), arguments == null ? null : (arguments.toArray(new ArgumentHandler[0])));
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public List<ArgumentHandler> getAllArguments()
	{
		return allArguments;
	}
	
	@Override
	public Map<String, ArgumentHandler> getFullArguments()
	{
		return argumentsFull;
	}

	@Override
	public Map<String, ArgumentHandler> getShortArguments()
	{
		return argumentsShort;
	}

	@Override
	public Map<String, CommandHandler> getCommands()
	{
		return commands;
	}
	
	public void addCommand(CommandHandler command)
	{
		commands.put(command.getName(), command);
	}
	
	public void addArgument(ArgumentHandler argument)
	{
		String fullName = argument.getFullName();
		String shortName = argument.getShortName();
		
		if (fullName != null)
		{
			argumentsFull.put(fullName, argument);
		}
		if (shortName != null)
		{
			argumentsShort.put(shortName, argument);
		}
	}
	
	public void removeCommand(String commandName)
	{
		commands.remove(commandName);
	}
	
	public void removeArgument(String anyNameOfTheArgument)
	{
		ArgumentHandler argument = null;
		
		argument = argumentsFull.get(anyNameOfTheArgument);
		if (argument == null)
		{
			argument = argumentsShort.get(anyNameOfTheArgument);
		}
		
		argumentsFull.remove(argument.getFullName());
		argumentsShort.remove(argument.getShortName());
	}

	@Override
	public abstract void handle(List<ArgumentResult> arguments, List<String> values);

	public void read(InputStream inputStream)
	{
		BufferedReader ir = new BufferedReader(new InputStreamReader(inputStream));

		while (true)
		{
			String line;

			try
			{
				line = ir.readLine();
			} catch (IOException e)
			{
				System.out.println("Failed to read a line from input stream");
				e.printStackTrace();
				continue;
			}

			List<String> elements = splitString(line);
			CommandHandler head = this;
			int i = 0;
			List<ArgumentResult> argumentResults = new ArrayList<ArgumentResult>();
			List<String> commandValues = new ArrayList<String>();

			for (; i < elements.size(); i++) // Handle commands stack
			{
				String e = elements.get(i);

				if (e.startsWith("-"))
				{
					break;
				}

				CommandHandler c = head.getCommands().get(e);

				if (c == null)
				{
					break;
				} else
				{
					head = c;
				}
			}
			
			if (head == this)
			{
				System.err.println("The command is unknown or wasn't specified.");
			}

			for (; i < elements.size(); i++) // Handle arguments and values
			{
				String e = elements.get(i);

				if (e.startsWith("-")) // If the next element is an argument
				{
					int equalSign = e.indexOf("=");
					String argument = equalSign == -1 ? e : e.substring(0, equalSign);

					ArgumentHandler argHandler = argument.charAt(1) == '-'
							? head.getFullArguments().get(argument.substring(2, argument.length()))
							: head.getShortArguments().get(argument.substring(1, argument.length()));

					if (argHandler != null)
					{
						if (argHandler.hasValue())
						{
							String value = null;

							if (equalSign == -1 && i + 1 < elements.size() && !elements.get(i + 1).startsWith("-"))
							{
								i++;
								value = elements.get(i);
							} else if (equalSign != -1)
							{
								value = e.substring(equalSign + 1, e.length());
							}

							argumentResults
									.add(new ArgumentResult(argHandler, argHandler.handle(value == "" ? null : value, head)));
						} else
						{
							argumentResults.add(new ArgumentResult(argHandler, argHandler.handle(null, head)));
						}
					} else
					{
						System.err.println("Could not find argument \"" + argument.toString() + "\" in \"" + head.toString()
								+ "\". Ignoring the argument and its possible value");

						if (equalSign == -1 && i + 1 < elements.size() && !elements.get(i + 1).startsWith("-")) // If next
																																				// element
																																				// is the
																																				// value
																																				// then we
																																				// skip it
						{
							i++;
						}

					}
				} else // It's a value for the command
				{
					commandValues.add(e);
				}
			}
			
			head.handle(argumentResults, commandValues);
		}
	}

	public static ArgumentResult getArgumentByFullName(String fullName, Iterable<ArgumentResult> arguments)
	{
		for (ArgumentResult i : arguments)
		{
			if (i.argumentHandler.getFullName() == fullName)
			{
				return i;
			}
		}

		return null;
	}

	public static ArgumentResult getArgumentByShortName(String shortName, Iterable<ArgumentResult> arguments)
	{
		for (ArgumentResult i : arguments)
		{
			if (i.argumentHandler.getShortName() == shortName)
			{
				return i;
			}
		}

		return null;
	}

	/// "Some words separated with spaces"wordsAttachedToAQuote
	/// it yields true

	/// wordsAttachedToAQuote"Some words separated with spaces"
	/// it yields true

	/// "Some words separated with spaces"
	/// it yields false

	/// Some words separated with spaces
	/// this is unacceptable, will result in unexpected behavior

	/// "Some words separated with spaces'
	/// this is unacceptable, will result in unexpected behavior

	/// "Some words separated with spaces
	/// this is unacceptable, will result in unexpected behavior

	/// Some "words separated" with spaces
	/// this is unacceptable, will result in unexpected behavior
	public static boolean isMixedWithQuote(String value)
	{
		char openingQuotationMark = value.charAt(0);
		char closingQuotationMark = value.charAt(value.length() - 1);

		if ((openingQuotationMark == '"' || openingQuotationMark == '\'') && closingQuotationMark != openingQuotationMark)
		{
			return true;
		}

		return false;
	}

	public static List<String> splitString(String value)
	{
		value = value.trim();

		List<String> ret = new ArrayList<String>(Arrays.asList(""));
		char quotationMark = 0;

		for (char i : value.toCharArray())
		{
			if (quotationMark != 0)
			{
				if (i == quotationMark)
				{
					quotationMark = 0;
					continue;
				}

				ret.set(ret.size() - 1, ret.get(ret.size() - 1) + i);
			} else
			{
				if (i == ' ' && ret.get(ret.size() - 1) == "")
				{
					continue;
				} else if (i == ' ')
				{
					ret.add("");
				} else if (i == '"' || i == '\'')
				{
					quotationMark = i;
				} else
				{
					ret.set(ret.size() - 1, ret.get(ret.size() - 1) + i);
				}
			}
		}

//		for (int i = 0; i < value.length(); i++)
//		{	
//			char c = value.charAt(i);
//
//			if (quotationMark != 0)
//			{
//				if (c == quotationMark)
//				{	
//					quotationMark = 0;
//					continue;
//				}
//				
//				ret.set(ret.size() - 1, ret.get(ret.size() - 1) + c);
//			} else
//			{
//				if (c == ' ' && ret.get(ret.size() - 1) == "")
//				{
//					continue;
//				} else if (c == ' ')
//				{
//					ret.add("");
//				} else if ((c == '"' || c == '\'') && ret.get(ret.size() - 1) == "")
//				{
//					quotationMark = c;
//				} else
//				{
//					ret.set(ret.size() - 1, ret.get(ret.size() - 1) + c);
//				}
//			}
//		}

		return ret;
	}
}
