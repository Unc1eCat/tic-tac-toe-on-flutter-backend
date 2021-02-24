package com.unclecat.tictactoe.gamelogic;

import java.util.List;

import com.unclecat.tictactoe.gamelogic.slots.GridSlot;

public class GameGrid
{
	protected List<List<GridSlot>> grid;
	protected int width;
	protected int height;

	public GameGrid(int width, int height)
	{
		super();
		this.width = width;
		this.height = height;
	}

	public boolean isWithinGrid(int x, int y)
	{
		return x >= 0 && y >= 0 && x < width && y < height;
	}
	
	public void setSlotAt(GridSlot slot, int x, int y)
	{
		grid.get(x).set(y, slot);
	}

	public GridSlot getSlotAt(int x, int y)
	{
		return grid.get(x).get(y);
	}

	public List<List<GridSlot>> getGrid()
	{
		return grid;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
}
