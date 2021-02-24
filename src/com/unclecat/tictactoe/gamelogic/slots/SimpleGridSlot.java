package com.unclecat.tictactoe.gamelogic.slots;

import com.unclecat.tictactoe.player.Player;

public class SimpleGridSlot implements GridSlot
{
	protected Player occupier;

	public SimpleGridSlot()
	{
		
	}
	
	public Player getOccupier()
	{
		return occupier;
	}

	public void setOccupier(Player occupier)
	{
		this.occupier = occupier;
	}
}
