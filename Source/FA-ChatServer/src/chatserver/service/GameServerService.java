package chatserver.service;

import chatserver.configs.Config;
import chatserver.network.gameserver.GsAuthResponse;
import chatserver.network.netty.handler.GameChannelHandler;

/**
 * 
 * @author ATracer
 *
 */
public class GameServerService
{
	public static byte GAMESERVER_ID;
	/**
	 * 
	 * @param gameChannelHandler
	 * @param gameServerId
	 * @param defaultAddress
	 * @param password
	 * @return
	 */
	public GsAuthResponse registerGameServer(GameChannelHandler gameChannelHandler, byte gameServerId,
		byte[] defaultAddress, String password)
	{
		GAMESERVER_ID = gameServerId;
		return passwordConfigAuth(password);
	}

	/**
	 * 
	 * @return
	 */
	private GsAuthResponse passwordConfigAuth(String password)
	{
		if (password.equals(Config.GAME_SERVER_PASSWORD))
			return GsAuthResponse.AUTHED;

		return GsAuthResponse.NOT_AUTHED;
	}

}
