package com.unclecat.tictactoe.util.commandconsole;

public class ArgumentResult
{
	public final ArgumentHandler argumentHandler;
	public final Object resultValue;
	
	public ArgumentResult(ArgumentHandler argumentHandler, Object resultValue)
	{
		this.argumentHandler = argumentHandler;
		this.resultValue = resultValue;
	}	
	
//	public boolean equals(Object other)
//	{
//		return other.getClass() == ArgumentResult.class && ((ArgumentResult)other).resultValue == resultValue && ((ArgumentResult)other).argumentHandler == argumentHandler;
//	}
}
