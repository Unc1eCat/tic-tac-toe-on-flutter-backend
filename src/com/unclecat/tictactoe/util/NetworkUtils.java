package com.unclecat.tictactoe.util;

import java.net.InetSocketAddress;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

public class NetworkUtils
{
	public static InetSocketAddress getIpSocketAddress(HttpExchange exc)
	{
		InetSocketAddress ip = null;
		String ipTextual = exc.getRequestHeaders().containsKey("X-Forwarded-For") ? (exc.getRequestHeaders().get("X-Forwarded-For").size() > 0
				? exc.getRequestHeaders().get("X-Forwarded-For").get(0)
				: null) : null;

		if (ipTextual == null || ipTextual.isEmpty() || ipTextual.contentEquals("unknown"))
		{
			ip = exc.getRemoteAddress();
		} else
		{
			try
			{
				ip = new InetSocketAddress(ipTextual.substring(0, ipTextual.lastIndexOf(':')),
						Integer.parseInt(ipTextual.substring(ipTextual.lastIndexOf(':'), ipTextual.length())));
			} catch (Exception e)
			{
				return null;
			}
		}
		
		return ip;
	}
	
	// TODO: Make it validate the query
	public static HashMap<String, String> parseQuery(String queryText)
	{
		if (queryText == null)
		{
			return null;
		}
		
		HashMap<String, String> ret = new HashMap<String, String>();
		
		try 
		{
			String[] splitQuery = queryText.split("&");
			
			for (String i : splitQuery)
			{
				String[] splitParameter = i.split("=");
				
				ret.put(splitParameter[0], splitParameter[1]);
			}
			return ret;
		} catch (Exception e)
		{
			System.err.println("Failed to parse query text \"" + queryText + "\"");
			e.printStackTrace();
			return null;
		}		
	}
}
