package com.unclecat.tictactoe;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * When a client is online, it periodically sends a request about that. The server records the time of the request arrival and 
 * IP address of the client. Periodically the server removes from the list of online clients those whichs' time difference to
 * the current time is greater than specified duration.
 * A problem is that anyone who is bad may spoil this system by simply sending the requests about online status.
 * Later I will move from IP addresses to IDs given to clients.
 */
public class OnlineStatusManager
{
	protected volatile Map<InetAddress, Long> onlineClients = new ConcurrentHashMap<InetAddress, Long>();
	protected Thread statusManagerThread;
	protected long actualityDurationMillis;
	protected long checkPeriodicityMillis;
	protected long minimumSleepTimeMillis;
	
	public OnlineStatusManager(long onlineActualityDurationMillis, long checkPeriodicityMillis, long minimumSleepTimeMillis)
	{
		this.actualityDurationMillis = onlineActualityDurationMillis;
		this.checkPeriodicityMillis = checkPeriodicityMillis;
		this.minimumSleepTimeMillis = minimumSleepTimeMillis;
		
		statusManagerThread = new Thread(() -> {
			while (true)
			{
				long start = System.nanoTime();
				
				for (InetAddress i : onlineClients.keySet())
				{
					if (System.currentTimeMillis() - onlineClients.get(i).longValue() > actualityDurationMillis)
					{
						System.out.println("REMOVE " + i);
						onlineClients.remove(i);
					}
				}
				
				long executionTime = System.nanoTime() - start;
				
				try
				{
					Thread.sleep((checkPeriodicityMillis - executionTime / 100) < minimumSleepTimeMillis ? minimumSleepTimeMillis : (checkPeriodicityMillis - executionTime  / 100));
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}, "Thread-OnlineStatusManager"); 
		
		statusManagerThread.start();
	}
	
	public void update(InetSocketAddress ip)
	{
		if (onlineClients.containsKey(ip.getAddress()))
		{
			System.out.println("UPDATE " + ip);
			onlineClients.replace(ip.getAddress(), System.currentTimeMillis());			
		} else
		{
			System.out.println("ADD " + ip);
			onlineClients.put(ip.getAddress(), System.currentTimeMillis());
		}
	}
	
	public int getOnlineAmount()
	{
		return onlineClients.size();
	}
}
