package chatserver.network.gameserver.clientpackets;

import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.network.gameserver.AbstractGameClientPacket;
import chatserver.network.gameserver.GsAuthResponse;
import chatserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;
import chatserver.network.netty.handler.GameChannelHandler;
import chatserver.network.netty.handler.GameChannelHandler.State;
import chatserver.service.GameServerService;


/**
 * 
 * @author ATracer
 * 
 */
public class CM_CS_AUTH extends AbstractGameClientPacket
{
	/**
	 * Password for authentication
	 */
	private String				password;

	/**
	 * Id of GameServer
	 */
	private byte				gameServerId;

	/**
	 * Default address for server
	 */
	private byte[]				defaultAddress;

	private GameServerService	gameServerService;

	public CM_CS_AUTH(ChannelBuffer buf, GameChannelHandler gameChannelHandler, GameServerService gameServerService)
	{
		super(buf, gameChannelHandler, 0x00);
		this.gameServerService = gameServerService;
	}

	@Override
	protected void readImpl()
	{
		gameServerId = (byte) readC();

		defaultAddress = readB(readC());
		password = readS();
	}

	@Override
	protected void runImpl()
	{
		GsAuthResponse resp = gameServerService.registerGameServer(gameChannelHandler, gameServerId, defaultAddress,
			password);

		switch (resp)
		{
			case AUTHED:
				gameChannelHandler.setState(State.AUTHED);
				gameChannelHandler.sendPacket(new SM_GS_AUTH_RESPONSE(resp));
				break;
			case NOT_AUTHED:
				gameChannelHandler.sendPacket(new SM_GS_AUTH_RESPONSE(resp));
				break;
			default:
				gameChannelHandler.close(new SM_GS_AUTH_RESPONSE(resp));
		}
	}
}
