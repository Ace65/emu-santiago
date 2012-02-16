package chatserver.network.gameserver;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.common.netty.AbstractPacketHandler;
import chatserver.network.gameserver.clientpackets.CM_CS_AUTH;
import chatserver.network.gameserver.clientpackets.CM_PLAYER_AUTH;
import chatserver.network.gameserver.clientpackets.CM_PLAYER_LOGOUT;
import chatserver.network.netty.handler.GameChannelHandler;
import chatserver.network.netty.handler.GameChannelHandler.State;
import chatserver.service.ChatService;
import chatserver.service.GameServerService;

import com.google.inject.Inject;

/**
 * @author ATracer
 */
public class GameServerPacketHandler extends AbstractPacketHandler
{
	@SuppressWarnings("unused")
	private static final Logger	log	= Logger.getLogger(GameServerPacketHandler.class);

	@Inject
	private GameServerService	gameServerService;
	@Inject
	private ChatService			chatService;

	/**
	 * Reads one packet from ChannelBuffer
	 * 
	 * @param buf
	 * @param channelHandler
	 * @return AbstractGameClientPacket
	 */
	public AbstractGameClientPacket handle(ChannelBuffer buf, GameChannelHandler channelHandler)
	{
		byte opCode = buf.readByte();
		State state = channelHandler.getState();
		AbstractGameClientPacket gamePacket = null;

		switch (state)
		{
			case CONNECTED:
			{
				switch (opCode)
				{
					case 0x00:
						gamePacket = new CM_CS_AUTH(buf, channelHandler, gameServerService);
						break;
					default:
						unknownPacket(opCode, state.toString());
				}
				break;
			}
			case AUTHED:
			{
				switch (opCode)
				{
					case 0x01:
						gamePacket = new CM_PLAYER_AUTH(buf, channelHandler, chatService);
						break;
					case 0x02:
						gamePacket = new CM_PLAYER_LOGOUT(buf, channelHandler, chatService);
						break;
					default:
						unknownPacket(opCode, state.toString());
				}
				break;
			}
		}

		return gamePacket;
	}

}
