package chatserver.network.aion.clientpackets;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.model.ChatClient;
import chatserver.model.channel.Channel;
import chatserver.network.aion.AbstractClientPacket;
import chatserver.network.aion.serverpackets.SM_CHANNEL_RESPONSE;
import chatserver.network.netty.handler.ClientChannelHandler;
import chatserver.service.ChatService;


/**
 * @author ATracer
 */
public class CM_CHANNEL_REQUEST extends AbstractClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_CHANNEL_REQUEST.class);

	private int					channelIndex;
	private byte[]				channelIdentifier;

	private ChatService			chatService;

	/**
	 * 
	 * @param channelBuffer
	 * @param gameChannelHandler
	 * @param opCode
	 */
	public CM_CHANNEL_REQUEST(ChannelBuffer channelBuffer, ClientChannelHandler gameChannelHandler,
		ChatService chatService)
	{
		super(channelBuffer, gameChannelHandler, 0x10);
		this.chatService = chatService;
	}

	@Override
	protected void readImpl()
	{
		readC(); // 0x40
		readH(); // 0x00
		channelIndex = readH();
		int length = readH() * 2;
		channelIdentifier = readB(length);
	}

	@Override
	protected void runImpl()
	{
//		try
//		{
//			log.info("Channel requested " + new String(channelIdentifier, "UTF-16le"));
//		}
//		catch (UnsupportedEncodingException e)
//		{
//			e.printStackTrace();
//		}
		if(channelIdentifier.length < 1)
			return;

		ChatClient chatClient = clientChannelHandler.getChatClient();
		Channel channel = chatService.registerPlayerWithChannel(chatClient, channelIndex, channelIdentifier);
		if (channel != null)
		{
			clientChannelHandler.sendPacket(new SM_CHANNEL_RESPONSE(chatClient, channel));
		}else
			log.warn("Channel not found: " + new String(channelIdentifier).replace(" ", ""));
	}

	@Override
	public String toString()
	{
		return "CM_CHANNEL_REQUEST [channelIndex=" + channelIndex + ", channelIdentifier="
			+ new String(channelIdentifier) + "]";
	}
}
