package chatserver.network.aion;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.common.netty.AbstractPacketHandler;
import chatserver.network.aion.clientpackets.CM_CHANNEL_MESSAGE;
import chatserver.network.aion.clientpackets.CM_CHANNEL_REQUEST;
import chatserver.network.aion.clientpackets.CM_PLAYER_AUTH;
import chatserver.network.netty.handler.ClientChannelHandler;
import chatserver.network.netty.handler.ClientChannelHandler.State;
import chatserver.service.BroadcastService;
import chatserver.service.ChatService;

import com.google.inject.Inject;

/**
 * @author ATracer
 */
public class ClientPacketHandler extends AbstractPacketHandler
{
	@Inject
	private BroadcastService broadcastService;
	@Inject
	private ChatService chatService;
	
	@SuppressWarnings("unused")
	private static final Logger	log	= Logger.getLogger(ClientPacketHandler.class);

	/**
	 * Reads one packet from ChannelBuffer
	 * 
	 * @param buf
	 * @param channelHandler
	 * @return AbstractClientPacket
	 */
	public AbstractClientPacket handle(ChannelBuffer buf, ClientChannelHandler channelHandler)
	{
		byte opCode = buf.readByte();
		State state = channelHandler.getState();
		AbstractClientPacket clientPacket = null;

		switch (state)
		{
			case CONNECTED:
				switch (opCode)
				{
					case 0x05:
						clientPacket = new CM_PLAYER_AUTH(buf, channelHandler, chatService);
						break;
					default:
						//unknownPacket(opCode, state.toString());
				}
				break;
			case AUTHED:
				switch (opCode)
				{
					case 0x10:
					case 0x2C:
					case 0x12:
						clientPacket = new CM_CHANNEL_REQUEST(buf, channelHandler, chatService);
						break;
					case 0x18:
						clientPacket = new CM_CHANNEL_MESSAGE(buf, channelHandler, broadcastService);
					default:
						//unknownPacket(opCode, state.toString());
				}
				break;
		}

		return clientPacket;
	}
}
