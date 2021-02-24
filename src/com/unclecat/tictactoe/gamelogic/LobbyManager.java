package com.unclecat.tictactoe.gamelogic;

import java.util.ArrayList;
import java.util.List;

import com.unclecat.tictactoe.JoinRequest;

public class LobbyManager
{
	protected List<Lobby> lobbies = new ArrayList<Lobby>();
	protected long generatedLobbyId = 0;

	public static final int MAX_LOBBIES = 900;
	public static final double JOIN_PERCENTAGE = 0.8;
	public static final double PLAYERS_LIMIT = 7;

	protected Object joinMonitor = new Object();

	// Returns false if could not join
	public boolean join(JoinRequest joinRequest)
	{
		synchronized (joinMonitor)
		{
			Lobby mostRelevant = findMostRelevantToJoin(joinRequest.maxPlayers,
					(int) Math.floor(joinRequest.maxPlayers * JOIN_PERCENTAGE));

			if (mostRelevant != null && mostRelevant.join(joinRequest))

			{
				System.out.println(lobbies);

				return true;
			} else
			{
				if (lobbies.size() < MAX_LOBBIES)
				{
					// TODO: The generatedLobbyId can hit the maximum
					Lobby newLobby = new Lobby(joinRequest.maxPlayers, Long.toString(generatedLobbyId++), 3, 3);

					if (newLobby.join(joinRequest))
					{
						lobbies.add(newLobby);

						System.out.println(lobbies);

						return true;
					} else
					{
						return false;
					}
				} else
				{
					return false;
				}
			}
		}
	}
	
	/*
	 * Clears the lobbies list. Basically stops all games and removes all players from them
	 */
	public void clear()
	{
		lobbies.clear();
	}
	
	public Lobby findMostRelevantToJoin(int maxPlayers, int relevantPlayersAmount) // TODO: It is not working right... Just try sending dummy join requests and look how lobbies are filled
	{
		Lobby mostRelevant = null;
		int mostRelevantDiff = 100;

		for (int i = 0; i < lobbies.size(); i++)
		{
			if (mostRelevant == null)
			{
				if ((lobbies.get(i).maxPlayers == maxPlayers && !lobbies.get(i).hasStarted))
				{
					mostRelevant = lobbies.get(i);
					mostRelevantDiff = Math.abs(lobbies.get(i).getPlayers().size() - relevantPlayersAmount);
				}
			} else
			{
//				if (lobbies.get(i).maxPlayers == maxPlayers && lobbies.get(i).canJoin()
//						&& Math.abs(lobbies.get(i).getPlayers().size() - relevantPlayersAmount) < mostRelevantDiff)
//				{
//					mostRelevant = lobbies.get(i);
//					mostRelevantDiff = Math.abs(lobbies.get(i).getPlayers().size() - relevantPlayersAmount);
//				}
				if (lobbies.get(i).maxPlayers == maxPlayers && lobbies.get(i).canJoin()
						&& Math.abs(lobbies.get(i).getPlayers().size() - maxPlayers) < mostRelevantDiff)
				{
					mostRelevant = lobbies.get(i);
					mostRelevantDiff = Math.abs(lobbies.get(i).getPlayers().size() - maxPlayers);
				}
			}
		}

		return mostRelevant;
	}
}
