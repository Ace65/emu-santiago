package chatserver.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import chatserver.model.ChatClient;
import chatserver.model.message.Message;
import chatserver.network.aion.serverpackets.SM_CHANNEL_MESSAGE;
import chatserver.network.netty.handler.ClientChannelHandler;


/**
 * 
 * @author ATracer
 *
 */
public class BroadcastService
{
	private Map<Integer, ChatClient> clients = new ConcurrentHashMap<Integer, ChatClient>();
	
	/**
	 * 
	 * @param client
	 */
	public void addClient(ChatClient client)
	{
		clients.put(client.getClientId(), client);
	}
	
	/**
	 * 
	 * @param client
	 */
	public void removeClient(ChatClient client)
	{
		clients.remove(client.getClientId());
	}
	
	/**
	 * 
	 * @param message
	 */
	public void broadcastMessage(Message message)
	{
		for(ChatClient client : clients.values())
		{
			if(client.isInChannel(message.getChannel()))
				sendMessage(client, message);
		}
	}
	
	/**
	 * 
	 * @param chatClient
	 * @param message
	 */
	public void sendMessage(ChatClient chatClient, Message message)
	{
		ClientChannelHandler cch = chatClient.getChannelHandler();
		cch.sendPacket(new SM_CHANNEL_MESSAGE(message));
	}
	
}
