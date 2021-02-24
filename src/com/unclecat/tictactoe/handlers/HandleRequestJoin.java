package com.unclecat.tictactoe.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.unclecat.tictactoe.JoinRequest;
import com.unclecat.tictactoe.Main;
import com.unclecat.tictactoe.gamelogic.Lobby;
import com.unclecat.tictactoe.gamelogic.LobbyManager;
import com.unclecat.tictactoe.util.NetworkUtils;

public class HandleRequestJoin implements HttpHandler
{
	@Override
	public void handle(HttpExchange exc) throws IOException
	{
		try (OutputStream s = exc.getResponseBody();)
		{
			InetSocketAddress ip = NetworkUtils.getIpSocketAddress(exc);

			if (ip == null)
			{
				exc.sendResponseHeaders(400, 0);
				s.write("couldNotDetermineIpAddress".getBytes());
			} else
			{
				Map<String, String> query = NetworkUtils.parseQuery(exc.getRequestURI().getQuery());

				if (query == null)
				{
					exc.sendResponseHeaders(400, 0);
					s.write("wrongQuery".getBytes());
				} else
				{
					String maxPlayersTextual = query.get("max_players");
					String playerName = query.get("player_name");

					if (maxPlayersTextual == null || playerName == null)
					{
						exc.sendResponseHeaders(400, 0);
						s.write("wrongQuery".getBytes());
					} else
					{
						try
						{
							int maxPlayers = Integer.parseInt(maxPlayersTextual);

							if (maxPlayers <= 1 || maxPlayers > LobbyManager.PLAYERS_LIMIT)
							{
								exc.sendResponseHeaders(400, 0);
								s.write("couldNotJoinLobby".getBytes());
							} else
							{
								try
								{
									if (Main.lobbyManager.join(new JoinRequest(maxPlayers, ip, playerName)))
									{
										exc.sendResponseHeaders(200, 0);
									} else
									{
										exc.sendResponseHeaders(400, 0);
										s.write("couldNotJoinLobby".getBytes());
									}
								} catch (Exception e)
								{
									e.printStackTrace();

									exc.sendResponseHeaders(400, 0);
									s.write("couldNotJoinLobby".getBytes());
								}
							}
						} catch (Exception e)
						{
							exc.sendResponseHeaders(400, 0);
							s.write("wrongQuery".getBytes());
						}
					}
				}
			}
		} catch (IOException e)
		{
			System.out.println("Failed to send response code in " + this.toString());
			e.printStackTrace();
		} finally
		{
			exc.close();
		}
	}
}
