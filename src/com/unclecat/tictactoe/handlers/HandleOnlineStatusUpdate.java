package com.unclecat.tictactoe.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.unclecat.tictactoe.Main;
import com.unclecat.tictactoe.util.NetworkUtils;

public class HandleOnlineStatusUpdate implements HttpHandler
{
	@Override
	public void handle(HttpExchange exc)
	{
		InetSocketAddress ip = NetworkUtils.getIpSocketAddress(exc);	

		if (ip == null)
		{
			try (OutputStream s = exc.getResponseBody();)
			{
				exc.sendResponseHeaders(400, 0);
				s.write("couldNotDetermineIpAddress".getBytes());
			} catch (IOException e)
			{
				System.out.println("Failed to send response code in " + this.toString());
				e.printStackTrace();
			}
		} else
		{
			try
			{
				exc.sendResponseHeaders(200, 0);
			} catch (IOException e)
			{
				System.out.println("Failed to send response code in " + this.toString());
				e.printStackTrace();
			} finally
			{
				exc.close();
				Main.onlineStatusManager.update(ip);
			}
		}
	}
}
