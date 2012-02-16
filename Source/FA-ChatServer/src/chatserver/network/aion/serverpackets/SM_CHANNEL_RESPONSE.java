package chatserver.network.aion.serverpackets;

import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.model.ChatClient;
import chatserver.model.channel.Channel;
import chatserver.network.aion.AbstractServerPacket;
import chatserver.network.netty.handler.ClientChannelHandler;


/**
 * 
 * @author ATracer
 *
 */
public class SM_CHANNEL_RESPONSE extends AbstractServerPacket
{
	
	private Channel channel;
	private ChatClient chatClient;
	
	public SM_CHANNEL_RESPONSE(ChatClient chatClient, Channel channel)
	{
		super(0x11);
		this.chatClient = chatClient;
		this.channel = channel;
	}

	@Override
	protected void writeImpl(ClientChannelHandler cHandler, ChannelBuffer buf)
	{
		writeC(buf, getOpCode());
		writeC(buf, 0x40);
		writeH(buf, chatClient.nextIndex());
		writeH(buf, 0x00);
		writeD(buf, channel.getChannelId());
//		writeC(buf, 0x19);
//		writeC(buf, 0x01);
//		writeC(buf, 0x80);
	}

}
