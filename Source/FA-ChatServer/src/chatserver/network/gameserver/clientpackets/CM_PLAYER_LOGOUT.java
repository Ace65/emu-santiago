package chatserver.network.gameserver.clientpackets;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.network.gameserver.AbstractGameClientPacket;
import chatserver.network.netty.handler.GameChannelHandler;
import chatserver.service.ChatService;


/**
 * @author ATracer
 */
public class CM_PLAYER_LOGOUT extends AbstractGameClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_PLAYER_LOGOUT.class);

	private int					playerId;

	private ChatService			chatService;

	public CM_PLAYER_LOGOUT(ChannelBuffer buf, GameChannelHandler gameChannelHandler, ChatService chatService)
	{
		super(buf, gameChannelHandler, 0x02);
		this.chatService = chatService;
	}

	@Override
	protected void readImpl()
	{
		playerId = readD();
	}

	@Override
	protected void runImpl()
	{
		chatService.playerLogout(playerId);
		log.info("Player logout " + playerId);
	}
}
