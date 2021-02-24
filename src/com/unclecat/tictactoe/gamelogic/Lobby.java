package com.unclecat.tictactoe.gamelogic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.unclecat.tictactoe.JoinRequest;
import com.unclecat.tictactoe.Main;
import com.unclecat.tictactoe.player.Player;
import com.unclecat.tictactoe.player.PlayerColors;
import com.unclecat.tictactoe.player.PlayerSigns;

/*
 * Represents a game, people can connect to
 */
public class Lobby
{
	protected GameGrid gameGrid;
	protected List<Player> players = new ArrayList<Player>();
	protected boolean hasStarted = false;
	protected int maxPlayers;
	protected String uniqueName;
	protected String cachedPlayersListJson;

	public Lobby(int maxPlayers, String uniqueName, int width, int height)
	{
		super();
		this.maxPlayers = maxPlayers;
		this.uniqueName = uniqueName;
		this.gameGrid = new GameGrid(width, height);
	}

	public void startGame() throws MalformedURLException, IOException
	{
		for (Player i : players)
		{
			HttpURLConnection con = (HttpURLConnection) new URL("http://" + i.getIp().toString() + "/game/start_pending_game").openConnection();
		
			con.
		}
		
		hasStarted = true;
	}
	
	public boolean canJoin()
	{
		return !hasStarted && players.size() < maxPlayers;
	}

	/*
	 * Returns false if failed to join
	 */
	public boolean join(JoinRequest joinRequest)
	{
		if (hasStarted || players.size() >= maxPlayers)
		{
			return false;
		}

		PlayerSigns sign = getFreeSign();

		if (sign == null)
		{
			return false;
		}

		players.add(new Player(PlayerColors.values()[sign.ordinal()], sign, joinRequest.ip,
				resolvePlayerNameConflicts(joinRequest.name)));
		cachedPlayersListJson = Main.gson.toJson(players);
		sendPlayersListToPlayers();

		return true;
	}

	public String resolvePlayerNameConflicts(String nameToAdd)
	{
		int addedNumber = 2;
		String ret = nameToAdd;

		while (true)
		{
			for (Player i : players)
			{
				if (i.getName() == ret)
				{
					ret = nameToAdd + " " + Integer.toString(addedNumber++);
				} else
				{
					return ret;
				}
			}
		}
	}

	public void sendPlayersListToPlayers()
	{
		for (Player i : players)
		{
			try
			{
				HttpURLConnection con = (HttpURLConnection) new URL(
						"http://" + i.getIp().toString() + "/game/update_players_in_lobby").openConnection();

				con.setRequestMethod("POST");
				con.connect();

				try (Writer w = new OutputStreamWriter(con.getOutputStream()))
				{
					w.write(cachedPlayersListJson);
				} catch (Exception e2)
				{
					e2.printStackTrace();
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	public PlayerSigns getFreeSign()
	{
		List<PlayerSigns> signs = new ArrayList<PlayerSigns>(Arrays.asList(PlayerSigns.values())); // TODO: Cache the
																																	// casted array

		for (Player i : players)
		{
			signs.remove(i.getSign());
		}

		return signs.size() <= 0 ? null : signs.get(0);
	}

	public String getUniqueName()
	{
		return uniqueName;
	}

	public void setUniqueName(String uniqueName)
	{
		this.uniqueName = uniqueName;
	}

	public int getMaxPlayers()
	{
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers)
	{
		this.maxPlayers = maxPlayers;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public void setPlayers(List<Player> players)
	{
		this.players = players;
	}

	public boolean isHasStarted()
	{
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted)
	{
		this.hasStarted = hasStarted;
	}
}
