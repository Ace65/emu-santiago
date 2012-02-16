package chatserver.network.gameserver.serverpackets;

import org.jboss.netty.buffer.ChannelBuffer;

import chatserver.model.ChatClient;
import chatserver.network.gameserver.AbstractGameServerPacket;
import chatserver.network.netty.handler.GameChannelHandler;


/**
 * @author ATracer
 */
public class SM_PLAYER_AUTH_RESPONSE extends AbstractGameServerPacket
{
	private int		playerId;
	private byte[]	token;

	public SM_PLAYER_AUTH_RESPONSE(ChatClient chatClient)
	{
		super(0x01);
		this.playerId = chatClient.getClientId();
		token = chatClient.getToken();
	}

	@Override
	protected void writeImpl(GameChannelHandler gameChannelHandler, ChannelBuffer buf)
	{
		writeC(buf, getOpCode());
		writeD(buf, playerId);
		writeC(buf, token.length);
		writeB(buf, token);
	}

}
