package com.unclecat.tictactoe.player;

public class SvgPlayerSign implements PlayerSign
{
	public SvgPlayerSign(String svgData)
	{
		super();
		this.svgData = svgData;
	}

	protected String svgData;
	
	@Override
	public String toString()
	{
		return svgData;
	}
}
