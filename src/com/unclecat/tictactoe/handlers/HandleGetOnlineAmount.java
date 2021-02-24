package com.unclecat.tictactoe.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.unclecat.tictactoe.Main;

public class HandleGetOnlineAmount implements HttpHandler
{
	@Override
	public void handle(HttpExchange exc) throws IOException
	{
		OutputStream out = exc.getResponseBody();
		
		exc.sendResponseHeaders(200, 0);
		out.write(Main.onlineStatusManager.getOnlineAmount());	
		
		out.close();
	}
}
