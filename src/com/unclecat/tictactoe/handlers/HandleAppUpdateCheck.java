package com.unclecat.tictactoe.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HandleAppUpdateCheck implements HttpHandler
{
	@Override
	public void handle(HttpExchange exc) throws IOException
	{
		OutputStream out = exc.getResponseBody();
		
		exc.sendResponseHeaders(200, 0);
		out.write("update&https://play.google.com/store/apps/details?id=com.unclecat.tictactoe".getBytes());
		out.close();
	}
}
