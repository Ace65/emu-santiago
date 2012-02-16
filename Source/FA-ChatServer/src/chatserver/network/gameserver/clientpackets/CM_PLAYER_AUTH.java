package chatserver.network.gameserver.clientpackets;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.model.ChatClient;
import chatserver.network.gameserver.AbstractGameClientPacket;
import chatserver.network.gameserver.serverpackets.SM_PLAYER_AUTH_RESPONSE;
import chatserver.network.netty.handler.GameChannelHandler;
import chatserver.service.ChatService;


/**
 * @author ATracer
 */
public class CM_PLAYER_AUTH extends AbstractGameClientPacket
{
	private static final Logger	log	= Logger.getLogger(CM_PLAYER_AUTH.class);

	private int					playerId;
	
	private String				playerLogin;

	private ChatService			chatService;

	public CM_PLAYER_AUTH(ChannelBuffer buf, GameChannelHandler gameChannelHandler, ChatService chatService)
	{
		super(buf, gameChannelHandler, 0x01);
		this.chatService = chatService;
	}

	@Override
	protected void readImpl()
	{
		playerId = readD();
		playerLogin = readS();
	}

	@Override
	protected void runImpl()
	{
		ChatClient chatClient = null;
		try
		{
			chatClient = chatService.registerPlayer(playerId, playerLogin);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error("Error registering player on ChatServer: " + e.getMessage());
		}
		catch (UnsupportedEncodingException e)
		{
			log.error("Error registering player on ChatServer: " + e.getMessage());
		}

		if (chatClient != null)
		{
			gameChannelHandler.sendPacket(new SM_PLAYER_AUTH_RESPONSE(chatClient));
		}
		else
		{
			log.info("Player was not authed " + playerId);
		}
	}
}
