package com.unclecat.tictactoe;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import com.unclecat.tictactoe.gamelogic.LobbyManager;
import com.unclecat.tictactoe.handlers.HandleAppUpdateCheck;
import com.unclecat.tictactoe.handlers.HandleGetOnlineAmount;
import com.unclecat.tictactoe.handlers.HandleOnlineStatusUpdate;
import com.unclecat.tictactoe.handlers.HandleRequestJoin;
import com.unclecat.tictactoe.util.commandconsole.ArgumentHandler;
import com.unclecat.tictactoe.util.commandconsole.ArgumentResult;
import com.unclecat.tictactoe.util.commandconsole.Command;
import com.unclecat.tictactoe.util.commandconsole.CommandHandler;
import com.unclecat.tictactoe.util.commandconsole.defaults.EmptyHandlerCommand;
import com.unclecat.tictactoe.util.commandconsole.defaults.HelpCommand;
import com.unclecat.tictactoe.util.commandconsole.defaults.QuitCommand;

// TODO: Move to more reliable way to identify players rather than IP address
public class Main
{
	public static OnlineStatusManager onlineStatusManager;
	public static LobbyManager lobbyManager;
	public static Gson gson;

	public static void main(String[] args)
	{
		System.out.println("Launched at " + new Date());

		ArrayList<CommandHandler> commands = new ArrayList<CommandHandler>();

		commands.add(new QuitCommand());
		commands.add(new HelpCommand(commands));
		commands.add(new Command("clearlobbies", "Clears the lobbies list.", null, (ArgumentHandler[])null)
		{
			@Override
			public void handle(List<ArgumentResult> arguments, List<String> values)
			{
				lobbyManager.clear();
				System.out.println("Cleared all lobbies.");
			}
		});

		Command mainCommand = new EmptyHandlerCommand("server", "Main command", commands,
				new ArrayList<ArgumentHandler>());

		gson = new Gson();
		onlineStatusManager = new OnlineStatusManager(12000, 12000, 2000);
		lobbyManager = new LobbyManager();

		try
		{
			HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 1221), 0);

			server.createContext("/game/update_online_status").setHandler(new HandleOnlineStatusUpdate());
			server.createContext("/game/request_join").setHandler(new HandleRequestJoin());
			server.createContext("/game/get_online_amount").setHandler(new HandleGetOnlineAmount());
			server.createContext("/game/app_update_check").setHandler(new HandleAppUpdateCheck());

			server.start();

			mainCommand.read(System.in);

			server.stop(10);
		} catch (IOException e)
		{
			System.out.println("Failed to initialize the server");
			e.printStackTrace();
		}
	}
}
